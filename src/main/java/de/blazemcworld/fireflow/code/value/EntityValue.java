package de.blazemcworld.fireflow.code.value;

import de.blazemcworld.fireflow.code.CodeThread;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntityValue {

    public final UUID uuid;
    private Entity cache = null;

    public EntityValue(Entity entity) {
        if (entity == null || entity instanceof Player) {
            uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
            return;
        }
        uuid = entity.getUuid();
    }

    public EntityValue(UUID uuid) {
        this.uuid = uuid;
    }

    public Entity resolve(Instance inst) {
        if (cache != null && cache.getInstance() == inst) return cache;
        Entity e = inst.getEntityByUuid(uuid);
        if (e instanceof Player) return null;
        cache = e;
        return e;
    }

    private Entity resolve(CodeThread ctx) {
        return resolve(ctx.evaluator.space.play);
    }

    public <T> T apply(Instance inst, Function<Entity, T> fn, T fallback) {
        Entity e = resolve(inst);
        return e == null ? fallback : fn.apply(e);
    }

    public <T> T apply(CodeThread ctx, Function<Entity, T> fn, T fallback) {
        return apply(ctx.evaluator.space.play, fn, fallback);
    }

    public void use(Instance inst, Consumer<Entity> cb) {
        Entity e = resolve(inst);
        if (e != null) cb.accept(e);
    }

    public void use(CodeThread ctx, Consumer<Entity> cb) {
        use(ctx.evaluator.space.play, cb);
    }
}
