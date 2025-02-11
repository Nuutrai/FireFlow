package de.blazemcworld.fireflow.code.node.impl.variable;

import de.blazemcworld.fireflow.code.VariableStore;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.minestom.server.item.Material;

public class SetVariableNode<T> extends SingleGenericNode<T> {

    public SetVariableNode(WireType<T> type) {
        super("set_variable", Material.IRON_BLOCK, type);

        Input<Void> signal = new Input<>("signal", SignalType.INSTANCE);
        Input<String> name = new Input<>("name", StringType.INSTANCE);
        Input<String> scope = new Input<>("scope", StringType.INSTANCE)
                .options("thread", "session", "saved");
        Input<T> value = new Input<>("value", type);
        Output<Void> next = new Output<>("next", SignalType.INSTANCE);

        signal.onSignal((ctx) -> {
            VariableStore store = switch (scope.getValue(ctx)) {
                case "saved" -> ctx.evaluator.space.savedVariables;
                case "session" -> ctx.evaluator.sessionVariables;
                case "thread" -> ctx.threadVariables;
                default -> null;
            };
            if (store != null) store.set(name.getValue(ctx), type, value.getValue(ctx));
            ctx.sendSignal(next);
        });
    }

    @Override
    public Node copy() {
        return new SetVariableNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new SetVariableNode<>(type);
    }
}
