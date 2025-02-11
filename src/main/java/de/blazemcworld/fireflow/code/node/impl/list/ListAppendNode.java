package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class ListAppendNode<T> extends SingleGenericNode<T> {

    public ListAppendNode(WireType<T> type) {
        super("list_append", Material.DISPENSER, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Varargs<T> value = new Varargs<>("value", type);

        Output<ListValue<T>> output = new Output<>("list", ListType.of(type));
        output.valueFrom((ctx) -> {
            ListValue<T> listValue = list.getValue(ctx);
            return listValue.add(value.getVarargs(ctx));
        });
    }

    @Override
    public Node copy() {
        return new ListAppendNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new ListAppendNode<>(type);
    }
}
