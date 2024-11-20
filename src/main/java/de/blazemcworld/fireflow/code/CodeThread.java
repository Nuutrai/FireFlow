package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.code.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CodeThread {

    public final CodeEvaluator evaluator;
    private final HashMap<Node.Output<?>, Object> threadValues = new HashMap<>();
    private final List<Runnable> todo = new ArrayList<>();

    public CodeThread(CodeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @SuppressWarnings("unchecked")
    public <T> T getThreadValue(Node.Output<T> out) {
        return (T) threadValues.get(out);
    }

    public <T> void setThreadValue(Node.Output<T> out, T value) {
        threadValues.put(out, value);
    }

    public void sendSignal(Node.Output<Void> signal) {
        todo.add(() -> signal.sendSignalImmediately(this));
    }

    public void clearQueue() {
        while (!todo.isEmpty()) {
            todo.removeFirst().run();
        }
    }
}
