package de.blazemcworld.fireflow.code.node.impl.condition;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.minestom.server.item.Material;

public class ValuesEqualNode<T> extends SingleGenericNode<T> {

    public ValuesEqualNode(WireType<T> type) {
        super("values_equal", Material.COMPARATOR, type);

        Input<T> primary = new Input<>("primary", type);
        Varargs<T> others = new Varargs<>("others", type);
        Output<Boolean> equal = new Output<>("equal", ConditionType.INSTANCE);

        equal.valueFrom((ctx) -> {
            T v = primary.getValue(ctx);
            for (T other : others.getVarargs(ctx)) {
                if (!type.valuesEqual(v, other)) return false;
            }
            return true;
        });
    }

    @Override
    public Node copy() {
        return new ValuesEqualNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new ValuesEqualNode<>(type);
    }
}
