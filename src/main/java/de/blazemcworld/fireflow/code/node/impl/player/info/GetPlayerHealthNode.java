package de.blazemcworld.fireflow.code.node.impl.player.info;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.item.Material;

public class GetPlayerHealthNode extends Node {
    public GetPlayerHealthNode() {
        super("get_player_health", Material.RED_DYE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<Double> health = new Output<>("health", NumberType.INSTANCE);

        health.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> (double) p.getHealth(), 0.0));
    }

    @Override
    public Node copy() {
        return new GetPlayerHealthNode();
    }
}
