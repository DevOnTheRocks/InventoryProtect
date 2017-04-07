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
 *
 * *************************************************************************
 *
 * This class is has been adapted from Nucleus, which is licensed under the MIT License (MIT).
 * The original file can be found at https://github.com/NucleusPowered/Nucleus/blob/sponge-api/5/src/main/
 * java/io/github/nucleuspowered/nucleus/configurate/typeserialisers/NucleusItemStackSnapshotSerialiser.java
 */

package rocks.devonthe.inventoryprotect.config.typeserializer;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.List;
import java.util.stream.Collectors;

public class ItemStackSnapshotSerializer implements TypeSerializer<ItemStackSnapshot> {

    private final TypeToken<ItemStackSnapshot> iss = TypeToken.of(ItemStackSnapshot.class);

    @Override public ItemStackSnapshot deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        // Process enchantments, temporary fix before Sponge gets a more general fix in.
        boolean emptyEnchant = false;
        ConfigurationNode ench = value.getNode("UnsafeData", "ench");
        if (!ench.isVirtual()) {
            List<? extends ConfigurationNode> enchantments = ench.getChildrenList();
            if (enchantments.isEmpty()) {
                // Remove empty enchantment list.
                value.getNode("UnsafeData").removeChild("ench");
            } else {
                enchantments.forEach(x -> {
                    try {
                        short id = Short.parseShort(x.getNode("id").getString());
                        short lvl = Short.parseShort(x.getNode("lvl").getString());

                        x.getNode("id").setValue(id);
                        x.getNode("lvl").setValue(lvl);
                    } catch (NumberFormatException e) {
                        x.setValue(null);
                    }
                });
            }
        }

        ConfigurationNode data = value.getNode("Data");
        if (!data.isVirtual() && data.hasListChildren()) {
            List<? extends ConfigurationNode> n = data.getChildrenList().stream()
                .filter(x ->
                    !x.getNode("DataClass").getString().endsWith("SpongeEnchantmentData")
                        || (!x.getNode("ManipulatorData", "ItemEnchantments").isVirtual() && x.getNode("ManipulatorData", "ItemEnchantments")
                        .hasListChildren()))
                .collect(Collectors.toList());
            emptyEnchant = n.size() != data.getChildrenList().size();

            if (emptyEnchant) {
                if (n.isEmpty()) {
                    value.removeChild("Data");
                } else {
                    value.getNode("Data").setValue(n);
                }
            }
        }

        ItemStackSnapshot snapshot;
        try {
            snapshot = value.getValue(iss);
        } catch (Exception e) {
            return ItemStackSnapshot.NONE;
        }

        if (emptyEnchant) {
            ItemStack is = snapshot.createStack();
            is.offer(Keys.ITEM_ENCHANTMENTS, Lists.newArrayList());
            return is.createSnapshot();
        }

        if (snapshot.get(Keys.ITEM_ENCHANTMENTS).isPresent()) {
            ItemStack is = snapshot.createStack();
            // Reset the data.
            is.offer(Keys.ITEM_ENCHANTMENTS, snapshot.get(Keys.ITEM_ENCHANTMENTS).get());
            return is.createSnapshot();
        }

        return snapshot;
    }

    @Override public void serialize(TypeToken<?> type, ItemStackSnapshot obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(iss, obj);
    }
}
