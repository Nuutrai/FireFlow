package de.blazemcworld.fireflow.code.node.impl.player.info;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.item.Material;

public class GetHeldSlotNode extends Node {
    public GetHeldSlotNode() {
        super("get_held_slot", Material.SMOOTH_STONE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<Double> slot = new Output<>("slot", NumberType.INSTANCE);

        slot.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, p -> (double) p.getHeldSlot(), 0.0));
    }

    @Override
    public Node copy() {
        return new GetHeldSlotNode();
    }
}
