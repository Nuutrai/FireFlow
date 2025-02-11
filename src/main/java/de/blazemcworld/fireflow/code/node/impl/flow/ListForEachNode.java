package de.blazemcworld.fireflow.code.node.impl.flow;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class ListForEachNode<T> extends SingleGenericNode<T> {

    public ListForEachNode(WireType<T> type) {
        super("list_for_each", Material.HOPPER, type);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        
        Output<Void> each = new Output<>("each", SignalType.INSTANCE);
        Output<T> value = new Output<>("value", type);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        value.valueFromScope();

        signal.onSignal((ctx) -> {
            int[] index = new int[] { 0 };
            ListValue<T> listValue = list.getValue(ctx);

            Runnable[] step = { null };
            step[0] = () -> {
                if (index[0] >= listValue.size()) {
                    ctx.sendSignal(next);
                    return;
                }
                ctx.setScopeValue(value, listValue.get(index[0]++));
                ctx.submit(step[0]);
                ctx.sendSignal(each);
            };

            step[0].run();
        });
    }

    @Override
    public Node copy() {
        return new ListForEachNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new ListForEachNode<>(type);
    }
}
