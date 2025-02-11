package de.blazemcworld.fireflow.code.node.impl.player.effect;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.TextType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

public class SendActionbarNode extends Node {

    public SendActionbarNode() {
        super("send_actionbar", Material.BAMBOO);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<PlayerValue> player = new Input<>("player", PlayerType.INSTANCE);
        Input<Component> message = new Input<>("message", TextType.INSTANCE);

        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            player.getValue(ctx).tryUse(ctx, p -> p.sendActionBar(message.getValue(ctx)));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SendActionbarNode();
    }
}

