package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.code.node.Node;
import net.minestom.server.event.Event;

import java.util.HashMap;
import java.util.Stack;

public class CodeThread {

    public final CodeEvaluator evaluator;
    private final Stack<Runnable> todo = new Stack<>();
    public final VariableStore threadVariables = new VariableStore();
    public HashMap<Node.Output<?>, Object> rootScopeStore = new HashMap<>();
    public FunctionScope functionScope;
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
    public <T> T getScopeValue(Node.Output<T> out) {
        Object v = (functionScope == null ? rootScopeStore : functionScope.store).get(out);
        return v == null ? out.type.defaultValue() : (T) v;
    }

    public <T> void setScopeValue(Node.Output<T> out, T value) {
        (functionScope == null ? rootScopeStore : functionScope.store).put(out, value);
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
        thread.rootScopeStore.putAll(rootScopeStore);
        if (functionScope != null) thread.functionScope = functionScope.copy();
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
