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

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import rocks.devonthe.inventoryprotect.data.DataProvider;
import rocks.devonthe.inventoryprotect.data.UserData;
import rocks.devonthe.inventoryprotect.permission.Permissions;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class ListCommand extends BaseCommand {

    private static final Text DESCRIPTION = Text.of("Use to list the saved inventories for a user.");
    private static final Text USER = Text.of("user");
    static CommandSpec commandSpec = CommandSpec.builder()
        .description(DESCRIPTION)
        .permission(Permissions.COMMAND_LIST)
        .arguments(GenericArguments.user(USER))
        .executor(new ListCommand())
        .build();

    static void register() {
        try {
            Sponge.getCommandManager().register(INSTANCE, commandSpec);
            LOGGER.debug("Registered command: InventoryProtect");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            LOGGER.error("Failed to register command: InventoryProtect");
        }
    }

    @Override public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        User user = args.<User>getOne(USER).orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Missing required user argument!")));
        UserData userData;
        try {
            userData = INSTANCE.getDataProvider().getUserData(user);
        } catch (IOException | ObjectMappingException e) {
            userData = new UserData(user);
        }

        List<Text> textList = Lists.newArrayList();

        if (userData.getInventoryData().isEmpty()) {
            textList.add(Text.of(user.getName(), " has no saved inventories!"));
        } else {
            userData.getInventoryData().forEach(data -> textList.add(Text.of(
                Text.builder("[X]").color(TextColors.RED), " ",
                Text.builder("[*]").onClick(TextActions.executeCallback(s -> {
                    if (s instanceof Player) {
                        ((Player) s).openInventory(data.getInventory(), INSTANCE.getCause());
                    }
                })), " ",
                data.getType(), " | ",
                data.getDate(), " | ",
                data.getLocation()
            )));
        }

        PaginationList.builder()
            .title(Text.of(TextColors.GRAY, user.getName(), "'s Saved Inventories"))
            .padding(Text.of(TextColors.GRAY, "-"))
            .contents(textList)
            .sendTo(src);
//        Sponge.getScheduler().createTaskBuilder()
//            .execute(sendInventoryList(src, user))
//            .async()
//            .submit(INSTANCE);
        return null;
    }

    private Consumer<Task> sendInventoryList(CommandSource src, User user) {
        return task -> {

        };
    }
}
