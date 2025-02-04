package de.blazemcworld.fireflow.code.widget;

import de.blazemcworld.fireflow.code.Interaction;
import de.blazemcworld.fireflow.util.TextWidth;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IconWidget implements Widget {

    private Vec pos = Vec.ZERO;
    private final double size;
    private final ItemWidget item;
    private final List<TextWidget> label;

    public IconWidget(ItemStack item, String label, double size) {
        this.size = size;
        this.item = new ItemWidget(item, size);
        this.label = wordWrap(label, (int) (size * 40));
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
        Vec out = new Vec(size, size, 0);
        for (TextWidget t : label) {
            Vec s = t.getSize();
            out = out.add(0, s.y(), 0).max(s.x(), 0, 0);
        }
        return out;
    }

    @Override
    public void update(InstanceContainer inst) {
        Vec iconSize = getSize();

        item.setPos(pos.sub(iconSize.x() * 0.5 - item.getSize().x() * 0.5, 0, 0));
        item.update(inst);

        double yShift = size;
        for (TextWidget t : label) {
            Vec textSize = t.getRawSize();
            t.setPos(pos.sub(iconSize.x() * 0.5 - textSize.x() * 0.5, yShift, 0));
            t.update(inst);
            yShift += textSize.y();
        }
    }

    @Override
    public void remove() {
        item.remove();
        for (TextWidget t : label) t.remove();
    }

    @Override
    public boolean interact(Interaction i) {
        return false;
    }

    @Override
    public List<Widget> getChildren() {
        ArrayList<Widget> out = new ArrayList<>(label);
        out.add(item);
        return out;
    }

    private static List<TextWidget> wordWrap(String label, int max) {
        String[] words = label.split(" ");
        List<TextWidget> out = new ArrayList<>();

        double width = 0;
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            double next = TextWidth.calculate(word, false);
            if (next + width > max && !current.isEmpty()) {
                out.add(new TextWidget(Component.text(current.toString())).stretch(0.5, 0.5));
                current = new StringBuilder();
                width = 0;
            }
            width += next;
            if (current.isEmpty()) current = new StringBuilder(word);
            else current.append(" ").append(word);
        }
        if (!current.isEmpty()) {
            out.add(new TextWidget(Component.text(current.toString())).stretch(0.5, 0.5));
        }

        return out;
    }
}
