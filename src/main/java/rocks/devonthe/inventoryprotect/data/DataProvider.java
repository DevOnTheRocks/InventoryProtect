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

package rocks.devonthe.inventoryprotect.data;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.User;
import rocks.devonthe.inventoryprotect.InventoryProtect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataProvider {

    private final String dataPath = InventoryProtect.getInstance().getConfigDir() + File.separator + "data";
    private final File dataDir = Paths.get(dataPath).toFile();
    private final String userData = "%1$s%2$s%3$s.json";
    private ObjectMapper<UserData>.BoundInstance userMapper;
    private ObjectMapper<InventoryData>.BoundInstance inventoryMapper;
    private GsonConfigurationLoader loader;

    public DataProvider() {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public UserData getUserData(UUID id) throws ObjectMappingException, IOException {
        File file = Paths.get(String.format(userData, dataPath, File.separator, id.toString().toUpperCase())).toFile();
        if (!file.exists()) {
            createUserData(id);
        }

        GsonConfigurationLoader dataLoader = GsonConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode dataNode = dataLoader.load();

        return dataNode.getValue(TypeToken.of(UserData.class), new UserData(id));
    }

    public UserData getUserData(User user) throws IOException, ObjectMappingException {
        return getUserData(user.getUniqueId());
    }

    private void createUserData(UUID id) throws IOException, ObjectMappingException {
        InventoryProtect.getInstance().getLogger().info("Created new user data for " + id);
        File file = Paths.get(String.format(userData, dataPath, File.separator, id.toString().toUpperCase())).toFile();
        file.createNewFile();

        GsonConfigurationLoader dataLoader = GsonConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode dataNode = dataLoader.load();
        dataNode.setValue(TypeToken.of(UserData.class), new UserData(id));
        dataLoader.save(dataNode);
    }

    public void saveUserData(UserData data) throws ObjectMappingException, IOException {
        File file = Paths.get(String.format(userData, dataPath, File.separator, data.getUserId().toString().toUpperCase())).toFile();
        if (!file.exists()) {
            file.createNewFile();
        }

        GsonConfigurationLoader dataLoader = GsonConfigurationLoader.builder().setFile(file).build();
        ConfigurationNode dataNode = dataLoader.load();
        dataNode.setValue(TypeToken.of(UserData.class), data);
        dataLoader.save(dataNode);
    }
}
