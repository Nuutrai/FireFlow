package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.minestom.server.item.Material;

public class CreateListNode<T> extends SingleGenericNode<T> {

    public CreateListNode(WireType<T> type) {
        super("create_list", Material.MINECART, type);

        Varargs<T> content = new Varargs<>("content", type);
        Output<ListValue<T>> output = new Output<>("list", ListType.of(type));
        output.valueFrom((ctx) -> {
            return new ListValue<>(type, content.getVarargs(ctx));
        });
    }

    @Override
    public Node copy() {
        return new CreateListNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new CreateListNode<>(type);
    }
}
