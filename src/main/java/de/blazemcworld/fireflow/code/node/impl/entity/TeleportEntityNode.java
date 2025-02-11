package de.blazemcworld.fireflow.code.node.impl.entity;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.EntityType;
import de.blazemcworld.fireflow.code.type.PositionType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.EntityValue;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;

public class TeleportEntityNode extends Node {

    public TeleportEntityNode() {
        super("teleport_entity", Material.ENDER_PEARL);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<EntityValue> entity = new Input<>("entity", EntityType.INSTANCE);
        Input<Pos> position = new Input<>("position", PositionType.INSTANCE);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            entity.getValue(ctx).use(ctx, e -> e.teleport(position.getValue(ctx)));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new TeleportEntityNode();
    }
}