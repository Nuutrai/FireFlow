package de.blazemcworld.fireflow.code.widget;

import de.blazemcworld.fireflow.code.Interaction;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.InstanceContainer;

import java.util.ArrayList;
import java.util.List;

public class GridWidget implements Widget {

    public int maxColumns;
    public double spacing = 0.125;
    public final List<Widget> widgets = new ArrayList<>();
    private Vec pos = Vec.ZERO;

    public GridWidget(int maxColumns) {
        this.maxColumns = maxColumns;
    }

    @Override
    public void setPos(Vec pos) {
        this.pos = pos;
    }

    @Override
    public Vec getPos() {
        return pos;
    }

    @Override
    public Vec getSize() {
        Vec tiles = tileSize();
        int columns = Math.min(widgets.size(), maxColumns);
        int rows = widgets.size() / columns;
        if (widgets.size() % columns != 0) rows++;

        return new Vec(
                tiles.x() * columns + (spacing * Math.max(0, columns - 1)),
                tiles.y() * rows + (spacing * Math.max(0, rows - 1)),
                0
        );
    }

    @Override
    public void update(InstanceContainer inst) {
        Vec tiles = tileSize();
        Vec origin = pos.sub(tiles.mul(4).apply(Vec.Operator.FLOOR).div(8));
        Vec current = origin;
        int column = 0;
        for (Widget w : widgets) {
            w.setPos(current.add(w.getSize().mul(4).apply(Vec.Operator.FLOOR).div(8)));
            w.update(inst);
            current = current.sub(tiles.x() + spacing, 0, 0);
            if (++column >= maxColumns) {
                current = current.sub(0, tiles.y() + spacing, 0).withX(origin.x());
                column = 0;
            }
        }
    }

    private Vec tileSize() {
        Vec size = Vec.ZERO;
        for (Widget w : widgets) {
            size = size.max(w.getSize());
        }
        return size;
    }

    @Override
    public void remove() {
        for (Widget w : widgets) {
            w.remove();
        }
    }

    @Override
    public boolean interact(Interaction i) {
        if (!inBounds(i.pos())) return false;
        for (Widget w : widgets) {
            if (w.interact(i)) return true;
        }
        return false;
    }

    @Override
    public List<Widget> getChildren() {
        return widgets;
    }
}
