/*
 * InventoryProtect - A Sponge Plugin
 * Copyright (C) 2017 DevOnTheRocks
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * InventoryProtect is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with InventoryProtect.  If not, see <http://www.gnu.org/licenses/>.
 */

package rocks.devonthe.inventoryprotect;

import static rocks.devonthe.inventoryprotect.PluginInfo.AUTHORS;
import static rocks.devonthe.inventoryprotect.PluginInfo.ID;
import static rocks.devonthe.inventoryprotect.PluginInfo.NAME;
import static rocks.devonthe.inventoryprotect.PluginInfo.VERSION;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameAboutToStartServerEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import rocks.devonthe.inventoryprotect.command.InventoryProtectCommand;
import rocks.devonthe.inventoryprotect.config.ConfigManager;
import rocks.devonthe.inventoryprotect.config.GlobalConfig;
import rocks.devonthe.inventoryprotect.config.typeserializer.TypeSerializers;
import rocks.devonthe.inventoryprotect.data.DataProvider;
import rocks.devonthe.inventoryprotect.listener.ConnectionListener;
import rocks.devonthe.inventoryprotect.listener.DeathListener;

import java.nio.file.Path;

@Plugin(
    id = ID,
    name = NAME,
    version = VERSION,
    authors = {AUTHORS}
)
public class InventoryProtect {

    private static InventoryProtect instance;

    @Inject private Logger logger;
    @Inject private PluginContainer pluginContainer;
    @Inject private Game game;
    @Inject @ConfigDir(sharedRoot = false)
    private Path configDir;
    @Inject @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private ConfigManager configManager;
    private GlobalConfig config;
    private DataProvider dataProvider;

    public static InventoryProtect getInstance() {
        return instance;
    }

    @Listener
    public void onPostInitialization(GamePostInitializationEvent event) {
        getLogger().info(String.format("%s %s is initializing...", NAME, VERSION));

        instance = this;
    }

    @Listener
    public void onAboutToStart(GameAboutToStartServerEvent event) {
        config = new GlobalConfig();
        configManager = new ConfigManager(configLoader);
        configManager.save();
        dataProvider = new DataProvider();

        game.getEventManager().registerListeners(this, new ConnectionListener());
        game.getEventManager().registerListeners(this, new DeathListener());

        TypeSerializers.register();
        InventoryProtectCommand.register();
    }

    @Listener
    public void onGameStopping(GameStoppingServerEvent event) {
        getLogger().info(String.format("%S %S is stopping...", NAME, VERSION));
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        reload();
    }

    public void reload() {
        configManager.load();
    }

    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }

    public Cause getCause() {
        return Cause.source(pluginContainer).build();
    }

    public Game getGame() {
        return game;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public GlobalConfig getConfig() {
        return this.config;
    }

    public void setConfig(GlobalConfig config) {
        this.config = config;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public Logger getLogger() {
        return logger;
    }
}
