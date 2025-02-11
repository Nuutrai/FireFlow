package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class SetListIndexNode<T> extends SingleGenericNode<T> {

    public SetListIndexNode(WireType<T> type) {
        super("set_list_index", Material.BUNDLE, type);

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<Double> index = new Input<>("index", NumberType.INSTANCE);
        Input<T> value = new Input<>("value", type);

        Output<ListValue<T>> updated = new Output<>("updated", ListType.of(type));

        updated.valueFrom((ctx) -> {
            ListValue<T> listValue = list.getValue(ctx);
            return listValue.set(index.getValue(ctx).intValue(), value.getValue(ctx));
        });
    }

    @Override
    public Node copy() {
        return new SetListIndexNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new SetListIndexNode<>(type);
    }
}
