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

package rocks.devonthe.inventoryprotect.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import rocks.devonthe.inventoryprotect.InventoryProtect;

import java.io.IOException;
import java.nio.file.Files;

public class ConfigManager {

    private final InventoryProtect INSTANCE = InventoryProtect.getInstance();
    private final Logger LOGGER = INSTANCE.getLogger();

    private ObjectMapper<GlobalConfig>.BoundInstance configMapper;
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public ConfigManager(ConfigurationLoader<CommentedConfigurationNode> loader) {
        if (!Files.exists(INSTANCE.getConfigDir())) {
            INSTANCE.getConfigDir().toFile().mkdir();
        }

        this.loader = loader;
        try {
            this.configMapper = ObjectMapper.forObject(INSTANCE.getConfig());
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        this.load();
    }

    /**
     * Saves the serialized config to file
     */
    public void save() {
        try {
            SimpleCommentedConfigurationNode out = SimpleCommentedConfigurationNode.root();
            this.configMapper.serialize(out);
            this.loader.save(out);
        } catch (ObjectMappingException | IOException e) {
            LOGGER.error(String.format("Failed to save config.\r\n %s", e.getMessage()));
        }
    }

    /**
     * Loads the configs into serialized objects, for the configMapper
     */
    public void load() {
        try {
            this.configMapper.populate(this.loader.load());
        } catch (ObjectMappingException | IOException e) {
            LOGGER.error(String.format("Failed to load config.\r\n %s", e.getMessage()));
        }
    }
}
