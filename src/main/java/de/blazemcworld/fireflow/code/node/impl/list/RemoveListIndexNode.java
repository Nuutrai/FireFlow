package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class RemoveListIndexNode<T> extends SingleGenericNode<T> {

    public RemoveListIndexNode(WireType<T> type) {
        super("remove_list_index", Material.TNT, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<Double> index = new Input<>("index", NumberType.INSTANCE);

        Output<ListValue<T>> output = new Output<>("list", ListType.of(type));
        output.valueFrom((ctx) -> list.getValue(ctx).remove(index.getValue(ctx).intValue()));
    }

    @Override
    public Node copy() {
        return new RemoveListIndexNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new RemoveListIndexNode<>(type);
    }
}

