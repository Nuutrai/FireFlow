package de.blazemcworld.fireflow.code.node.impl.event.player;

import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.VectorType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;

public class OnPlayerClickBlockNode extends Node {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Vec> position;

    public OnPlayerClickBlockNode() {
        super("on_player_click_block", Material.IRON_PICKAXE);

        signal = new Output<>("signal", SignalType.INSTANCE);
        player = new Output<>("player", PlayerType.INSTANCE);
        position = new Output<>("position", VectorType.INSTANCE);
        player.valueFromScope();
        position.valueFromScope();
    }

    @Override
    public void init(CodeEvaluator evaluator) {
        evaluator.events.addListener(PlayerBlockInteractEvent.class, event -> {
            if (event.getHand() == PlayerHand.OFF) return;
            CodeThread thread = evaluator.newCodeThread(event);
            thread.setScopeValue(player, new PlayerValue(event.getPlayer()));
            thread.setScopeValue(position, event.getBlockPosition().asVec());
            thread.sendSignal(signal);
            thread.clearQueue();
        });
    }

    @Override
    public Node copy() {
        return new OnPlayerClickBlockNode();
    }
}
