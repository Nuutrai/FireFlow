package de.blazemcworld.fireflow.inventory;

import de.blazemcworld.fireflow.code.type.TextType;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceInfo;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.Transfer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ActiveSpacesInventory {

    public static void open(Player player) {
        Inventory inv = new Inventory(InventoryType.CHEST_3_ROW, "Active Spaces");

        List<Space> spaces = SpaceManager.activeSpaces();

        spaces.sort(Comparator.comparingInt(s -> -s.play.getPlayers().size()));

        for (int i = 0; i < spaces.size(); i++) {
            SpaceInfo info = spaces.get(i).info;
            inv.setItemStack(i, ItemStack.builder(info.icon)
                    .customName(TextType.MM.deserialize(info.name).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                    .lore(
                            Component.text("Players: " + spaces.get(i).play.getPlayers().size()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                            Component.text("ID: " + info.id).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                    )
                    .build());
        }

        inv.addInventoryCondition((p, slot, type, res) -> {
            res.setCancel(true);
            if (p != player) return;

            if (slot < spaces.size()) {
                Transfer.move(player, SpaceManager.getOrLoadSpace(spaces.get(slot).info).play);
            }
        });

        player.openInventory(inv);
    }

}

