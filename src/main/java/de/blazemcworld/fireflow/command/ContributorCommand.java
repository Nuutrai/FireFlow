package de.blazemcworld.fireflow.command;

import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import de.blazemcworld.fireflow.util.Transfer;
import de.blazemcworld.fireflow.util.Translations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.mojang.MojangUtils;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ContributorCommand extends Command {

    private final Function<Space, Set<UUID>> fn;

    public ContributorCommand(String id, Function<Space, Set<UUID>> fn) {
        super(id);
        this.fn = fn;

        setDefaultExecutor((sender, ctx) -> {
            run(sender, "list", null);
        });

        addSyntax((sender, ctx) -> {
            run(sender, "list", null);
        }, new ArgumentLiteral("list"));

        addSyntax((sender, ctx) -> {
            run(sender, "add", ctx.get("name"));
        }, new ArgumentLiteral("add"), new ArgumentString("name"));

        addSyntax((sender, ctx) -> {
            run(sender, "remove", ctx.get("name"));
        }, new ArgumentLiteral("remove"), new ArgumentString("name"));
    }

    private void run(CommandSender sender, String action, String other) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(Translations.get("error.needs.player")).color(NamedTextColor.RED));
            return;
        }
        Space space = SpaceManager.getSpaceForPlayer(player);
        if (space == null) {
            sender.sendMessage(Component.text(Translations.get("error.needs.space")).color(NamedTextColor.RED));
            return;
        }

        if (!space.info.owner.equals(player.getUuid())) {
            sender.sendMessage(Component.text(Translations.get("error.needs.owner")).color(NamedTextColor.RED));
            return;
        }

        new Thread(() -> {
            Set<UUID> contributors = fn.apply(space);
            if (action.equals("list")) {
                if (contributors.isEmpty()) {
                    sender.sendMessage(Component.text(Translations.get("error.empty.contributors")).color(NamedTextColor.GRAY));
                    return;
                }
                sender.sendMessage(Component.text(Translations.get("success.contributors.list")).color(NamedTextColor.YELLOW));
                for (UUID contributor : contributors) {
                    String name = "Internal Error";
                    try {
                        name = MojangUtils.getUsername(contributor);
                    } catch (IOException ignored) {
                    }
                    sender.sendMessage(Component.text(name).color(NamedTextColor.GOLD)
                        .append(Component.text(" (" + contributor + ")").color(NamedTextColor.GRAY))
                    );
                }
                return;
            }

            if (action.equals("add")) {
                UUID contributor = null;
                try {
                    contributor = MojangUtils.getUUID(other);
                } catch (IOException ignored) {
                }
                if (contributor == null) {
                    sender.sendMessage(Component.text(Translations.get("error.invalid.player")).color(NamedTextColor.RED));
                    return;
                }
                if (contributors.contains(contributor)) {
                    sender.sendMessage(Component.text(Translations.get("error.already.contributor")).color(NamedTextColor.RED));
                    return;
                }
                contributors.add(contributor);
                sender.sendMessage(Component.text(Translations.get("success.contributors.added")).color(NamedTextColor.GREEN));
                return;
            }

            if (action.equals("remove")) {
                UUID contributor = null;
                try {
                    contributor = MojangUtils.getUUID(other);
                } catch (IOException ignored) {
                }
                if (contributor == null) {
                    sender.sendMessage(Component.text(Translations.get("error.invalid.player")).color(NamedTextColor.RED));
                    return;
                }
                if (!contributors.contains(contributor)) {
                    sender.sendMessage(Component.text(Translations.get("error.not.contributor")).color(NamedTextColor.RED));
                    return;
                }
                contributors.remove(contributor);
                if (space.code.getPlayerByUuid(contributor) != null) {
                    Transfer.move(space.code.getPlayerByUuid(contributor), space.play);
                }
                sender.sendMessage(Component.text(Translations.get("success.contributors.removed")).color(NamedTextColor.GREEN));
                return;
            }
        }).start();
    }
}
