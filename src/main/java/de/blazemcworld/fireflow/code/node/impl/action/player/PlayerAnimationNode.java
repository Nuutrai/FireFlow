package de.blazemcworld.fireflow.code.node.impl.action.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;

public class PlayerAnimationNode extends Node {

    public PlayerAnimationNode() {
        super("player_animation", Material.GLOWSTONE_DUST);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Input<String> animation = new Input<>("animation", StringType.INSTANCE)
                .options("damage", "critical", "magic_critical", "main_hand", "off_hand", "wake_up");
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> {
                EntityAnimationPacket.Animation anim = switch (animation.getValue(ctx)) {
                    case "damage" -> EntityAnimationPacket.Animation.TAKE_DAMAGE;
                    case "critical" -> EntityAnimationPacket.Animation.CRITICAL_EFFECT;
                    case "magic_critical" -> EntityAnimationPacket.Animation.MAGICAL_CRITICAL_EFFECT;
                    case "main_hand" -> EntityAnimationPacket.Animation.SWING_MAIN_ARM;
                    case "off_hand" -> EntityAnimationPacket.Animation.SWING_OFF_HAND;
                    case "wake_up" -> EntityAnimationPacket.Animation.LEAVE_BED;
                    default -> null;
                };

                if (anim == null) return;
                p.sendPacketToViewersAndSelf(new EntityAnimationPacket(p.getEntityId(), anim));
            });
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new PlayerAnimationNode();
    }
}
