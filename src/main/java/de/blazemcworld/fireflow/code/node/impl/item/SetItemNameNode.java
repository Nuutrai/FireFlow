package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.TextType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.kyori.adventure.text.Component;

public class SetItemNameNode extends Node {

    public SetItemNameNode() {
        super("set_item_name", Material.NAME_TAG);

        Input<ItemStack> item = new Input<>("item", ItemType.INSTANCE);
        Input<Component> name = new Input<>("name", TextType.INSTANCE);
        Output<ItemStack> updated = new Output<>("updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> item.getValue(ctx).withCustomName(name.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new SetItemNameNode();
    }
}