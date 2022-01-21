package commands;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


import javax.annotation.Nonnull;
import main.CrusadeBot;
import main.Permissions;
import setup.CommandNotEnabledException;
import setup.ConfigDatabase;
import setup.InvalidSetupException;
import java.util.Arrays;
import java.util.List;

import static main.CrusadeBot.commandManager;
import static main.CrusadeBot.config;

public class CommandListener extends ListenerAdapter {

    public final List<String> excludedGuildIdList = Arrays.asList("771984493484965888", "842619102636015656");

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        ConfigDatabase.addNewGuild(event.getGuild().getId());

        String prefix = ConfigDatabase.getPrefix(event.getGuild().getId());

        if (!event.getMessage().getContentRaw().startsWith(prefix))
            return;

        String[] split = event.getMessage().getContentRaw().split(" ");
        String alias = split[0].toLowerCase().replace(prefix, "");
        String[] args = Arrays.copyOfRange(split, 1, split.length);

        Command command;
        if ((command = commandManager.getCommand(alias)) != null) {
            if (event.getAuthor().getId().equals(config.ownerId)) {
                command.execute(event.getMessage(), event.getMember(), alias, args);

                return;
            }

            try {
                if (Permissions.hasPermission(event.getMember(), command)) { //add other command checks here
                    command.execute(event.getMessage(), event.getMember(), alias, args);
                }
            } catch (InvalidSetupException | CommandNotEnabledException e) {
                event.getChannel().sendMessage(e.getMessage()).queue();
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String[] split = event.getMessage().getContentRaw().split(" ");
        String alias = split[0].toLowerCase().replaceAll("[^A-Za-z]", "");
        String[] args = Arrays.copyOfRange(split, 1, split.length);

        Command command;
        if ((command = commandManager.getCommand(alias)) != null) {
            if (command.guildOnly) {
                event.getChannel().sendMessage("Command can only be used inside a server").queue();

                return;
            }

            List<Guild> guildList =  event.getAuthor().getMutualGuilds();

            if (guildList.size() == 0) {
                event.getChannel().sendMessage("Error, no mutual guilds").queue();
            }
            else if (guildList.size() == 1) {
                if (event.getAuthor().getId().equals(CrusadeBot.config.ownerId)) {
                    command.execute(event.getMessage(), guildList.get(0).getMember(event.getAuthor()), alias, args);

                    return;
                }

                try {
                    if (Permissions.hasPermission(guildList.get(0).getMember(event.getAuthor()), command)) { //add other command checks here
                        command.execute(event.getMessage(), guildList.get(0).getMember(event.getAuthor()), alias, args);
                    }
                }  catch (InvalidSetupException | CommandNotEnabledException e) {
                    event.getChannel().sendMessage(e.getMessage()).queue();
                }
            }
            else {
                determineGuild(guildList, event, command, alias, args);
            }
        }
    }

    private void determineGuild(List<Guild> guildList, PrivateMessageReceivedEvent event, Command command, String alias, String[] args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
        embedBuilder.setTitle("Select the guild you wish to perform the command from: ");

        guildList = guildList.stream().filter(g -> {
            if(excludedGuildIdList.contains(g.getId())) {
                return false;
            }

            if (event.getAuthor().getId().equals(CrusadeBot.config.ownerId)) {
                return true;
            }

            try {
                return Permissions.hasPermission(g.getMember(event.getAuthor()), command); //add other command checks here
            } catch (InvalidSetupException | CommandNotEnabledException e) {
                return false;
            }
        }).toList();

        if(guildList.size() == 1) {
            command.execute(event.getMessage(), guildList.get(0).getMember(event.getAuthor()), alias, args);
            return;
        }

        String desc = "";
        String emoteList = "";

        for (int i = 0; i < guildList.size(); i++) {
             desc = desc.concat((i+1) + "\uFE0F\u20E3 - **" + guildList.get(i).getName() + "**\n");
             emoteList = emoteList.concat((i+1) + "\uFE0F\u20E3|");
        }

        embedBuilder.setDescription(desc);

        Message message = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();

        for (int i = 0; i < guildList.size(); i++) {
            message.addReaction((i+1) + "\uFE0F\u20E3").complete();
        }

        String finalEmoteList = emoteList;
        List<Guild> finalGuildList = guildList;
        CrusadeBot.waiter.waitForEvent(PrivateMessageReactionAddEvent.class, e ->
                    !e.getUser().isBot() &&
                    e.getMessageId().equals(message.getId()) &&
                    e.getReactionEmote().isEmoji() &&
                    (finalEmoteList).contains(e.getReactionEmote().getEmoji()), e -> {
            String reaction = e.getReactionEmote().getEmoji();
            int num = Integer.parseInt(Arrays.stream(finalEmoteList.split("|")).filter(reaction::contains).findFirst().orElse("11").split("/")[0]);

            if (num < 11) {
                embedBuilder.setTitle("Guild selected: " + finalGuildList.get(num-1).getName());
                embedBuilder.setDescription("");
                message.editMessageEmbeds(embedBuilder.build()).complete();

                command.execute(event.getMessage(), finalGuildList.get(num-1).getMember(event.getAuthor()), alias, args);
            }
            else {
                event.getChannel().sendMessage("Unable to determine server, try the command from one").queue();
            }
        });
    }
}