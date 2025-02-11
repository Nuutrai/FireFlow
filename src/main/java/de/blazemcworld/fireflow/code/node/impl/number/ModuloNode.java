package de.blazemcworld.fireflow.code.node.impl.number;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import net.minestom.server.item.Material;

public class ModuloNode extends Node {

    public ModuloNode() {
        super("modulo_number", Material.BONE_MEAL);

        Input<Double> left = new Input<>("left", NumberType.INSTANCE);
        Input<Double> right = new Input<>("right", NumberType.INSTANCE);
        Output<Double> result = new Output<>("result", NumberType.INSTANCE);

        result.valueFrom((ctx) -> {
            double l = left.getValue(ctx);
            double r = right.getValue(ctx);
            return ((l % r) + r) % r;
        });
    }

    @Override
    public Node copy() {
        return new ModuloNode();
    }
}

