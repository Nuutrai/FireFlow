package de.blazemcworld.fireflow.code.widget;

import de.blazemcworld.fireflow.util.TextWidth;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.InstanceContainer;

public class LineElement {

    public Vec from = Vec.ZERO;
    public Vec to = Vec.ZERO;
    private final Entity display = new Entity(EntityType.TEXT_DISPLAY);
    private final TextDisplayMeta meta = (TextDisplayMeta) display.getEntityMeta();
    public String pattern = null;

    public LineElement() {
        meta.setText(Component.text("-"));
        meta.setBackgroundColor(0);
        meta.setLineWidth(Integer.MAX_VALUE);
        meta.setTransformationInterpolationDuration(1);
        meta.setPosRotInterpolationDuration(1);
        meta.setHasNoGravity(true);
    }

    public void update(InstanceContainer inst) {
        double dist = from.withZ(0).distance(to.withZ(0));
        double xScale = dist * 8;
        if (pattern != null) {
            double single = TextWidth.calculate(pattern, false) / 4.0;
            int repeat = xScale < single ? 1 : (int) (xScale / single);
            meta.setText(Component.text(pattern.repeat(repeat)).color(meta.getText().color()));
            xScale /= repeat * 1.6;
            dist /= repeat;
        }
        meta.setScale(new Vec(xScale, 1, 1));
        float angle = (float) Math.atan2(to.y() - from.y(), from.x() - to.x());
        meta.setLeftRotation(new float[]{0, 0, (float) Math.sin(angle * 0.5), (float) Math.cos(angle * 0.5)});
        Vec v = Vec.fromPoint(from).add(to).mul(0.5).add(
                Math.cos(angle) * dist * 0.1 - Math.sin(angle) * 0.135,
                -Math.sin(angle) * dist * 0.1 - Math.cos(angle) * 0.135,
                0
        );
        display.setInstance(inst, v.withZ(15.999).asPosition().withView(180f, 0f));
    }

    public void remove() {
        display.remove();
    }

    public void color(TextColor color) {
        meta.setText(Component.text("-").color(color));
    }

    public TextColor color() {
        return meta.getText().color();
    }
}
