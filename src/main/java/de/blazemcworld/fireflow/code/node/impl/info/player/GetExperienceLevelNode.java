package de.blazemcworld.fireflow.code.node.impl.info.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.item.Material;

public class GetExperienceLevelNode extends Node {
    public GetExperienceLevelNode() {
        super("get_experience_level", Material.ENCHANTING_TABLE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<Double> level = new Output<>("level", NumberType.INSTANCE);

        level.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> (double) p.getLevel(), 0.0));
    }

    @Override
    public Node copy() {
        return new GetExperienceLevelNode();
    }
}
