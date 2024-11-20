package de.blazemcworld.fireflow.code.node.impl;

import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class OnPlayerJoinNode extends Node {

    private final Output<Void> signal;
    private final Output<Player> player;

    public OnPlayerJoinNode() {
        super("on_player_join");

        signal = new Output<>("signal", SignalType.INSTANCE);
        player = new Output<>("player", PlayerType.INSTANCE);
        player.valueFromThread();
    }

    @Override
    public void init(CodeEvaluator evaluator) {
        evaluator.events.addListener(PlayerSpawnEvent.class, event -> {
            CodeThread thread = evaluator.newCodeThread();
            thread.setThreadValue(player, event.getPlayer());
            thread.sendSignal(signal);
            thread.clearQueue();
        });
    }

    @Override
    public Node copy() {
        return new OnPlayerJoinNode();
    }
}
