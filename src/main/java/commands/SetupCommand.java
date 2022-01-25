package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import main.CrusadeBot;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ComponentLayout;
import org.apache.commons.collections4.iterators.SingletonIterator;
import org.json.simple.JSONObject;
import setup.ConfigCommandsDatabase;
import setup.ConfigDatabase;

import javax.swing.*;
import java.sql.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SetupCommand extends Command {

    public SetupCommand() {
        this.aliases = new String[] {"setup"};
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        List<String> answers = Arrays.asList("prefix", "channelIds", "commands");
        List<Button> buttonList;
        Message initial;


        embedBuilder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
        embedBuilder.setAuthor(member.getEffectiveName(), null, member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTitle("CrusadeBot Setup");
        embedBuilder.setFooter("Click 'cancel' to cancel setup");
        embedBuilder.setDescription("""
                Select category number
                ```
                1 - prefix
                2 - channelIds
                3 - commands
                ```""");

        buttonList = Arrays.asList(
                Button.secondary("prefix", "Prefix"),
                Button.secondary("channelIds", "Channel ID's"),
                Button.secondary("commands", "Commands"),
                Button.danger("setupCancel", "Cancel")
        );

        initial = message.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(buttonList).complete();

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMember().equals(member) &&
                e.getMessageId().equals(initial.getId()) &&
                "setupCancel".contains(e.getComponentId()), e -> {

            e.deferEdit().queue();

            cancelSetup(initial, embedBuilder);

        }, 30L, TimeUnit.MINUTES, null);

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMember().equals(member) &&
                e.getMessageId().equals(initial.getId()) &&
                answers.contains(e.getComponentId()), e -> {
            e.deferEdit().queue();
            String id = e.getComponentId();

            switch (id) {
                case "prefix" -> changePrefix(initial, embedBuilder, member);
                case "channelIds" -> changeChannelIds(initial, embedBuilder, member);
                case "commands" -> changeCommands(initial, embedBuilder, member);
            }
        }, 10L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    /*public void execute(Message message, Member member, String aliases, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        List<String> answers = Arrays.asList("prefix", "roleIds", "channelIds", "commands");
        List<Button> buttonListLower;
        Message initial;


        embedBuilder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
        embedBuilder.setAuthor(member.getEffectiveName(), null, member.getUser().getEffectiveAvatarUrl());
        embedBuilder.setTitle("CrusadeBot Setup");
        embedBuilder.setFooter("Click 'cancel' to cancel setup");
        embedBuilder.setDescription("""
                Select category number
                ```
                1 - prefix
                2 - roleIds
                3 - channelIds
                4 - commands
                ```""");

        buttonListLower = Arrays.asList(
                Button.secondary("prefix", "Prefix"),
                Button.secondary("roleIds", "Role ID's"),
                Button.secondary("channelIds", "Channel ID's"),
                Button.secondary("commands", "Commands"),
                Button.danger("setupCancel", "Cancel")
        );

        initial = message.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(buttonListLower).complete();

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMember().equals(member) &&
                        e.getMessageId().equals(initial.getId()) &&
                        "setupCancel".contains(e.getComponentId()), e -> {
            e.deferEdit().queue();

            cancelSetup(initial, embedBuilder);

        });

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMember().equals(member) &&
                        e.getMessageId().equals(initial.getId()) &&
                        answers.contains(e.getComponentId()), e -> {
            e.deferEdit().queue();
            String id = e.getComponentId();

            switch (id) {
                case "prefix" -> changePrefix(initial, embedBuilder, member);
                case "roleIds" -> changeRoleIds(initial, embedBuilder, member);
                case "channelIds" -> changeChannelIds(initial, embedBuilder, member);
                case "commands" -> changeCommands(initial, embedBuilder, member);
            }
        });
    }*/



    private void changePrefix(Message initial, EmbedBuilder embedBuilder, Member member) {
        embedBuilder.setTitle("Setup - Prefix");
        embedBuilder.setDescription(
                "Enter new prefix\n" +
                        "```\n" +
                        "current prefix: " + ConfigDatabase.getPrefix(initial.getGuild().getId()) + "\n" +
                        "```");

        initial.editMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("setupCancel", "Cancel")).queue();


        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                        m.getMember().equals(member), m -> {
            String reply = m.getMessage().getContentRaw().trim().split(" ")[0];

            ConfigDatabase.setString("prefix", reply, initial.getGuild().getId());

            embedBuilder.setTitle("Setup - Prefix - Complete");
            embedBuilder.setDescription(
                    "Changes made\n" +
                            "```\n" +
                            "prefix -> " + ConfigDatabase.getPrefix(initial.getGuild().getId()) + "\n" +
                            "```");
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageEmbeds(embedBuilder.build()).queue();
        }, 10L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }


    /*private void changeRoleIds(Message initial, EmbedBuilder embedBuilder, Member member) {
        List<String> roleIdNames = ConfigDatabase.getConfigColumnNames(member.getGuild().getId());

        roleIdNames = roleIdNames.stream().filter(n -> n.endsWith("RoleId")).map(n -> n.replace("RoleId", "")).toList();

        List<String> answers = new ArrayList<>(Arrays.asList("all", "cancel"));
        String desc =
                "Select role id to change, 'all' to set all\n" +
                "```\n";

        for(int i = 0; i < roleIdNames.size(); i++) {
             desc = desc.concat((i + 1) + " - " + roleIdNames.get(i) + ": " + ConfigDatabase.retrieve(roleIdNames.get(i) + "RoleId", initial.getGuild().getId()) + "\n");
             answers.add((i+1) + "");
        }

        desc = desc.concat("```");

        embedBuilder.setTitle("Setup - Role Id's");
        embedBuilder.setDescription(desc);

        initial.editMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("setupCancel", "Cancel")).queue();

        List<String> finalRoleIdNames = roleIdNames;
        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member) &&
                answers.contains(m.getMessage().getContentRaw().trim().split(" ")[0]), m -> {
            String reply = m.getMessage().getContentRaw().trim().split(" ")[0];

            if (reply.equals("all")) {
                changeAllRoleIds(initial, embedBuilder, member, finalRoleIdNames);
                return;
            }

            int num = Integer.parseInt(reply) - 1;

            changeOneRoleId(initial, embedBuilder, member, finalRoleIdNames.get(num));
        });
    }

    private void changeOneRoleId(Message initial, EmbedBuilder embedBuilder, Member member, String roleIdName) {
        embedBuilder.setTitle("Setup - Role Id's - " + roleIdName);
        embedBuilder.setDescription(
                "Enter new role id\n" +
                "```\n" +
                roleIdName + ": " + ConfigDatabase.retrieve(roleIdName + "RoleId", initial.getGuild().getId()) + "\n" +
                "```"
        );

        initial.editMessageEmbeds(embedBuilder.build()).queue();

        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member), m -> {
            String newId = m.getMessage().getContentRaw().trim().split(" ")[0];

            if (newId.equals("cancel")) {
                cancelSetup(initial, embedBuilder);
                return;
            }

            ConfigDatabase.setString(roleIdName + "RoleId", newId, initial.getGuild().getId());

            embedBuilder.setTitle("Setup - Role Id's - " + roleIdName + " - Complete");
            embedBuilder.setDescription(
                    "Changes made\n" +
                            "```\n" +
                            roleIdName + " -> " + ConfigDatabase.retrieve(roleIdName + "RoleId", initial.getGuild().getId()) + "\n" +
                            "```");
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    private void changeAllRoleIds(Message initial, EmbedBuilder embedBuilder, Member member, List<String> roleIdNames) {
        String desc =
                "Copy and re-enter filled out form\n" +
                "```\n" +
                "BULKROLEIDSET\n";
        for (String roleName : roleIdNames) {
            desc = desc.concat(roleName + "=\n");
        }
        desc = desc.concat("```");

        roleIdNames = roleIdNames.stream().map(n -> n.concat("RoleId")).toList();

        embedBuilder.setTitle("Setup - Role Id's - Set All");

        initial.editMessageEmbeds(embedBuilder.build()).queue();

        embedBuilder.setDescription(desc);

        Message formatSender = initial.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();

        List<String> finalRoleIdNames = roleIdNames;
        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member) &&
                m.getMessage().getContentRaw().startsWith("BULKROLEIDSET\n") ||
                m.getMessage().getContentRaw().toLowerCase().startsWith("cancel"), m -> {
            formatSender.delete().queue();

            String response = m.getMessage().getContentRaw().replace("BULKROLEIDSET\n", "");

            List<String> answers = Arrays.stream(response.split("\\n")).toList();

            answers = answers.stream().map(n -> n.split("=")[1]).toList();

            ConfigDatabase.setBulkLongs(finalRoleIdNames, answers, member.getGuild().getId());

            String newDesc =
                    "Changes made\n" +
                    "```\n";

            for (int i = 0; i < finalRoleIdNames.size() && i < answers.size(); i++) {
                newDesc = newDesc.concat(finalRoleIdNames.get(i).replace("RoleId", "") + " -> " + answers.get(i) + "\n");
            }

            newDesc = newDesc.concat("```");

            embedBuilder.setTitle("Setup - Role Id's - Set All - complete");
            embedBuilder.setDescription(newDesc);
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }*/


    private void changeChannelIds(Message initial, EmbedBuilder embedBuilder, Member member) {
        List<String> channelIdNames = ConfigDatabase.getConfigColumnNames(member.getGuild().getId());

        channelIdNames = channelIdNames.stream().filter(n -> n.endsWith("ChannelId")).map(n -> n.replace("ChannelId", "")).toList();

        List<String> answers = new ArrayList<>(Arrays.asList("all", "cancel"));
        String desc =
                "Select channel id to change, 'all' to set all\n" +
                "```\n";

        for(int i = 0; i < channelIdNames.size(); i++) {
            desc = desc.concat((i + 1) + " - " + channelIdNames.get(i) + ": " + ConfigDatabase.retrieve(channelIdNames.get(i) + "ChannelId", initial.getGuild().getId()) + "\n");
            answers.add((i+1) + "");
        }

        desc = desc.concat("```");

        embedBuilder.setTitle("Setup - Channel Id's");
        embedBuilder.setDescription(desc);

        initial.editMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("setupCancel", "Cancel")).queue();

        List<String> finalChannelIdNames = channelIdNames;
        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member) &&
                answers.contains(m.getMessage().getContentRaw().trim().split(" ")[0]), m -> {
            String reply = m.getMessage().getContentRaw().trim().split(" ")[0];

            if (reply.equals("all")) {
                changeAllChannelIds(initial, embedBuilder, member, finalChannelIdNames);
                return;
            }

            int num = Integer.parseInt(reply) - 1;

            changeOneChannelId(initial, embedBuilder, member, finalChannelIdNames.get(num));
        }, 5L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    private void changeOneChannelId(Message initial, EmbedBuilder embedBuilder, Member member, String channelIdName) {
        embedBuilder.setTitle("Setup - Channel Id's - " + channelIdName);
        embedBuilder.setDescription(
                "Enter new channel id\n" +
                "```\n" +
                channelIdName + ": " + ConfigDatabase.retrieve(channelIdName + "ChannelId", initial.getGuild().getId()) +  "\n" +
                "```"
        );

        initial.editMessageEmbeds(embedBuilder.build()).queue();

        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member), m -> {
            String newId = m.getMessage().getContentRaw().trim().split(" ")[0];

            ConfigDatabase.setString(channelIdName + "ChannelId", newId, initial.getGuild().getId());

            embedBuilder.setTitle("Setup - Channel Id's - " + channelIdName + " - Complete");
            embedBuilder.setDescription(
                    "Changes made\n" +
                    "```\n" +
                    channelIdName + " -> " + ConfigDatabase.retrieve(channelIdName + "ChannelId", initial.getGuild().getId()) + "\n" +
                    "```");
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageComponents().complete();
            initial.editMessageEmbeds(embedBuilder.build()).queue();
        }, 5L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    private void changeAllChannelIds(Message initial, EmbedBuilder embedBuilder, Member member, List<String> channelIdNames) {
        String desc =
                "Copy and re-enter filled out form\n" +
                "```\n" +
                "BULKCHANNELIDSET\n";
        for (String channelName : channelIdNames) {
            desc = desc.concat(channelName + "=\n");
        }
        desc = desc.concat("```");

        channelIdNames = channelIdNames.stream().map(n -> n.concat("ChannelId")).toList();

        embedBuilder.setTitle("Setup - Channel Id's - Set All");

        initial.editMessageEmbeds(embedBuilder.build()).queue();

        embedBuilder.setDescription(desc);

        Message formatSender = initial.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();

        List<String> finalChannelIdNames = channelIdNames;
        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member) &&
                m.getMessage().getContentRaw().startsWith("BULKCHANNELIDSET\n") ||
                m.getMessage().getContentRaw().toLowerCase().startsWith("cancel"), m -> {
            formatSender.delete().queue();

            String response = m.getMessage().getContentRaw().replace("BULKCHANNELIDSET\n", "");

            List<String> answers = Arrays.stream(response.split("\\n")).toList();

            answers = answers.stream().map(n -> n.split("=")[1]).toList();

            ConfigDatabase.setBulkLongs(finalChannelIdNames, answers, member.getGuild().getId());

            String newDesc =
                    "Changes made\n" +
                    "```\n";

            for (int i = 0; i < finalChannelIdNames.size() && i < answers.size(); i++) {
                newDesc = newDesc.concat(finalChannelIdNames.get(i).replace("ChannelId", "") + " -> " + answers.get(i) + "\n");
            }

            newDesc = newDesc.concat("```");

            embedBuilder.setTitle("Setup - Channel Id's - Set All - complete");
            embedBuilder.setDescription(newDesc);
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageComponents().complete();
            initial.editMessageEmbeds(embedBuilder.build()).queue();
        }, 5L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }


    private void changeCommands(Message initial, EmbedBuilder embedBuilder, Member member) {
        List<String> commandNames = ConfigCommandsDatabase.getCommandColumnNames();
        List<JSONObject> commandsData = new ArrayList<>();

        for (int i = 0; i < commandNames.size(); i++) {
            commandsData.add(ConfigCommandsDatabase.getCommandData(commandNames.get(i), member.getGuild().getId()));
        }

        List<String> answers = new ArrayList<>(Arrays.asList("all"));
        String desc =
                "Select command to change" +
                "```\n";

        for(int i = 0; i < commandNames.size(); i++) {
            desc = desc.concat((i + 1) + " - " + commandNames.get(i) + ": "
                    + (commandsData.get(i).get("isEnabled").equals("true") ? "Enabled" : "Disabled")
                    + "\n");

            answers.add(String.valueOf(i+1));

        }

        desc = desc.concat("```");

        embedBuilder.setTitle("Setup - Commands");
        embedBuilder.setDescription(desc);

        initial.editMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("setupCancel", "Cancel")).queue();

        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member) &&
                answers.contains(m.getMessage().getContentRaw().trim().split(" ")[0]), m -> {
            String reply = m.getMessage().getContentRaw().trim().split(" ")[0];

            int num = Integer.parseInt(reply) - 1;

            changeOneCommand(initial, embedBuilder, member, commandNames.get(num), commandsData.get(num));
        }, 5L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    private void changeOneCommand(Message initial, EmbedBuilder embedBuilder, Member member, String commandName, JSONObject commandData) {
        List<String> answers = Arrays.asList("isEnabled", "minRoleId", "roleExceptions");
        List<Button> buttonList = Arrays.asList(
                Button.secondary("isEnabled", "Status"),
                Button.secondary("minRoleId", "Min Role"),
                Button.secondary("roleExceptions", "Exceptions"),
                Button.danger("setupCancel", "Cancel")
        );;

        embedBuilder.setTitle("Setup - Commands - " + commandName);
        embedBuilder.setDescription(
                "Select data value to change\n" +
                "```\n" +
                "1 - " + commandName + ": " + (commandData.get("isEnabled").equals("true") ? "Enabled" : "Disabled") + "\n" +
                "2 - minRoleId: " + commandData.get("minRoleId") + "\n" +
                "3 - roleExceptions: " + commandData.get("roleExceptions") + "\n" +
                "\n```"
        );

        initial.editMessageEmbeds(embedBuilder.build()).queue();
        initial.editMessageComponents(ActionRow.of(buttonList)).queue();

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMember().equals(member) &&
                e.getMessageId().equals(initial.getId()) &&
                answers.contains(e.getComponentId()), e -> {
            e.deferEdit().queue();
            String id = e.getComponentId();

            switch(id) {
                case "isEnabled" -> changeCommandStatus(initial, embedBuilder, commandName, commandData);
                case "minRoleId" -> changeCommandRoleId(initial, embedBuilder, member, commandName, commandData);
                case "roleExceptions" -> changeCommandRoleExceptions(initial, embedBuilder, member, commandName, commandData);
            }

        }, 10L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    private void changeCommandStatus(Message initial, EmbedBuilder embedBuilder, String commandName, JSONObject commandData) {
        commandData.replace("isEnabled", (commandData.get("isEnabled").equals("false") ? "true" : "false"));

        ConfigCommandsDatabase.setCommandData(commandName, commandData, initial.getGuild().getId());

        embedBuilder.setTitle("Setup - Commands - " + commandName + " - Complete");
        embedBuilder.setDescription(
            "Changes made\n" +
            "```\n" +
            commandName + " -> " + (commandData.get("isEnabled").equals("true") ? "Enabled" : "Disabled") + "\n" +
            "```");
        embedBuilder.setFooter("");
        embedBuilder.setTimestamp(new Date().toInstant());

        initial.editMessageComponents().complete();
        initial.editMessageEmbeds(embedBuilder.build()).queue();
    }

    private void changeCommandRoleId(Message initial, EmbedBuilder embedBuilder, Member member, String commandName, JSONObject commandData) {
        embedBuilder.setTitle("Setup - Commands - " + commandName + " - Minimum Role");
        embedBuilder.setDescription(
                "Enter new role id\n" +
                "```\n" +
                commandName + ": " + commandData.get("minRoleId") +  "\n" +
                "```"
        );

        initial.editMessageEmbeds(embedBuilder.build()).setActionRow(Button.danger("setupCancel", "Cancel")).queue();

        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
                m.getChannel().equals(initial.getChannel()) &&
                m.getMember().equals(member), m -> {
            String newId = m.getMessage().getContentRaw().trim().split(" ")[0];

            commandData.replace("minRoleId", newId);
            ConfigCommandsDatabase.setCommandData(commandName, commandData, member.getGuild().getId());

            embedBuilder.setTitle("Setup - Commands - Minimum Role - " + commandName + " - Complete");
            embedBuilder.setDescription(
                    "Changes made\n" +
                    "```\n" +
                    commandName + " -> " + commandData.get("minRoleId") + "\n" +
                    "```");
            embedBuilder.setFooter("");
            embedBuilder.setTimestamp(new Date().toInstant());

            initial.editMessageComponents().complete();
            initial.editMessageEmbeds(embedBuilder.build()).queue();
        }, 5L, TimeUnit.MINUTES, () -> timeoutSetup(initial, embedBuilder));
    }

    private void changeCommandRoleExceptions(Message initial, EmbedBuilder embedBuilder, Member member, String commandName, JSONObject commandData) {
        initial.reply("Unable to edit role exceptions, feature not yet released.").queue();
        cancelSetup(initial, embedBuilder);
    }

