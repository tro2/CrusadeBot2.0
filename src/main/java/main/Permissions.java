package main;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;


import commands.Command;
import setup.CommandNotEnabledException;
import setup.ConfigCommandsDatabase;
import setup.ConfigDatabase;
import setup.InvalidSetupException;

public class Permissions {

    public static boolean hasPermission(Member member, Command command) throws InvalidSetupException, CommandNotEnabledException {
        if (command.minRole.equals("botDaddy")) {
            return CrusadeBot.config.ownerId.equals(member.getId());
        }

        ConfigCommandsDatabase.addNewGuild(member.getGuild().getId());

        if (!isEnabled(member.getGuild().getId(), command.getClass().getSimpleName())) {
            throw new CommandNotEnabledException("Command is not enabled for this server");
        }

        String roleId = ConfigDatabase.retrieve(command.minRole + "RoleId", member.getGuild().getId());
        Role minRole = member.getGuild().getRoleById(roleId);

        if (minRole == null) {
            throw new InvalidSetupException("Role `" + command.minRole + "` is not initialized in the database");
        }

        return member.getRoles().stream().anyMatch(role -> role.getPosition() >= minRole.getPosition());
    }

    private static boolean isEnabled(String guildId, String commandName) {
        return ConfigCommandsDatabase.getCommandStatus(commandName, guildId);
    }
}
