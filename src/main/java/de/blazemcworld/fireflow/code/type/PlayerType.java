package de.blazemcworld.fireflow.code.type;

import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class PlayerType extends WireType<PlayerValue> {

    public static final PlayerType INSTANCE = new PlayerType();

    private PlayerType() {
    }

    @Override
    public String id() {
        return "player";
    }

    @Override
    public PlayerValue defaultValue() {
        return new PlayerValue(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @Override
    public TextColor getColor() {
        return NamedTextColor.GOLD;
    }

    @Override
    public PlayerValue convert(Object obj) {
        if (obj instanceof PlayerValue player) return player;
        return null;
    }

    @Override
    public JsonElement toJson(PlayerValue obj) {
        return new JsonPrimitive(obj.uuid.toString());
    }

    @Override
    public PlayerValue fromJson(JsonElement json) {
        return new PlayerValue(UUID.fromString(json.getAsString()));
    }
}
