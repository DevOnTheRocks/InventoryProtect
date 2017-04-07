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

package rocks.devonthe.inventoryprotect.listener;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.scheduler.Task;
import rocks.devonthe.inventoryprotect.data.InventoryData;
import rocks.devonthe.inventoryprotect.data.InventoryType;
import rocks.devonthe.inventoryprotect.data.UserData;

import java.io.IOException;
import java.time.Instant;
import java.util.function.Consumer;

public class DeathListener extends BaseListener {

    @Listener
    public void onPlayerDeath(DestructEntityEvent.Death event, @Getter("getTargetEntity") Player player) {
        if (!INSTANCE.getConfig().isSaveDeathInventories()) {
            return;
        }
        // Save Inventory Async
        Sponge.getScheduler()
            .createTaskBuilder()
            .execute(saveDeathInventory(player))
            .async()
            .submit(INSTANCE);
    }

    private Consumer<Task> saveDeathInventory(Player player) {
        return task -> {
            LOGGER.info(String.format("%s has died! Saving their death inventory.", player.getName()));
            UserData userData;
            try {
                userData = INSTANCE.getDataProvider().getUserData(player);
            } catch (IOException | ObjectMappingException e) {
                userData = new UserData(player);
            }

            userData.saveInventoryData(new InventoryData(player, InventoryType.DEATH, Instant.now()));

            try {
                INSTANCE.getDataProvider().saveUserData(userData);
                LOGGER.info(String.format("Saved %s's death inventory!", player.getName()));
            } catch (ObjectMappingException | IOException e) {
                e.printStackTrace();
            }
        };
    }
}
