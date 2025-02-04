package de.blazemcworld.fireflow.code.node.impl.item;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.TextType;
import de.blazemcworld.fireflow.code.value.ListValue;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class SetItemLoreNode extends Node {

    public SetItemLoreNode() {
        super("set_item_lore", Material.WRITABLE_BOOK);

        Input<ItemStack> item = new Input<>("item", ItemType.INSTANCE);
        Input<ListValue<Component>> lore = new Input<>("lore", ListType.of(TextType.INSTANCE));
        Output<ItemStack> updated = new Output<>("updated", ItemType.INSTANCE);

        updated.valueFrom((ctx) -> item.getValue(ctx).withLore(lore.getValue(ctx).view()));
    }

    @Override
    public Node copy() {
        return new SetItemLoreNode();
    }
}