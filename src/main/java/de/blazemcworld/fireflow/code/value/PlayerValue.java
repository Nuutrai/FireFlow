package de.blazemcworld.fireflow.code.value;

import de.blazemcworld.fireflow.code.CodeThread;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerValue {
    
    public final UUID uuid;

    public PlayerValue(Player player) {
        uuid = player.getUuid();
    }

    public PlayerValue(UUID uuid) {
        this.uuid = uuid;
    }

    public void use(Instance inst, Consumer<Player> cb) {
        Player p = inst.getPlayerByUuid(uuid);
        if (p != null) {
            synchronized (p) {
                if (p.getInstance() == inst) {
                    cb.accept(p);
                    return;
                }
            }
        }
        cb.accept(null);
    }

    public <T> T apply(Instance inst, Function<Player, T> fn) {
        Player p = inst.getPlayerByUuid(uuid);
        if (p != null) {
            synchronized (p) {
                if (p.getInstance() == inst) {
                    return fn.apply(p);
                }
            }
        }
        return fn.apply(null);
    }

    public void tryUse(Instance inst, Consumer<Player> cb) {
        use(inst, (p) -> {
            if (p == null) return;
            cb.accept(p);
        });
    }

    public <T> T tryGet(Instance inst, Function<Player, T> fn, T fallback) {
        return apply(inst, p -> {
            if (p == null) return fallback;
            return fn.apply(p);
        });
    }

    public <T> T tryGet(CodeThread ctx, Function<Player, T> fn, T fallback) {
        return tryGet(ctx.evaluator.space.play, fn, fallback);
    }

    public void tryUse(CodeThread ctx, Consumer<Player> cb) {
        tryUse(ctx.evaluator.space.play, cb);
    }
}
