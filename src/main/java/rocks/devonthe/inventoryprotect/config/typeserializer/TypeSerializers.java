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
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class TypeSerializers {

    private static TypeSerializers instance;
    private static TypeSerializerCollection typeSerializerCollection = null;

    private TypeSerializers() {
        instance = this;

        TypeSerializerCollection tsc = ConfigurationOptions.defaults().getSerializers();

        tsc.registerType(TypeToken.of(Inventory.class), new InventorySerializer());
        tsc.registerType(TypeToken.of(ItemStackSnapshot.class), new ItemStackSnapshotSerializer());
        tsc.registerPredicate(
            typeToken -> Location.class.isAssignableFrom(typeToken.getRawType()),
            new LocationSerializer()
        );
        TypeSerializers.typeSerializerCollection = tsc;
    }

    public static void register() {
        if (instance == null) {
            new TypeSerializers();
        }
    }

    public static TypeSerializerCollection getTypeSerializerCollection() {
        return typeSerializerCollection;
    }
}
