package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.code.widget.NodeIOWidget;
import de.blazemcworld.fireflow.code.widget.NodeWidget;
import de.blazemcworld.fireflow.code.widget.Widget;
import de.blazemcworld.fireflow.code.widget.WireWidget;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.coordinate.Vec;

import java.util.*;

public class Pathfinder {

    private final CodeEditor editor;

    public Pathfinder(CodeEditor editor) {
        this.editor = editor;
    }

    public List<Vec> findPath(Vec start, Vec end) {
        start = start.withZ(0);
        end = end.withZ(0);

        HashMap<Widget, Vec> sizes = new HashMap<>();
        List<Double> xChanges = new ArrayList<>();
        List<Double> yChanges = new ArrayList<>();
        List<Pair<Vec, Vec>> wires = new ArrayList<>();

        for (Widget w : editor.rootWidgets) {
            if (!(w instanceof NodeWidget n)) continue;
            sizes.put(w, w.getSize());

            Vec size = sizes.computeIfAbsent(w, Widget::getSize);
            Vec pos = w.getPos();
            xChanges.add(pos.x() + 0.25);
            xChanges.add(pos.x() - size.x() - 0.25);
            yChanges.add(pos.y() + 0.25);
            yChanges.add(pos.y() - size.y() - 0.25);

            for (NodeIOWidget io : n.getIOWidgets()) {
                for (WireWidget c : io.connections) {
                    for (WireWidget part : c.getFullWire()) {
                        Vec min = part.line.from.min(part.line.to).withZ(0);
                        Vec max = part.line.from.max(part.line.to).withZ(0);
                        wires.add(Pair.of(min, max));
                        xChanges.add(min.x() - 0.25);
                        xChanges.add(max.x() + 0.25);
                        yChanges.add(min.y() - 0.25);
                        yChanges.add(max.y() + 0.25);
                    }
                }
            }
        }

        xChanges.add(end.x());
        yChanges.add(end.y());

        xChanges = new ArrayList<>(new HashSet<>(xChanges));
        yChanges = new ArrayList<>(new HashSet<>(yChanges));
        wires = new ArrayList<>(new HashSet<>(wires));

        xChanges.sort(Double::compareTo);
        yChanges.sort(Double::compareTo);

        HashMap<Vec, Vec> origins = new HashMap<>();
        HashMap<Vec, Double> penalty = new HashMap<>();
        HashMap<Vec, Double> order = new HashMap<>();
        PriorityQueue<Vec> todo = new PriorityQueue<>(Comparator.comparing(order::get));
        todo.add(start);
        origins.put(start, null);
        penalty.put(start, 0.0);
        order.put(start, 0.0);

        int computeLimit = 256;
        while (!todo.isEmpty()) {
            if (--computeLimit < 0) break;
            Vec current = todo.poll();

            if (current.equals(end)) {
                List<Vec> path = new ArrayList<>();
                while (current != null) {
                    path.add(current);
                    current = origins.get(current);
                }
                Collections.reverse(path);
                return path;
            }

            Pair<Double, Double> minMax = validXRange(current, sizes);
            for (double xChange : xChanges) {
                if (minMax.left() >= xChange) continue;
                if (minMax.right() <= xChange) break;
                Vec v = new Vec(xChange, current.y(), 0);
                double p = penalty.get(current) + 25;
                if (current == start) p = 0;
                if (v.equals(end)) p -= 25;

                double xMin = Math.min(xChange, current.x());
                double xMax = Math.max(xChange, current.x());
                for (Pair<Vec, Vec> wire : wires) {
                    if (wire.left().y() != wire.right().y()) continue;
                    if (wire.left().y() != current.y()) continue;
                    if (wire.left().x() > xMax || wire.right().x() < xMin) continue;
                    p += 25;
                }

                p += current.distance(v);
                if (origins.containsKey(v) && penalty.get(v) <= p) continue;
                origins.put(v, current);
                penalty.put(v, p);
                order.put(v, v.distance(end) + p);
                todo.add(v);
            }

            minMax = validYRange(current, sizes);
            for (double yChange : yChanges) {
                if (minMax.left() >= yChange) continue;
                if (minMax.right() <= yChange) break;
                Vec v = new Vec(current.x(), yChange, 0);
                double p = penalty.get(current) + 25;

                double yMin = Math.min(yChange, current.y());
                double yMax = Math.max(yChange, current.y());
                for (Pair<Vec, Vec> wire : wires) {
                    if (wire.left().x() != wire.right().x()) continue;
                    if (wire.left().x() != current.x()) continue;
                    if (wire.left().y() > yMax || wire.right().y() < yMin) continue;
                    p += 25;
                }

                if (origins.containsKey(v) && penalty.get(v) <= p) continue;
                origins.put(v, current);
                penalty.put(v, p);
                order.put(v, v.distanceSquared(end) + p);
                todo.add(v);
            }
        }

        List<Vec> path = new ArrayList<>();
        path.add(start);
        path.add(start.withX(Math.round((start.x() + end.x()) * 4) / 8.0));
        path.add(end.withX(Math.round((start.x() + end.x()) * 4) / 8.0));
        path.add(end);
        return path;
    }

    private Pair<Double, Double> validXRange(Vec current, HashMap<Widget, Vec> sizes) {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        for (Widget w : editor.rootWidgets) {
            if (!(w instanceof NodeWidget)) continue;

            Vec maxWall = w.getPos();
            Vec minWall = maxWall.sub(sizes.get(w));
            if (current.y() > maxWall.y() || current.y() < minWall.y()) continue;
            if (current.x() < minWall.x()) max = Math.min(max, minWall.x());
            if (current.x() > maxWall.x()) min = Math.max(min, maxWall.x());
        }
        return Pair.of(min, max);
    }

    private Pair<Double, Double> validYRange(Vec current, HashMap<Widget, Vec> sizes) {
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        for (Widget w : editor.rootWidgets) {
            if (!(w instanceof NodeWidget)) continue;

            Vec maxWall = w.getPos();
            Vec minWall = maxWall.sub(sizes.get(w));
            if (current.x() > maxWall.x() || current.x() < minWall.x()) continue;
            if (current.y() < minWall.y()) max = Math.min(max, minWall.y());
            if (current.y() > maxWall.y()) min = Math.max(min, maxWall.y());
        }
        return Pair.of(min, max);
    }
}