//    private void changeAllCommandStatuses(Message initial, EmbedBuilder embedBuilder, Member member, List<String> commandNames) {
//        String desc =
//                "Copy and re-enter filled out form\n" +
//                "```\n" +
//                "BULKCOMMANDSTATUSSET\n";
//        for (String commandStatus : commandNames) {
//            desc = desc.concat(commandStatus + "=\n");
//        }
//        desc = desc.concat("```");
//
//        embedBuilder.setTitle("Setup - Commands - Set All");
//
//        initial.editMessageEmbeds(embedBuilder.build()).queue();
//
//        embedBuilder.setDescription(desc);
//
//        Message formatSender = initial.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
//
//        CrusadeBot.waiter.waitForEvent(GuildMessageReceivedEvent.class, m ->
//                m.getChannel().equals(initial.getChannel()) &&
//                m.getMember().equals(member) &&
//                m.getMessage().getContentRaw().startsWith("BULKCOMMANDSTATUSSET\n") ||
//                m.getMessage().getContentRaw().toLowerCase().startsWith("cancel"), m -> {
//            formatSender.delete().queue();
//
//            String response = m.getMessage().getContentRaw().replace("BULKCOMMANDSTATUSSET\n", "");
//
//            List<Boolean> answers = Arrays.stream(response.split("\\n")).map(n -> Boolean.parseBoolean(n.split("=")[1])).toList();
//
//            ConfigCommandsDatabase.changeBulkCommandStatuses(commandNames, answers, initial.getGuild().getId());
//
//            String newDesc =
//                    "Changes made\n" +
//                    "```\n";
//
//            for (int i = 0; i < commandNames.size() && i < answers.size(); i++) {
//                newDesc = newDesc.concat(commandNames.get(i) + " -> " + answers.get(i) + "\n");
//            }
//
//            newDesc = newDesc.concat("```");
//
//            embedBuilder.setTitle("Setup - Commands - Set All - complete");
//            embedBuilder.setDescription(newDesc);
//            embedBuilder.setFooter("");
//            embedBuilder.setTimestamp(new Date().toInstant());
//
//            initial.editMessageEmbeds(embedBuilder.build()).queue();
//        });
//    }

    private void timeoutSetup(Message initial, EmbedBuilder embedBuilder) {
        embedBuilder.setTitle("Setup timed out");
        embedBuilder.setDescription("");
        embedBuilder.setFooter("");
        embedBuilder.setTimestamp(new Date().toInstant());

        initial.editMessageComponents().complete();
        initial.editMessageEmbeds(embedBuilder.build()).complete();
    }

    private void cancelSetup(Message initial, EmbedBuilder embedBuilder) {
        embedBuilder.setTitle("Setup cancelled");
        embedBuilder.setDescription("");
        embedBuilder.setFooter("");
        embedBuilder.setTimestamp(new Date().toInstant());

        initial.editMessageComponents().complete();
        initial.editMessageEmbeds(embedBuilder.build()).complete();
    }
}
