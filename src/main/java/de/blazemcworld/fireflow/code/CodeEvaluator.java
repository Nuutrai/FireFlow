package de.blazemcworld.fireflow.code;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.widget.NodeWidget;
import de.blazemcworld.fireflow.code.widget.Widget;
import de.blazemcworld.fireflow.space.Space;
import de.blazemcworld.fireflow.util.Config;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CodeEvaluator {

    public final Space space;
    public final EventNode<InstanceEvent> events;
    private boolean stopped = false;
    private long cpuUsed = 0;

    public CodeEvaluator(Space space) {
        this.space = space;

        Set<Node> nodes = new HashSet<>();
        for (Widget widget : space.editor.rootWidgets) {
            if (widget instanceof NodeWidget nodeWidget) {
                nodes.add(nodeWidget.node);
            }
        }

        events = EventNode.type("space-" + space.info.id, EventFilter.INSTANCE);
        space.play.eventNode().addChild(events);

        nodes = copyNodes(nodes);

        for (Node node : nodes) {
            node.init(this);
        }

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            if (isStopped()) return TaskSchedule.stop();
            if (cpuUsed > Config.store.limits().cpuPerTick()) {
                FireFlow.LOGGER.info("Space " + space.info.id + " used too much CPU: " + (cpuUsed * 100 / Config.store.limits().cpuPerTick()) + "%");
                space.reload();
                return TaskSchedule.stop();
            }
            cpuUsed = 0;
            return TaskSchedule.tick(1);
        }, TaskSchedule.tick(1));
    }

    public void stop() {
        space.play.eventNode().removeChild(events);
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    @SuppressWarnings("unchecked")
    private Set<Node> copyNodes(Set<Node> nodes) {
        HashMap<Node, Node> old2new = new HashMap<>();
        for (Node node : nodes) {
            old2new.put(node, node.copy());
        }

        for (Node old : nodes) {
            Node copy = old2new.get(old);
            for (int i = 0; i < copy.inputs.size(); i++) {
                Node.Input<?> newInput = copy.inputs.get(i);
                Node.Output<?> oldTarget = old.inputs.get(i).connected;
                if (oldTarget == null) continue;
                Node.Output<?> newTarget = old2new.get(oldTarget.getNode()).outputs.get(oldTarget.getNode().outputs.indexOf(oldTarget));
                ((Node.Input<Object>) newInput).connected = (Node.Output<Object>) newTarget;
            }

            for (int i = 0; i < copy.outputs.size(); i++) {
                Node.Output<?> newOutput = copy.outputs.get(i);
                Node.Input<?> oldTarget = old.outputs.get(i).connected;
                if (oldTarget == null) continue;
                Node.Input<?> newTarget = old2new.get(oldTarget.getNode()).inputs.get(oldTarget.getNode().inputs.indexOf(oldTarget));
                ((Node.Output<Object>) newOutput).connected = (Node.Input<Object>) newTarget;
            }

            for (int i = 0; i < copy.inputs.size(); i++) {
                Node.Input<?> newInput = copy.inputs.get(i);
                Node.Input<?> oldInput = old.inputs.get(i);
                newInput.inset = oldInput.inset;
            }
        }

        return new HashSet<>(old2new.values());
    }

    public CodeThread newCodeThread() {
        return new CodeThread(this);
    }

    public boolean timelimitHit(long elapsed) {
        cpuUsed += elapsed;
        return cpuUsed > Config.store.limits().cpuPerTick();
    }
}