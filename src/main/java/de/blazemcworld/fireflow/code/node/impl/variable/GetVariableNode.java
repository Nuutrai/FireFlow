package de.blazemcworld.fireflow.code.node.impl.variable;

import de.blazemcworld.fireflow.code.VariableStore;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.minestom.server.item.Material;

public class GetVariableNode<T> extends SingleGenericNode<T> {

    public GetVariableNode(WireType<T> type) {
        super("get_variable", Material.IRON_INGOT, type);

        Input<String> name = new Input<>("name", StringType.INSTANCE);
        Input<String> scope = new Input<>("scope", StringType.INSTANCE)
                .options("thread", "session", "saved");
        Output<T> value = new Output<>("value", type);

        value.valueFrom((ctx) -> {
            VariableStore store = switch (scope.getValue(ctx)) {
                case "saved" -> ctx.evaluator.space.savedVariables;
                case "session" -> ctx.evaluator.sessionVariables;
                case "thread" -> ctx.threadVariables;
                default -> null;
            };
            if (store == null) return type.defaultValue();
            return store.get(name.getValue(ctx), type);
        });
    }

    @Override
    public Node copy() {
        return new GetVariableNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new GetVariableNode<>(type);
    }
}
