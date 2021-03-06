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

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import rocks.devonthe.inventoryprotect.InventoryProtect;
import rocks.devonthe.inventoryprotect.exception.InventoryProtectException;

import java.util.List;
import java.util.UUID;

@ConfigSerializable
public class UserData {

    private final static InventoryProtect INSTANCE = InventoryProtect.getInstance();

    @Setting(value = "id")
    private UUID id;
    @Setting(value = "inventories")
    private List<InventoryData> inventoryData = Lists.newArrayList();

    private UserData() {
        // Required for Configurate
    }

    public UserData(User user) {
        this(user.getUniqueId());
    }

    public UserData(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return id;
    }

    public User getUser() throws InventoryProtectException {
        return INSTANCE.getGame().getServiceManager().provideUnchecked(UserStorageService.class).get(id)
            .orElseThrow(() -> new InventoryProtectException(
                InventoryProtectException.Type.NOSUCHUSER,
                Text.of("Unable to load user (", id, ") from storage."))
            );
    }

    public List<InventoryData> getInventoryData() {
        return inventoryData;
    }

    public void saveInventoryData(InventoryData data) {
        inventoryData.add(data);
    }

    public void deleteInventoryData(InventoryData data) {
        inventoryData.remove(data);
    }
}
