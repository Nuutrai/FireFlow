package de.blazemcworld.fireflow.code.node.impl.condition;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.SingleGenericNode;
import de.blazemcworld.fireflow.code.type.ConditionType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.minestom.server.item.Material;

public class ConditionalChoiceNode<T> extends SingleGenericNode<T> {

    public ConditionalChoiceNode(WireType<T> type) {
        super("conditional_choice", Material.WATER_BUCKET, type);

        Input<Boolean> condition = new Input<>("condition", ConditionType.INSTANCE);
        Input<T> trueValue = new Input<>("trueValue", type);
        Input<T> falseValue = new Input<>("falseValue", type);
        Output<T> choice = new Output<>("choice", type);

        choice.valueFrom(ctx -> {
            if (condition.getValue(ctx)) return trueValue.getValue(ctx);
            return falseValue.getValue(ctx);
        });
    }

    @Override
    public Node copy() {
        return new ConditionalChoiceNode<>(type);
    }

    @Override
    public Node copyWithType(WireType<?> type) {
        return new ConditionalChoiceNode<>(type);
    }
}
