package de.blazemcworld.fireflow.code.node.impl.info.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

public class PlayerIsFlyingNode extends Node {
    public PlayerIsFlyingNode() {
        super("player_is_flying", Material.FEATHER);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<Boolean> flying = new Output<>("flying", ConditionType.INSTANCE);

        flying.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::isFlying, false));
    }

    @Override
    public Node copy() {
        return new PlayerIsFlyingNode();
    }
}
