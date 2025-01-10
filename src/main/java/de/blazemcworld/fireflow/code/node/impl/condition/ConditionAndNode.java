package de.blazemcworld.fireflow.code.node.impl.condition;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import net.minestom.server.item.Material;

public class ConditionAndNode extends Node {

    public ConditionAndNode() {
        super("conditional_and", Material.REDSTONE_BLOCK);

        Input<Boolean> primary = new Input<>("primary", ConditionType.INSTANCE);
        Varargs<Boolean> others = new Varargs<>("others", ConditionType.INSTANCE);
        Output<Boolean> result = new Output<>("result", ConditionType.INSTANCE);

        result.valueFrom((ctx) -> {
            if (!primary.getValue(ctx)) return false;
            return !others.getVarargs(ctx).contains(false);
        });
    }

    @Override
    public Node copy() {
        return new ConditionAndNode();
    }
}
