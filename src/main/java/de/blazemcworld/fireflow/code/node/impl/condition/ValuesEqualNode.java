package de.blazemcworld.fireflow.code.node.impl.condition;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.AllTypes;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.util.Translations;
import net.minestom.server.item.Material;

import java.util.List;

public class ValuesEqualNode<T> extends Node {

    private final WireType<T> type;

    public ValuesEqualNode(WireType<T> type) {
        super("values_equal", Material.COMPARATOR);
        this.type = type;

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
    public String getTitle() {
        if (type == null) return Translations.get("node.values_equal.base_title");
        return Translations.get("node.values_equal.title", type.getName());
    }

    @Override
    public Node copy() {
        return new ValuesEqualNode<>(type);
    }

    @Override
    public int getTypeCount() {
        return 1;
    }

    @Override
    public List<WireType<?>> getTypes() {
        return List.of(type);
    }

    @Override
    public boolean acceptsType(WireType<?> type, int index) {
        return AllTypes.isValue(type);
    }

    @Override
    public Node copyWithTypes(List<WireType<?>> types) {
        return new ValuesEqualNode<>(types.getFirst());
    }

}
