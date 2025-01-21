package de.blazemcworld.fireflow.code.node.impl.event.player;

import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.Material;

public class OnPlayerAttackPlayerNode extends Node {

    private final Output<Void> signal;
    private final Output<PlayerValue> source;
    private final Output<PlayerValue> target;

    public OnPlayerAttackPlayerNode() {
        super("on_player_attack_player", Material.IRON_SWORD);

        signal = new Output<>("signal", SignalType.INSTANCE);
        source = new Output<>("source", PlayerType.INSTANCE);
        target = new Output<>("target", PlayerType.INSTANCE);

        source.valueFromThread();
        target.valueFromThread();
    }

    @Override
    public void init(CodeEvaluator evaluator) {
        evaluator.events.addListener(EntityAttackEvent.class, event -> {
            if (!(event.getEntity() instanceof Player s)) return;
            if (!(event.getTarget() instanceof Player t)) return;

            CodeThread thread = evaluator.newCodeThread(event);
            thread.setThreadValue(source, new PlayerValue(s));
            thread.setThreadValue(target, new PlayerValue(t));
            thread.sendSignal(signal);
            thread.clearQueue();
        });
    }

    @Override
    public Node copy() {
        return new OnPlayerAttackPlayerNode();
    }

}
