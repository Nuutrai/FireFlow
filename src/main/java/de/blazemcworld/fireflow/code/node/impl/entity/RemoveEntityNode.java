package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;

public class RemoveEntityNode extends Node {

    public RemoveEntityNode() {
        super("remove_entity", Material.TNT_MINECART);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<EntityValue> entity = new Input<>("entity", EntityType.INSTANCE);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            entity.getValue(ctx).use(ctx, Entity::remove);
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new RemoveEntityNode();
    }
}
