package main;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;


import commands.Command;
import org.json.simple.JSONObject;
import setup.*;

import java.util.List;

public class Permissions {

    public static boolean hasPermission(Member member, Command command) throws InvalidSetupException, CommandNotEnabledException {
        ConfigCommandsDatabase.addNewGuild(member.getGuild().getId());

        JSONObject commandData = ConfigCommandsDatabase.getCommandData(command.getClass().getSimpleName(), member.getGuild().getId());

        if(commandData == null || commandData.isEmpty()) {
            throw new InvalidSetupException("Setup error, command data returned null");
        }

        String roleId = getMinRoleId(commandData);
        List<String> roleExceptions = getRoleExceptions(commandData);

        if (roleId.equals("-1")) {
            return member.getId().equals(CrusadeBot.config.ownerId);
        }

        if (!isEnabled(commandData)) {
            throw new CommandNotEnabledException("Command is not enabled for this server");
        }

        Role minRole = member.getGuild().getRoleById(roleId);

        if (minRole == null) {
            throw new InvalidSetupException("Minimum role id for this command is not valid: '" + roleId + "'");
        }
        else if(roleExceptions.size() == 0) {
            return member.getRoles().stream().anyMatch(role -> role.getPosition() >= minRole.getPosition());
        }

        return member.getRoles().stream().anyMatch(role -> roleExceptions.contains(role.getId()));
    }

    private static boolean isEnabled(JSONObject commandData) {
        return (boolean) commandData.get("isEnabled");
    }

    private static String getMinRoleId(JSONObject commandData) {
        return commandData.get("minRoleId").toString();
    }

    private static List<String> getRoleExceptions(JSONObject commandData) throws InvalidSetupException {
        try {
            return (List<String>) commandData.get("roleExceptions");
        } catch (Exception e) {
            throw new InvalidSetupException("Role Exceptions for this command are invalid");
        }
    }
}
