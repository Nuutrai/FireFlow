package de.blazemcworld.fireflow.code.node.impl.player.effect;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.*;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

public class SendParticleNode extends Node {

    public SendParticleNode() {
        super("send_particle", Material.GLOWSTONE_DUST);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Input<Vec> position = new Input<>("position", VectorType.INSTANCE);
        Input<Particle> particle = new Input<>("particle", ParticleType.INSTANCE);
        Input<Double> count = new Input<>("count", NumberType.INSTANCE);

        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> p.sendPacket(new ParticlePacket(
                    particle.getValue(ctx), position.getValue(ctx), Vec.ZERO,
                    0, Math.min(count.getValue(ctx).intValue(), 256)
            )));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SendParticleNode();
    }
}
