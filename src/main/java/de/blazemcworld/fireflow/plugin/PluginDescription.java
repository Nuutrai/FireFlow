package de.blazemcworld.fireflow.plugin;

import com.google.common.collect.ImmutableList;
import de.blazemcworld.fireflow.plugin.exceptions.InvalidDescriptionException;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class PluginDescription {

    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
        @Override
        @NotNull
        protected Yaml initialValue() {
            DumperOptions dumperOptions = new DumperOptions();
            return new Yaml(new SafeConstructor(new LoaderOptions()), new Representer(dumperOptions), dumperOptions);
        }
    };


    private String name;
    private String main;
    private List<String> authors;


    public PluginDescription(InputStream stream) throws InvalidDescriptionException {
        loadFromMap(asMap(YAML.get().load(stream)));
    }


    private void loadFromMap(@NotNull Map<?, ?> map) throws InvalidDescriptionException {
        try {
            name = map.get("name").toString();
            if (!VALID_NAME.matcher(name).matches()) {
                throw new InvalidDescriptionException(String.format("The name '%s' contains invalid characters!", name));
            }
            name = name.replace(' ', '_');
        } catch (InvalidDescriptionException e) {
            throw new InvalidDescriptionException("name is not defined", e);
        }

        try {
            main = map.get("main").toString();
            if (main.startsWith("net.minestom.")) {
                throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
            }
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException("main is not defined", ex);
        }

        if (map.get("authors") != null) {
            ImmutableList.Builder<String> authorsBuilder = ImmutableList.<String>builder();
            if (map.get("author") != null) {
                authorsBuilder.add(map.get("author").toString());
            }
            try {
                for (Object o : (Iterable<?>) map.get("authors")) {
                    authorsBuilder.add(o.toString());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException("authors are of wrong type", ex);
            } catch (NullPointerException ex) {
                throw new InvalidDescriptionException("authors are improperly defined", ex);
            }
            authors = authorsBuilder.build();
        } else if (map.get("author") != null) {
            authors = ImmutableList.of(map.get("author").toString());
        } else {
            authors = ImmutableList.<String>of();
        }
    }

    private Map<?, ?> asMap(@NotNull Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return ((Map<?, ?>) object);
        }
        throw new InvalidDescriptionException(String.format("plugin.yml is not structured correctly! Found %s, but should be a map", name));
    }

    public String getName() {
        return name;
    }

    public String getMain() {
        return main;
    }

    public List<String> getAuthors() {
        return authors;
    }
}
