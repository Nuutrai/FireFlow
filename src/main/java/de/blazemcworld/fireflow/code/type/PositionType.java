package de.blazemcworld.fireflow.code.type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.item.Material;

public class PositionType extends WireType<Pos> {

    public static final PositionType INSTANCE = new PositionType();

    private PositionType() {
        super("position", NamedTextColor.DARK_PURPLE, Material.COMPASS);
    }

    @Override
    public Pos defaultValue() {
        return Pos.ZERO;
    }

    @Override
    public Pos checkType(Object obj) {
        if (obj instanceof Pos p) return p;
        return null;
    }

    @Override
    public JsonElement toJson(Pos pos) {
        JsonObject out = new JsonObject();
        out.addProperty("x", pos.x());
        out.addProperty("y", pos.y());
        out.addProperty("z", pos.z());
        out.addProperty("pitch", pos.pitch());
        out.addProperty("yaw", pos.yaw());
        return out;
    }

    @Override
    public Pos fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        return new Pos(
                obj.get("x").getAsDouble(),
                obj.get("y").getAsDouble(),
                obj.get("z").getAsDouble(),
                obj.get("pitch").getAsFloat(),
                obj.get("yaw").getAsFloat()
        );
    }

    @Override
    public boolean valuesEqual(Pos a, Pos b) {
        return a.equals(b);
    }

    @Override
    protected String stringifyInternal(Pos value) {
        return "(%.2f, %.2f, %.2f, %.2f, %.2f)".formatted(
                value.x(),
                value.y(),
                value.z(),
                value.pitch(),
                value.yaw()
        );
    }

    @Override
    protected boolean canConvertInternal(WireType<?> other) {
        return other == VectorType.INSTANCE;
    }

    @Override
    protected Pos convertInternal(WireType<?> other, Object v) {
        if (other == VectorType.INSTANCE && v instanceof Vec vec) {
            return vec.asPosition();
        }
        return super.convertInternal(other, v);
    }
}
