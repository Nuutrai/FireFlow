package de.blazemcworld.fireflow.code.node.impl.action;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.item.Material;

public class UnCancelEventNode extends Node {

    public UnCancelEventNode() {
        super("uncancel_event", Material.BARRIER);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);
        signal.onSignal((ctx) -> {
            Event event = ctx.event;
            if (event instanceof CancellableEvent e) {
                e.setCancelled(false);
            }
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new UnCancelEventNode();
    }
}
