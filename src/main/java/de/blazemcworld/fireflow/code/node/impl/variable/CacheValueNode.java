package de.blazemcworld.fireflow.code.node.impl.variable;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.minestom.server.item.Material;

public class CacheValueNode<T> extends SingleGenericNode<T> {

    public CacheValueNode(WireType<T> type) {
        super("cache_value", Material.KNOWLEDGE_BOOK, type);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<T> store = new Input<>("store", type);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);
        Output<T> cache = new Output<>("cache", type);

        cache.valueFromScope();
        signal.onSignal((ctx) -> {
            ctx.setScopeValue(cache, store.getValue(ctx));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new CacheValueNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new CacheValueNode<>(type);
    }
}
