package de.blazemcworld.fireflow.util;

import de.blazemcworld.fireflow.space.Lobby;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.space.SpaceManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Chunk;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.jetbrains.annotations.NotNull;

public class CustomPlayer extends Player {
    private Space space;
    private boolean serverRegistered = false;
    private long lastTick = System.currentTimeMillis();

    public CustomPlayer(@NotNull PlayerConnection playerConnection, @NotNull GameProfile gameProfile) {
        super(playerConnection, gameProfile);
    }

    @Override
    public void tick(long time) {
        lastTick = time;
        super.interpretPacketQueue();
        super.tick(time);
    }

    @Override
    protected void refreshCurrentChunk(Chunk currentChunk) {
        this.currentChunk = currentChunk;
        synchronized (this) {
            if (space == null || (space.play != instance && space.code != instance && space.build != instance)) {
                space = SpaceManager.getSpaceForPlayer(this);
            }
        }
        boolean shouldBeServerRegistered = space == null || space.play != instance;
        if (shouldBeServerRegistered != serverRegistered) {
            if (shouldBeServerRegistered) {
                MinecraftServer.process().dispatcher().updateElement(this, currentChunk);
            } else {
                MinecraftServer.process().dispatcher().removeElement(this);
            }
            serverRegistered = shouldBeServerRegistered;
        }
    }

    public void serverTick(long time) {
        synchronized (this) {
            if (space != null && space.play == instance && lastTick + 5000 < time) {
                sendMessage(Component.text(Translations.get("info.space.timed_out")).color(NamedTextColor.GOLD));
                if (space.isOwnerOrDeveloper(this)) {
                    Transfer.move(this, space.code);
                } else {
                    Transfer.move(this, Lobby.instance);
                }
            }
        }
    }

    @Override
    public void interpretPacketQueue() {
        // Do nothing
    }
}
