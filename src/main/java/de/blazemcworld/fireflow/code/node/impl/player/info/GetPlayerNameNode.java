package de.blazemcworld.fireflow.code.node.impl.player.info;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;

public class GetPlayerNameNode extends Node {
    public GetPlayerNameNode() {
        super("get_player_name", Material.NAME_TAG);

        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<String> name = new Output<>("name", StringType.INSTANCE);

        name.valueFrom(ctx -> player.getValue(ctx).tryGet(ctx, Player::getUsername, ""));
    }

    @Override
    public Node copy() {
        return new GetPlayerNameNode();
    }
}