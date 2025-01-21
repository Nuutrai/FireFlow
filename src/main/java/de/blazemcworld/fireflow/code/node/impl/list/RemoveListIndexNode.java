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

public class RemoveListIndexNode<T> extends Node {
    
    private final WireType<T> type;

    public RemoveListIndexNode(WireType<T> type) {
        super("remove_list_index", Material.TNT);
        this.type = type;

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<Double> index = new Input<>("index", NumberType.INSTANCE);

        Output<ListValue<T>> output = new Output<>("list", ListType.of(type));
        output.valueFrom((ctx) -> list.getValue(ctx).remove(index.getValue(ctx).intValue()));
    }

    @Override
    public String getTitle() {
        if (type == null) return Translations.get("node.remove_list_index.base_title");
        return Translations.get("node.remove_list_index.title", type.getName());
    }

    @Override
    public Node copy() {
        return new RemoveListIndexNode<>(type);
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
        return new RemoveListIndexNode<>(types.getFirst());
    }
}

