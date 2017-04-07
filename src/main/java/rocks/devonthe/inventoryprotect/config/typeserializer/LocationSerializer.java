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

package rocks.devonthe.inventoryprotect.config.typeserializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class LocationSerializer implements TypeSerializer<Location<?>> {

    @Override public Location<?> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return new Location<>(
            Sponge.getServer().getWorld(UUID.fromString(value.getNode("world").getString())).get(),
            value.getNode("x").getInt(),
            value.getNode("y").getInt(),
            value.getNode("z").getInt()
        );
    }

    @Override public void serialize(TypeToken<?> type, Location<?> obj, ConfigurationNode value) throws ObjectMappingException {
        value.getNode("world").setValue(obj.getExtent().getUniqueId());
        value.getNode("x").setValue(obj.getBlockX());
        value.getNode("y").setValue(obj.getBlockY());
        value.getNode("z").setValue(obj.getBlockZ());
    }
}
