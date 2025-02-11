package de.blazemcworld.fireflow.code.node.impl.player.info;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ListType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.value.ListValue;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;

public class PlayerListNode extends Node {

    public PlayerListNode() {
        super("player_list", Material.LIGHT_BLUE_DYE);

        Output<ListValue<PlayerValue>> playing = new Output<>("players", ListType.of(PlayerType.INSTANCE));

        playing.valueFrom((ctx) ->{
            List<PlayerValue> out = new ArrayList<>();
            for (Player p : ctx.evaluator.space.play.getPlayers()) {
                out.add(new PlayerValue(p));
            }
            return new ListValue<>(PlayerType.INSTANCE, out);
        });
    }

    @Override
    public Node copy() {
        return new PlayerListNode();
    }
}
