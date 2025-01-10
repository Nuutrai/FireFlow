package de.blazemcworld.fireflow.code.node.impl.condition;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.ConditionType;
import net.minestom.server.item.Material;

public class ConditionOrNode extends Node {

    public ConditionOrNode() {
        super("conditional_or", Material.REDSTONE_ORE);

        Input<Boolean> primary = new Input<>("primary", ConditionType.INSTANCE);
        Varargs<Boolean> others = new Varargs<>("others", ConditionType.INSTANCE);
        Output<Boolean> result = new Output<>("result", ConditionType.INSTANCE);

        result.valueFrom((ctx) -> {
            if (primary.getValue(ctx)) return true;
            return others.getVarargs(ctx).contains(true);
        });
    }

    @Override
    public Node copy() {
        return new ConditionOrNode();
    }
}
