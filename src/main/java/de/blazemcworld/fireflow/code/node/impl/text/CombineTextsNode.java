package de.blazemcworld.fireflow.code.node.impl.text;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.TextType;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

public class CombineTextsNode extends Node {
    public CombineTextsNode() {
        super("combine_texts", Material.SLIME_BALL);

        Varargs<Component> texts = new Varargs<>("texts", TextType.INSTANCE);
        Output<Component> combined = new Output<>("combined", TextType.INSTANCE);

        combined.valueFrom(ctx -> {
            Component out = Component.empty();
            for (Component text : texts.getVarargs(ctx)) {
                out = out.append(text);
            }
            return out;
        });
    }

    @Override
    public Node copy() {
        return new CombineTextsNode();
    }
}

