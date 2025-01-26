package de.blazemcworld.fireflow.code.node.impl.vector;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.VectorType;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.item.Material;

public class AddVectorsNode extends Node {

    public AddVectorsNode() {
        super("add_vectors", Material.ANVIL);

        Input<Vec> first = new Input<>("first", VectorType.INSTANCE);
        Input<Vec> second = new Input<>("second", VectorType.INSTANCE);
        Output<Vec> result = new Output<>("result", VectorType.INSTANCE);

    	result.valueFrom(ctx -> first.getValue(ctx).add(second.getValue(ctx)));
    }

    @Override
    public Node copy() {
        return new AddVectorsNode();
    }
}
