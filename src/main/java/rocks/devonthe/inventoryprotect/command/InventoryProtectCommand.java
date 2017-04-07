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

package rocks.devonthe.inventoryprotect.command;

import static rocks.devonthe.inventoryprotect.PluginInfo.VERSION;

import com.google.common.collect.Lists;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class InventoryProtectCommand extends BaseCommand {

    private static final Text DESCRIPTION = Text.of("Use to execute or view help info for Inventory Protect commands.");
    private static final Text HELP = Text.of("help");
    private static CommandSpec commandSpec = CommandSpec.builder()
        .description(DESCRIPTION)
        .child(ListCommand.commandSpec, "list")
        .arguments(GenericArguments.optionalWeak(GenericArguments.onlyOne(GenericArguments.literal(HELP, "?"))))
        .executor(new InventoryProtectCommand())
        .build();

    public static void register() {
        try {
            Sponge.getCommandManager().register(INSTANCE, commandSpec, "inventoryprotect", "ip");
            LOGGER.debug("Registered command: InventoryProtect");
            registerSubCommands();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            LOGGER.error("Failed to register command: InventoryProtect");
        }
    }

    private static void registerSubCommands() {
        ListCommand.register();
    }

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> helpText = Lists.newArrayList();

        if (helpText.isEmpty()) {
            src.sendMessage(Text.of("Inventory Protect ", VERSION));
        } else {
            PaginationList.builder()
                .title(Text.of(TextColors.GRAY, "Inventory Protect"))
                .padding(Text.of(TextColors.GRAY, "-"))
                .contents(helpText)
                .sendTo(src);
        }

        return CommandResult.success();
    }
}
