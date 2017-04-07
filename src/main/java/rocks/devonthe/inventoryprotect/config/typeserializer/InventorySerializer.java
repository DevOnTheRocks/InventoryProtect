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

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import rocks.devonthe.inventoryprotect.InventoryProtect;

import java.util.List;

public class InventorySerializer implements TypeSerializer<Inventory> {

    private final TypeToken<List<ItemStackSnapshot>> inv = new TypeToken<List<ItemStackSnapshot>>() {};

    @Override public Inventory deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        Inventory inventory = Inventory.builder()
            .forCarrier(Player.class)
            .build(InventoryProtect.getInstance());
        value.getValue(inv).forEach(s -> inventory.offer(s.createStack()));
        return inventory;
    }

    @Override public void serialize(TypeToken<?> type, Inventory obj, ConfigurationNode value) throws ObjectMappingException {
        List<ItemStackSnapshot> stacks = Lists.newArrayList();
        obj.slots().forEach(slot -> slot.poll().ifPresent(itemStack -> stacks.add(itemStack.createSnapshot())));
        value.setValue(inv, stacks);
    }
}
