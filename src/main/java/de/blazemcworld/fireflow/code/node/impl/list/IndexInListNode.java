package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class IndexInListNode<T> extends SingleGenericNode<T> {

    public IndexInListNode(WireType<T> type) {
        super("index_in_list", Material.COMPASS, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<T> value = new Input<>("value", type);

        Output<Double> index = new Output<>("index", NumberType.INSTANCE);

        index.valueFrom((ctx) -> {
            ListValue<T> listValue = list.getValue(ctx);
            T search = value.getValue(ctx);
            for (int i = 0; i < listValue.size(); i++) {
                if (type.valuesEqual(listValue.get(i), search)) return (double) i;
            }
            return -1.0;
        });
    }

    @Override
    public Node copy() {
        return new IndexInListNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new IndexInListNode<>(type);
    }
}
