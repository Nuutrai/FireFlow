package de.blazemcworld.fireflow.code.node.impl.player.info;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ItemType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class PlayerMainItemNode extends Node {
    public PlayerMainItemNode() {
        super("player_main_item", Material.IRON_SHOVEL);

        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<ItemStack> item = new Output<>("item", ItemType.INSTANCE);

        item.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::getItemInMainHand, ItemStack.AIR));
    }

    @Override
    public Node copy() {
        return new PlayerMainItemNode();
    }
}