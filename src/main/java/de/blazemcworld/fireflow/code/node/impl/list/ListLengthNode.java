package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class ListLengthNode<T> extends SingleGenericNode<T> {

    public ListLengthNode(WireType<T> type) {
        super("list_length", Material.KNOWLEDGE_BOOK, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Output<Double> length = new Output<>("length", NumberType.INSTANCE);

        length.valueFrom((ctx) -> (double) list.getValue(ctx).size());
    }

    @Override
    public Node copy() {
        return new ListLengthNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new ListLengthNode<>(type);
    }
}

