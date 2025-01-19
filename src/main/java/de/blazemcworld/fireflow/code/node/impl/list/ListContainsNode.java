package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.AllTypes;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.util.Translations;
import net.minestom.server.item.Material;

import java.util.HashSet;
import java.util.List;

public class ListContainsNode<T> extends Node {

    private final WireType<T> type;

    public ListContainsNode(WireType<T> type) {
        super("list_contains", Material.ARROW);
        this.type = type;

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Varargs<T> check = new Varargs<>("check", type);
        Output<Boolean> contains = new Output<>("contains", ConditionType.INSTANCE);

        contains.valueFrom((ctx) -> {
            ListValue<T> v = list.getValue(ctx);
            HashSet<T> missing = new HashSet<>(check.getVarargs(ctx));
            for (int i = 0; i < v.size(); i++) {
                missing.remove(v.get(i));
            }
            return missing.isEmpty();
        });
    }

    @Override
    public String getTitle() {
        if (type == null) return Translations.get("node.list_contains.base_title");
        return Translations.get("node.list_contains.title", type.getName());
    }

    @Override
    public Node copy() {
        return new ListContainsNode<>(type);
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
        return new ListContainsNode<>(types.getFirst());
    }

}

