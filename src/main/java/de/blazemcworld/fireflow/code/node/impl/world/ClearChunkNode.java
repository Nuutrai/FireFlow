package de.blazemcworld.fireflow.code.node.impl.world;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.SignalType;
import net.minestom.server.instance.Chunk;
import net.minestom.server.item.Material;

public class ClearChunkNode extends Node {

    public ClearChunkNode() {
        super("clear_chunk", Material.STONE);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<Double> x = new Input<>("x", NumberType.INSTANCE);
        Input<Double> z = new Input<>("z", NumberType.INSTANCE);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {

            Chunk chunk = ctx.evaluator.space.play.getChunkAt(x.getValue(ctx), z.getValue(ctx));

            if (chunk != null) {
                chunk.reset();
            }

            ctx.sendSignal(next);

        });

    }

    @Override
    public Node copy() {
        return new ClearChunkNode();
    }
}
