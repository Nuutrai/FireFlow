package de.blazemcworld.fireflow.code.node.impl.number;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import net.minestom.server.item.Material;

public class AbsoluteNumberNode extends Node {
    public AbsoluteNumberNode() {
        super("absolute_number", Material.PISTON);
        Input<Double> value = new Input<>("value", NumberType.INSTANCE);
        Output<Double> positive_value = new Output<>("positive_value", NumberType.INSTANCE);

        positive_value.valueFrom((ctx -> Math.abs(value.getValue(ctx))));
    }

    @Override
    public Node copy() {
        return new AbsoluteNumberNode();
    }
}