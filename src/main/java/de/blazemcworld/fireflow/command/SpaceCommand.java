package de.blazemcworld.fireflow.command;

import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class SpaceCommand extends Command {
    
    public SpaceCommand() {
        super("space");

        addSubcommand(new MonitorCommand());
        addSubcommand(new ContributorCommand("developer", space -> space.info.developers));
        addSubcommand(new ContributorCommand("builder", space -> space.info.builders));
        addSubcommand(new VariablesCommand());
        addSubcommand(new ClearCommand());
        
        addSyntax((sender, ctx) -> {
            Space space = getSpace(sender);
            if (space == null) return;
            
            ItemStack item = ctx.get("item");
            if (item == null || item.isAir()) {
                sender.sendMessage(Component.text(Translations.get("error.invalid.item")).color(NamedTextColor.RED));
                return;
            }

            space.info.icon = item.material();
            sender.sendMessage(Component.text(Translations.get("success.changed_icon")).color(NamedTextColor.GREEN));
        }, new ArgumentLiteral("icon"), new ArgumentItemStack("item"));
        addSyntax((sender, ctx) -> {
            Space space = getSpace(sender);
            if (space == null) return;
            
            String name = String.join(" ", ctx.<String[]>get("text"));
            if (name.length() > 64) {
                sender.sendMessage(Component.text(Translations.get("error.text.too_long")).color(NamedTextColor.RED));
                return;
            }

            if (name.isEmpty()) {
                sender.sendMessage(Component.text(Translations.get("error.invalid.text")).color(NamedTextColor.RED));
                return;
            }

            space.info.name = name;
            sender.sendMessage(Component.text(Translations.get("success.changed_name")).color(NamedTextColor.GREEN));
        }, new ArgumentLiteral("name"), new ArgumentStringArray("text"));

        addSyntax((sender, ctx) -> {
            Space space = getSpace(sender);
            if (space == null) return;

            if (!space.potentiallyBroken) {
                sender.sendMessage(Component.text(Translations.get("error.space.not_broken")).color(NamedTextColor.RED));
                return;
            }

            space.potentiallyBroken = false;
            sender.sendMessage(Component.text(Translations.get("success.space.marked_stable")).color(NamedTextColor.GREEN));
        }, new ArgumentLiteral("mark_stable"));

        setDefaultExecutor((sender, ctx) -> {
            sender.sendMessage(Component.text(Translations.get("error.needs.subcommand")).color(NamedTextColor.RED));
        });
    }

    private Space getSpace(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(Translations.get("error.needs.player")).color(NamedTextColor.RED));
            return null;
        }
        Space space = SpaceManager.getSpaceForPlayer(player);
        if (space == null) {
            sender.sendMessage(Component.text(Translations.get("error.needs.space")).color(NamedTextColor.RED));
            return null;
        }

        if (!space.info.owner.equals(player.getUuid())) {
            sender.sendMessage(Component.text(Translations.get("error.needs.owner")).color(NamedTextColor.RED));
            return null;
        }

        return space;
    }

}
