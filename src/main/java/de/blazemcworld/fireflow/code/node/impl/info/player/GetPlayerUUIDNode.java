package de.blazemcworld.fireflow.code.node.impl.info.player;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minestom.server.item.Material;

public class GetPlayerUUIDNode extends Node {
    public GetPlayerUUIDNode() {
        super("get_player_uuid", Material.GOLDEN_APPLE);

        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Output<String> uuid = new Output<>("uuid", StringType.INSTANCE);

        uuid.valueFrom(ctx -> player.getValue(ctx).uuid.toString());
    }

    @Override
    public Node copy() {
        return new GetPlayerUUIDNode();
    }
}
