package de.blazemcworld.fireflow.code.node;

import de.blazemcworld.fireflow.code.type.AllTypes;
import de.blazemcworld.fireflow.code.type.WireType;
import de.blazemcworld.fireflow.util.Translations;
import net.minestom.server.item.Material;

import java.util.List;

public abstract class SingleGenericNode<T> extends Node {
    protected final WireType<T> type;

    protected SingleGenericNode(String id, Material icon, WireType<T> type) {
        super(id, icon);
        this.type = type;
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
    public String getTitle() {
        if (type == null) return Translations.get("node." + id + ".base_title");
        return Translations.get("node." + id + ".title", type.getName());
    }

    @Override
    public int getTypeCount() {
        return 1;
    }

    @Override
    public Node copyWithTypes(List<WireType<?>> types) {
        return copyWithType(types.getFirst());
    }

    public abstract Node copyWithType(WireType<?> type);
}
