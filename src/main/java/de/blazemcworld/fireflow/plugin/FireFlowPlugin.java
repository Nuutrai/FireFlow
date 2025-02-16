package de.blazemcworld.fireflow.plugin;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import org.apache.logging.log4j.Logger;

import java.io.File;

public abstract class FireFlowPlugin {

    private boolean isEnabled = false;
    private MinecraftServer server;
    private File file;
    private PluginDescription description;
    private File dataFolder;
    private PluginClassLoader pluginClassLoader;
    private File configFile;
    private Logger logger;

    public FireFlowPlugin() {
        if (this.getClass().getClassLoader() instanceof PluginClassLoader pluginClassLoader) {
            pluginClassLoader.init(this);
        } else {
            throw new IllegalStateException("JavaPlugin requires to be created by a valid classloader.");
        }
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public void init(MinecraftServer server, PluginDescription description, File dataFolder, File file, PluginClassLoader pluginClassLoader, Logger logger) {

        this.server = server;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.pluginClassLoader = pluginClassLoader;
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = logger; // Paper

    }

    public MinecraftServer getServer() {
        return server;
    }

    public File getFile() {
        return file;
    }

    public PluginDescription getDescription() {
        return description;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public File getConfigFile() {
        return configFile;
    }

    public Logger getLogger() {
        return logger;
    }

    public PluginClassLoader getPluginClassLoader() {
        return pluginClassLoader;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                try {
                    onEnable();
                } catch (Exception ignored) { }
            } else {
                onDisable();
            }
        }
    }
}
