package de.blazemcworld.fireflow.code.node.impl.list;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.AllTypes;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.util.Translations;
import net.minestom.server.item.Material;

import java.util.List;

public class ListLengthNode<T> extends Node {
    
    private final WireType<?> type;

    public ListLengthNode(WireType<T> type) {
        super("list_length", Material.KNOWLEDGE_BOOK);
        this.type = type;

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Output<Double> length = new Output<>("length", NumberType.INSTANCE);

        length.valueFrom((ctx) -> (double) list.getValue(ctx).size());
    }

    @Override
    public String getTitle() {
        if (type == null) return Translations.get("node.list_length.base_title");
        return Translations.get("node.list_length.title", type.getName());
    }

    @Override
    public Node copy() {
        return new ListLengthNode<>(type);
    }

    @Override
    public boolean acceptsType(WireType<?> type, int index) {
        return AllTypes.isValue(type);
    }

    @Override
    public List<WireType<?>> getTypes() {
        return List.of(type);
    }

    @Override
    public int getTypeCount() {
        return 1;
    }

    @Override
    public Node copyWithTypes(List<WireType<?>> types) {
        return new ListLengthNode<>(types.getFirst());

    }

}

