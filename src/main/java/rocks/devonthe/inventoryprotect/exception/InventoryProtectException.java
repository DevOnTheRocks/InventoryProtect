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

package rocks.devonthe.inventoryprotect.exception;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.TextMessageException;

public class InventoryProtectException extends TextMessageException {

    public InventoryProtectException(Type type, Text message) {
        super(Text.of(TextColors.RED, type.getName(), " : ", message));
    }

    public enum Type {
        NOSUCHUSER("NoSuchUser");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
