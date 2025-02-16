package de.blazemcworld.fireflow.plugin;

import com.google.common.base.Preconditions;
import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.plugin.exceptions.InvalidDescriptionException;
import de.blazemcworld.fireflow.plugin.exceptions.InvalidPluginException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static de.blazemcworld.fireflow.FireFlow.LOGGER;

public final class PluginLoader {

    private final List<PluginClassLoader> loaders = new CopyOnWriteArrayList<PluginClassLoader>();
    private final List<FireFlowPlugin> plugins = new ArrayList<>();

    public static final File pluginFolder = new File("plugins");
    private static final Pattern VALID_JAR = Pattern.compile(".+\\.jar$");

    public void init() {
        if (pluginFolder.exists()) {
            pluginFolder.mkdir();
        }
        loadJars();
        enablePluins();
    }

    public void loadJars() {
        ArrayList<File> files = new ArrayList<>();
        try {
            getFiles(pluginFolder, files);
        } catch (IOException e) {
            LOGGER.error("Failed to retrieve plugin files!", e);
        }

        for (File file : files) {
            try {
                System.out.printf("Attempting to load plugin: %s%n", file.getName());
                FireFlowPlugin plugin = loadPlugin(file);
                if (plugin != null) plugins.add(plugin);
            } catch (InvalidPluginException e) {
                LOGGER.error("Failed to load plugin!", e);
            }
        }

    }

    private FireFlowPlugin loadPlugin(File file) throws InvalidPluginException {
        final PluginDescription description;
        try {
            description = getDescription(file);
        } catch (InvalidDescriptionException e) {
            throw new InvalidPluginException(e);
        }

        final File dataFolder = new File(pluginFolder, description.getName());
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        final PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(getClass().getClassLoader(), description, dataFolder, file);
        } catch (Exception e) {
            throw new InvalidPluginException(e);
        }

        loaders.add(loader);

        return loader.plugin;
    }

    public void enablePluins() {
        for (FireFlowPlugin plugin: plugins) {
            enablePlugin(plugin);
        }
    }

    public void enablePlugin(@NotNull final FireFlowPlugin plugin) {

        if (!plugin.isEnabled()) {
            plugin.getLogger().info("Enabling " + plugin.getDescription().getName());


            PluginClassLoader pluginLoader = plugin.getPluginClassLoader();

            if (!loaders.contains(pluginLoader)) {
                loaders.add(pluginLoader);
                LOGGER.warn("Enabled plugin with unregistered PluginClassLoader " + plugin.getDescription().getName());
            }

            try {
                plugin.setEnabled(true);
            } catch (Throwable ex) {
                LOGGER.error("Error occurred while enabling {} (Is it up to date?)", plugin.getDescription().getName(), ex);
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
//            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    public void disablePlugins() {
        for (FireFlowPlugin plugin: plugins) {
            disablePlugin(plugin);
        }
    }

    public void disablePlugin(@NotNull final FireFlowPlugin plugin) {

        if (plugin.isEnabled()) {
            plugin.getLogger().info("Disabling " + plugin.getDescription().getName());

            PluginClassLoader pluginLoader = plugin.getPluginClassLoader();

            try {
                plugin.setEnabled(false);
            } catch (Throwable ex) {
                LOGGER.error("Error occurred while disabling {}", plugin.getDescription().getName(), ex);
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
//            server.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @SuppressWarnings("ConstantValue")
    private PluginDescription getDescription(File file) throws InvalidDescriptionException {

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry pluginYAML = jar.getJarEntry("plugin.yml");

            if (pluginYAML == null) {
                throw new InvalidDescriptionException(new FileNotFoundException(String.format("No plugin.yml was found in file: %s", jar.getName())));
            }

            return new PluginDescription(jar.getInputStream(pluginYAML));

        } catch (IOException e) {
            throw new InvalidDescriptionException(e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ignored) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }



    private void getFiles(File directory, ArrayList<File> list) throws IOException {
        if (directory.listFiles() == null) {
            return;
        }

        System.out.printf("Current working directory: %s%n", directory.getName());

        for (File file: directory.listFiles()) {
            if (file.equals(directory)) {
                getFiles(file, list);
            }

            if (!file.isFile()) {
                continue;
            }

            System.out.printf("Current working file: %s%n", file.getName());

            if (VALID_JAR.matcher(file.getName()).matches()) {
                System.out.println("Matches jar");
                list.add(file);
            }

        }
    }

}
