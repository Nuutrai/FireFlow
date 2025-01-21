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

public class GetListIndexNode<T> extends Node {

    private final WireType<T> type;

    public GetListIndexNode(WireType<T> type) {
        super("get_list_index", Material.TNT);
        this.type = type;

        Input<ListValue<T>> list = new Input<>("list", ListType.of(type));
        Input<Double> index = new Input<>("index", NumberType.INSTANCE);

        Output<T> output = new Output<>("value", type);
        output.valueFrom((ctx) -> list.getValue(ctx).get(index.getValue(ctx).intValue()));
    }

    @Override
    public String getTitle() {
        if (type == null) return Translations.get("node.get_list_index.base_title");
        return Translations.get("node.get_list_index.title", type.getName());
    }

    @Override
    public Node copy() {
        return new GetListIndexNode<>(type);
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
        return new GetListIndexNode<>(types.getFirst());
    }
}

