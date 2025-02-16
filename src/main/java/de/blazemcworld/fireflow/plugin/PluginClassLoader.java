package de.blazemcworld.fireflow.plugin;

import com.google.common.base.Preconditions;
import de.blazemcworld.fireflow.FireFlow;
import de.blazemcworld.fireflow.plugin.exceptions.InvalidPluginException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public final class PluginClassLoader extends URLClassLoader {

    private final PluginDescription description;
    private final File dataFolder;
    private final File file;
    final FireFlowPlugin plugin;
    private FireFlowPlugin pluginInit;
    private IllegalStateException pluginState;

    public PluginClassLoader(ClassLoader parent, PluginDescription description, File dataFolder, File file) throws InvalidPluginException, MalformedURLException {
        super(file.getName(), new URL[]{file.toURI().toURL()}, parent);
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;

        Class<?> jarClass;
        try {
            jarClass = Class.forName(description.getMain(), true, this);
        } catch (ClassNotFoundException e) {
            throw new InvalidPluginException(String.format("Cannot find main class '%s'", description.getMain()), e);
        }

        Class<? extends FireFlowPlugin> pluginClass;
        try {
            pluginClass = jarClass.asSubclass(FireFlowPlugin.class);
        } catch (ClassCastException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must extend FireFlowPlugin", ex);
        }

        Constructor<? extends FireFlowPlugin> pluginConstructor;
        try {
            pluginConstructor = pluginClass.getDeclaredConstructor();
        } catch (NoSuchMethodException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must have a public no-args constructor", ex);
        }

        try {
            plugin = pluginConstructor.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' constructor must be public", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("main class `" + description.getMain() + "' must not be abstract", ex);
        } catch (IllegalArgumentException ex) {
            throw new InvalidPluginException("Could not invoke main class `" + description.getMain() + "' constructor", ex);
        } catch (ExceptionInInitializerError | InvocationTargetException ex) {
            throw new InvalidPluginException("Exception initializing main class `" + description.getMain() + "'", ex);
        }
    }

    public synchronized void initialize(@NotNull FireFlowPlugin fireFlowPlugin) { // Paper
        Preconditions.checkArgument(fireFlowPlugin != null, "Initializing plugin cannot be null");
        Preconditions.checkArgument(fireFlowPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = fireFlowPlugin;

        fireFlowPlugin.init(FireFlow.getServer(), description, dataFolder, file, this, FireFlow.LOGGER); // Paper
    }

    public void init(FireFlowPlugin fireFlowPlugin) {
        this.initialize(fireFlowPlugin);
    }
}
