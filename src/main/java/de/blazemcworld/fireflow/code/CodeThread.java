package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.node.impl.function.FunctionCallNode;
import net.minestom.server.event.Event;

import java.util.HashMap;
import java.util.Stack;

public class CodeThread {

    public final CodeEvaluator evaluator;
    private final HashMap<Node.Output<?>, Object> threadValues = new HashMap<>();
    private final Stack<Runnable> todo = new Stack<>();
    public final VariableStore threadVariables = new VariableStore();
    public final Stack<FunctionCallNode> functionStack = new Stack<>();
    public Event event = null;
    private boolean paused = false;

    public CodeThread(CodeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public CodeThread(CodeEvaluator evaluator, Event event) {
        this.evaluator = evaluator;
        this.event = event;
    }

    @SuppressWarnings("unchecked")
    public <T> T getThreadValue(Node.Output<T> out) {
        Object v = threadValues.get(out);
        if (v == null) return out.type.defaultValue();
        return (T) threadValues.get(out);
    }

    public <T> void setThreadValue(Node.Output<T> out, T value) {
        threadValues.put(out, value);
    }

    public void sendSignal(Node.Output<Void> signal) {
        if (evaluator.space.debugger.isActive()) {
            evaluator.space.debugger.onSignal(signal, this);
        }
        todo.push(() -> signal.sendSignalImmediately(this));
    }

    public void submit(Runnable r) {
        todo.add(r);
    }

    public void clearQueue() {
        if (evaluator.isStopped()) return;
        while (!todo.isEmpty() && !paused) {
            todo.pop().run();
            if (evaluator.isStopped()) return;
        }
    }

    public CodeThread subThread() {
        CodeThread thread = new CodeThread(evaluator, event);
        thread.threadValues.putAll(threadValues);
        return thread;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
        clearQueue();
    }
}
