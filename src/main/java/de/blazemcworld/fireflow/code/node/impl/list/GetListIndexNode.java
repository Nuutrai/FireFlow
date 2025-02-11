package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class GetListIndexNode<T> extends SingleGenericNode<T> {

    public GetListIndexNode(WireType<T> type) {
        super("get_list_index", Material.HOPPER, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<Double> index = new Input<>("index", NumberType.INSTANCE);

        Output<T> output = new Output<>("value", type);
        output.valueFrom((ctx) -> list.getValue(ctx).get(index.getValue(ctx).intValue()));
    }

    @Override
    public Node copy() {
        return new GetListIndexNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new GetListIndexNode<>(type);
    }
}

