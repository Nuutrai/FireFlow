package de.blazemcworld.fireflow.code.widget;

import de.blazemcworld.fireflow.code.CodeEditor;
import de.blazemcworld.fireflow.code.Interaction;
import de.blazemcworld.fireflow.code.action.WireAction;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.WireType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.InstanceContainer;

import java.util.ArrayList;
import java.util.List;

public class NodeIOWidget implements Widget {

    private final TextWidget text;
    public final List<WireWidget> connections = new ArrayList<>();
    private final boolean isInput;
    private final WireType<?> type;
    public final Node.Output<?> output;
    public final Node.Input<?> input;
    public final NodeWidget parent;

    public NodeIOWidget(NodeWidget parent, Node.Output<?> output) {
        type = output.type;
        isInput = false;
        this.output = output;
        this.input = null;
        this.parent = parent;
        text = new TextWidget(displayText());
        text.shiftLeft = true;
    }

    public NodeIOWidget(NodeWidget parent, Node.Input<?> input) {
        type = input.type;
        isInput = true;
        this.output = null;
        this.input = input;
        this.parent = parent;
        text = new TextWidget(displayText());
    }

    @Override
    public void setPos(Vec pos) {
        text.setPos(pos);
    }

    @Override
    public Vec getPos() {
        return text.getPos();
    }

    @Override
    public Vec getSize() {
        return text.getSize();
    }

    @Override
    public void update(InstanceContainer inst) {
        text.text(displayText());
        text.update(inst);
    }

    @Override
    public void remove() {
        text.remove();
    }

    @Override
    public boolean interact(Interaction i) {
        if (!inBounds(i.pos())) return false;
        if (isInput && input.options != null) {
            int currentOption = input.options.indexOf(input.inset);
            if (i.type() == Interaction.Type.LEFT_CLICK) {
                int next = currentOption + 1;
                if (next >= input.options.size()) next = 0;
                insetValue(input.options.get(next), i.editor());
                return true;
            }
            if (i.type() == Interaction.Type.RIGHT_CLICK) {
                int next = currentOption - 1;
                if (next < 0) next = input.options.size() - 1;
                insetValue(input.options.get(next), i.editor());
                return true;
            }
        }
        if (i.type() == Interaction.Type.RIGHT_CLICK) {
            if (!this.isInput()) i.editor().setAction(i.player(), new WireAction(this, i.editor(), i.player()));
            return true;
        }
        if (i.type() == Interaction.Type.LEFT_CLICK && isInput && input.inset != null) {
            insetValue(null, i.editor());
            parent.update(i.editor().space.code);
            return true;
        }
        return false;
    }

    public boolean isInput() {
        return isInput;
    }

    public TextColor color() {
        return text.text().color();
    }

    public WireType<?> type() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public void connect(WireWidget wire) {
        if (wire.type() == SignalType.INSTANCE) {
            NodeIOWidget input = wire.getOutputs().getFirst();
            for (NodeIOWidget output : wire.getInputs()) {
                ((Node.Output<Object>) output.output).connected = (Node.Input<Object>) input.input;
            }
        } else {
            NodeIOWidget output = wire.getInputs().getFirst();
            for (NodeIOWidget input : wire.getOutputs()) {
                ((Node.Input<Object>) input.input).connect((Node.Output<Object>) output.output);
            }
        }
        text.text(displayText());
        parent.refreshInputs();
    }

    public void removed(WireWidget wire) {
        if (wire.type() == SignalType.INSTANCE) {
            if (wire.previousOutput != null) {
                wire.previousOutput.output.connected = null;
            }
        } else {
            if (wire.nextInput != null) {
                wire.nextInput.input.connect(null);
                if (wire.nextInput.input.varargsParent != null) wire.nextInput.input.varargsParent.update();
            }
        }
        text.text(displayText());
        parent.refreshInputs();
    }

    public void insetValue(String value, CodeEditor editor) {
        input.setInset(value);

        for (WireWidget w : new ArrayList<>(connections)) {
            List<NodeIOWidget> inputs = w.getInputs();
            List<NodeIOWidget> outputs = w.getOutputs();
            w.removeConnection(editor);
            if (w.type() == SignalType.INSTANCE && !outputs.getFirst().connections.isEmpty()) outputs.getFirst().connections.getFirst().cleanup(editor);
            else if (!inputs.getFirst().connections.isEmpty()) inputs.getFirst().connections.getFirst().cleanup(editor);
        }

        text.text(displayText());
        parent.refreshInputs();
    }

    private Component displayText() {
        String str = isInput ? ((connections.isEmpty() ? "\u25CB " : "\u23FA ") + input.getName()) :
            (output.getName() + (connections.isEmpty() ? " \u25CB" : " \u23FA"));
        
        if (isInput && input.inset != null) {
            str = "\u23F9 " + input.inset;
        }

        return Component.text(str).color(type.color);
    }

    @Override
    public List<Widget> getChildren() {
        return null;
    }
}