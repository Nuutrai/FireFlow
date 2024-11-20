package de.blazemcworld.fireflow.code.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class TextType extends WireType<Component> {

    public static final TextType INSTANCE = new TextType();

    public static final MiniMessage MM = MiniMessage.builder()
            .tags(TagResolver.builder().resolvers(
                    StandardTags.color(),
                    StandardTags.decorations(),
                    StandardTags.font(),
                    StandardTags.gradient(),
                    StandardTags.keybind(),
                    StandardTags.newline(),
                    StandardTags.rainbow(),
                    StandardTags.reset(),
                    StandardTags.transition(),
                    StandardTags.translatable()
            ).build()).build();

    private TextType() {
    }

    @Override
    public String id() {
        return "text";
    }

    @Override
    public Component defaultValue() {
        return Component.empty();
    }

    @Override
    public TextColor getColor() {
        return NamedTextColor.LIGHT_PURPLE;
    }

    @Override
    public Component parseInset(String str) {
        return MM.deserialize(str);
    }
}