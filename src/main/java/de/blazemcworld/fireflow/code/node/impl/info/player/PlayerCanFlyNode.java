package de.blazemcworld.fireflow.code.node.impl.info.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

public class PlayerCanFlyNode extends Node {
    public PlayerCanFlyNode() {
        super("player_can_fly", Material.WHITE_WOOL);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<Boolean> allowed = new Output<>("allowed", ConditionType.INSTANCE);

        allowed.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::isAllowFlying, false));
    }

    @Override
    public Node copy() {
        return new PlayerCanFlyNode();
    }
}
