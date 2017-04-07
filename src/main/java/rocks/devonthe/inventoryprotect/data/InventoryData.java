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

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.time.Instant;

@ConfigSerializable
public class InventoryData {

    @Setting private InventoryType type;
    @Setting private Long date;
    @Setting private Location<World> location;
    @Setting private Inventory inventory;

    private InventoryData() {

    }

    private InventoryData(InventoryType type, Instant date, Location<World> location, Inventory inventory) {
        this.type = type;
        this.date = date.getEpochSecond();
        this.location = location;
        this.inventory = inventory;
    }

    public InventoryData(Player player, InventoryType type, Instant date) {
        this(type, date, player.getLocation(), player.getInventory());
    }

    public InventoryType getType() {
        return type;
    }

    public Instant getDate() {
        return Instant.ofEpochSecond(date);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Location<World> getLocation() {
        return location;
    }
}
