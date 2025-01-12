package de.blazemcworld.fireflow.command;

import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.entity.Player;

public class AddNodeCommand extends Command {
    public AddNodeCommand() {
        super("add?", "add");

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text(Translations.get("error.needs.player")).color(NamedTextColor.RED));
                return;
            }

            Space space = SpaceManager.getSpaceForPlayer(player);
            if (space == null) {
                sender.sendMessage(Component.text(Translations.get("error.needs.space")).color(NamedTextColor.RED));
                return;
            }

            if (space.code != player.getInstance()) {
                sender.sendMessage(Component.text(Translations.get("error.mode.code")).color(NamedTextColor.RED));
                return;
            }

            space.editor.addNode(player, ctx.get("query"), ctx.getCommandName().equals("add?"));
        }, new ArgumentString("query"));
    }
}
