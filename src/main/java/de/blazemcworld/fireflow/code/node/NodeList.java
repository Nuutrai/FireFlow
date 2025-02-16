package de.blazemcworld.fireflow.code.node;

import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.code.node.impl.action.CancelEventNode;
import de.blazemcworld.fireflow.code.node.impl.action.player.*;
import de.blazemcworld.fireflow.code.node.impl.condition.*;
import de.blazemcworld.fireflow.code.node.impl.event.player.*;
import de.blazemcworld.fireflow.code.node.impl.event.space.OnChunkLoadNode;
import de.blazemcworld.fireflow.code.node.impl.flow.*;
import de.blazemcworld.fireflow.code.node.impl.info.GetBlockNode;
import de.blazemcworld.fireflow.code.node.impl.info.player.*;
import de.blazemcworld.fireflow.code.node.impl.item.CreateItemNode;
import de.blazemcworld.fireflow.code.node.impl.item.ItemsEqualNode;
import de.blazemcworld.fireflow.code.node.impl.item.SetItemCountNode;
import de.blazemcworld.fireflow.code.node.impl.list.*;
import de.blazemcworld.fireflow.code.node.impl.number.*;
import de.blazemcworld.fireflow.code.node.impl.position.FacingVectorNode;
import de.blazemcworld.fireflow.code.node.impl.position.PackPositionNode;
import de.blazemcworld.fireflow.code.node.impl.position.UnpackPositionNode;
import de.blazemcworld.fireflow.code.node.impl.string.CharacterAtNode;
import de.blazemcworld.fireflow.code.node.impl.string.CombineStringsNode;
import de.blazemcworld.fireflow.code.node.impl.string.StringLengthNode;
import de.blazemcworld.fireflow.code.node.impl.text.CombineTextsNode;
import de.blazemcworld.fireflow.code.node.impl.text.FormatToTextNode;
import de.blazemcworld.fireflow.code.node.impl.text.StringToTextNode;
import de.blazemcworld.fireflow.code.node.impl.variable.*;
import de.blazemcworld.fireflow.code.node.impl.vector.*;
import de.blazemcworld.fireflow.code.node.impl.world.SetBlockNode;
import de.blazemcworld.fireflow.code.node.impl.world.SetRegionNode;
import de.blazemcworld.fireflow.util.Translations;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class NodeList {

    public static Category root;

    public static void init() {
        root = new Category("root", null)
            .add(new Category("action", Material.REDSTONE)
                    .add(new Category("player", Material.PLAYER_HEAD)
                            .add(new ClearInventoryNode())
                            .add(new GivePlayerItemNode())
                            .add(new KillPlayerNode())
                            .add(new PlayerAnimationNode())
                            .add(new RespawnPlayerNode())
                            .add(new SendActionbarNode())
                            .add(new SendBlockChangeNode())
                            .add(new SendMessageNode())
                            .add(new SendParticleNode())
                            .add(new SendTitleNode())
                            .add(new SetAllowFlyingNode())
                            .add(new SetExperienceLevelNode())
                            .add(new SetExperiencePercentageNode())
                            .add(new SetGamemodeNode())
                            .add(new SetHeldSlotNode())
                            .add(new SetPlayerFlyingNode())
                            .add(new SetPlayerFoodNode())
                            .add(new SetPlayerHealthNode())
                            .add(new SetPlayerInvulnerableNode())
                            .add(new SetPlayerSaturationNode())
                            .add(new SetPlayerVelocityNode())
                            .add(new TeleportPlayerNode())
                    )
                    .add(new CancelEventNode())
            )
            .add(new Category("condition", Material.COMPARATOR)
                    .add(new ConditionAndNode())
                    .add(new ConditionalChoiceNode<>(null))
                    .add(new ConditionOrNode())
                    .add(new InvertConditionNode())
                    .add(new ValuesEqualNode<>(null))
            )
            .add(new Category("event", Material.OBSERVER)
                    .add(new OnChunkLoadNode())
                    .add(new OnPlayerAttackPlayerNode())
                    .add(new OnPlayerChatNode())
                    .add(new OnPlayerJoinNode())
                    .add(new OnPlayerStartFlyingNode())
                    .add(new OnPlayerStartGlidingNode())
                    .add(new OnPlayerStartSneakingNode())
                    .add(new OnPlayerStartSprintingNode())
                    .add(new OnPlayerStopFlyingNode())
                    .add(new OnPlayerStopGlidingNode())
                    .add(new OnPlayerStopSneakingNode())
                    .add(new OnPlayerStopSprintingNode())
                    .add(new OnPlayerUseItemNode())
            )
            .add(new Category("flow", Material.REPEATER)
                    .add(new IfNode())
                    .add(new ListForEachNode<>(null))
                    .add(new PauseThreadNode())
                    .add(new RepeatNode())
                    .add(new ScheduleNode())
                    .add(new WhileNode())
            )
            .add(new Category("item", Material.ITEM_FRAME)
                    .add(new CreateItemNode())
                    .add(new ItemsEqualNode())
                    .add(new SetItemCountNode())
            )
            .add(new Category("info", Material.ENDER_EYE)
                    .add(new GetBlockNode())
                    .add(new GetExperienceLevelNode())
                    .add(new GetExperiencePercentageNode())
                    .add(new GetPlayerFoodNode())
                    .add(new GetPlayerHealthNode())
                    .add(new GetPlayerSaturationNode())
                    .add(new GetHeldSlotNode())
                    .add(new IsPlayerInvulnerableNode())
                    .add(new IsPlayerSneakingNode())
                    .add(new IsPlayingNode())
                    .add(new PlayerCanFlyNode())
                    .add(new PlayerIsFlyingNode())
                    .add(new PlayerListNode())
                    .add(new PlayerMainItemNode())
            )
            .add(new Category("list", Material.BOOKSHELF)
                    .add(new CreateListNode<>(null))
                    .add(new GetListIndexNode<>(null))
                    .add(new ListAppendNode<>(null))
                    .add(new ListContainsNode<>(null))
                    .add(new ListLengthNode<>(null))
                    .add(new RemoveListIndexNode<>(null))
            )
            .add(new Category("number", Material.CLOCK)
                    .add(new Category("noises", Material.GRAY_CONCRETE_POWDER)
                            .add(new BasicNoiseNode())
                    )
                    .add(new AbsoluteNumberNode())
                    .add(new AddNumbersNode())
                    .add(new ClampNumberNode())
                    .add(new DivideNumbersNode())
                    .add(new GreaterEqualNode())
                    .add(new GreaterThanNode())
                    .add(new LessEqualNode())
                    .add(new LessThanNode())
                    .add(new MultiplyNumbersNode())
                    .add(new NumberToStringNode())
                    .add(new NumberToTextNode())
                    .add(new ParseNumberNode())
                    .add(new RandomNumberNode())
                    .add(new RoundNumberNode())
                    .add(new SetToExponentialNode())
                    .add(new SquareRootNode())
                    .add(new SubtractNumbersNode())
            )
            .add(new Category("position", Material.COMPASS)
                    .add(new FacingVectorNode())
                    .add(new PackPositionNode())
                    .add(new PlayerPositionNode())
                    .add(new UnpackPositionNode())
            )
            .add(new Category("string", Material.STRING)
                    .add(new CharacterAtNode())
                    .add(new CombineStringsNode())
                    .add(new StringLengthNode())
            )
            .add(new Category("vector", Material.ARROW)
                    .add(new GetVectorComponentNode())
                    .add(new PackVectorNode())
                    .add(new SetVectorComponentNode())
                    .add(new SetVectorLengthNode())
                    .add(new UnpackVectorNode())
            )
            .add(new Category("text", Material.WRITABLE_BOOK)
                    .add(new CombineTextsNode())
                    .add(new FormatToTextNode())
                    .add(new StringToTextNode())
            )
            .add(new Category("variable", Material.ENDER_CHEST)
                    .add(new CacheValueNode<>(null))
                    .add(new DecrementVariableNode())
                    .add(new GetVariableNode<>(null))
                    .add(new IncrementVariableNode())
                    .add(new SetVariableNode<>(null))
            )
            .add(new Category("function", Material.COMMAND_BLOCK).markFunctions())
            .add(new Category("world",Material.GRASS_BLOCK)
                    .add(new SetBlockNode())
                    .add(new SetRegionNode())
            )
            .finish();

        FireFlow.LOGGER.info("Loaded " + root.collectNodes().size() + " node types");
    }

    public static Category getRoot() {
        return root;
    }

    public static class Category {
        public final String name;
        public final String rawName;
        public final Material icon;

        public final List<Category> categories = new ArrayList<>();
        public final List<Node> nodes = new ArrayList<>();
        public boolean isFunctions = false;
        public Predicate<Node> filter;

        public Category(String id, Material icon) {
            name = Translations.get("category." + id);
            rawName = id;
            this.icon = icon;
        }

        public Category(Category copy) {
            name = copy.name;
            rawName = copy.rawName;
            icon = copy.icon;
            isFunctions = copy.isFunctions;
        }

        public Category add(Node node) {
            nodes.add(node);
            return this;
        }

        public Category add(Category category) {
            categories.add(category);
            return this;
        }

        public Category finish() {
            for (Category category : categories) {
                category.finish();
            }
            categories.sort(Comparator.comparing(c -> c.name));
            nodes.sort(Comparator.comparing(Node::getTitle));
            return this;
        }

        public List<Node> collectNodes() {
            List<Node> list = new ArrayList<>(nodes);
            for (Category category : categories) {
                list.addAll(category.collectNodes());
            }
            return list;
        }

        public Category markFunctions() {
            isFunctions = true;
            return this;
        }

        public Category filtered(Predicate<Node> filter) {
            Category filtered = new Category(this);
            for (Node n : nodes) {
                if (filter.test(n)) filtered.add(n);
            }
            for (Category c : categories) {
                Category fc = c.filtered(filter);
                if (fc.isFunctions) {
                    fc.filter = filter;
                    filtered.categories.add(fc);
                    continue;
                }
                if (fc.nodes.isEmpty() && fc.categories.isEmpty()) continue;
                filtered.categories.add(fc);
            }
            return filtered;
        }

        public Category getCategory(String id) {
            if (id.equals("root") || id.equals("main")) {
                return getRoot();
            }

            for (Category category: categories) {
                if (category.rawName.equals(id)) {
                    return category;
                }
            }

            System.out.println("'" + this.rawName + "' has no child category named '" + id + "'");

            return null;

        }

    }
}
