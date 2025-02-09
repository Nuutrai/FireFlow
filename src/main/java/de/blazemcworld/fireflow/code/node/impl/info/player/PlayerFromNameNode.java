package de.blazemcworld.fireflow.code.node.impl.info.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

public class PlayerFromNameNode extends Node {
    public PlayerFromNameNode() {
        super("player_from_name", Material.PLAYER_HEAD);

        Input<String> name = new Input<>("name", StringType.INSTANCE);
        Output<PlayerValue> player = new Output<>("player", PlayerType.INSTANCE);

        player.valueFrom(ctx -> {
            for (Player p : ctx.evaluator.space.play.getPlayers()) {
                if (p.getUsername().equals(name.getValue(ctx))) {
                    return new PlayerValue(p);
                }
            }
            return PlayerType.INSTANCE.defaultValue();
        });
    }

    @Override
    public Node copy() {
        return new PlayerFromNameNode();
    }
}