package commands;

import main.CrusadeBot;
import main.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import setup.ConfigDatabase;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class HeadcountCommand extends Command {

    public HeadcountCommand() {
        this.aliases = new String[] {"headcount", "hc"};
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        if (args.length == 0) {
            message.reply("You need some arguments to go with that command").queue();
            return;
        }

        String type = args[0];

        List<String> argsList = new ArrayList<>(Arrays.asList(args));

        argsList.remove(0);

        String dungeon = argsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ", "", ""));

        if (dungeon.length() > 1000) {
            dungeon = dungeon.substring(0, 988);
        }

        switch (type) {
            case "void", "v" -> voidHeadcount(message, member);
            case "o3", "oryx", "oryx3", "o" -> o3Headcount(message, member);
            case "cult", "c" -> cultHeadcount(message, member);
            case "fungal", "f" -> fungalHeadcount(message, member);
            case "shatters", "s" -> shattersHeadcount(message, member);
            case "nest", "n" -> nestHeadcount(message, member);
            case "event", "e" -> eventHeadcount(message, member, dungeon);
        }
    }

    private void voidHeadcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("void_entity", "lh_key", "vial", "paladin", "warrior", "knight", "curse", "armorbreak", "trickster");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Void",
                "React with key/vial if you have one \n Otherwise react to applicable choices below",
                new Color(72, 0, 181));

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void o3Headcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("oryx_3", "helm_rune", "sword_rune", "shield_rune", "inc", "paladin", "warrior", "curse", "armorbreak", "trickster");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Oryx 3",
                "React with rune/inc if you have one \n Otherwise react to applicable choices below",
                new Color(37, 0, 208));

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void cultHeadcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("malus", "lh_Key", "planewalker");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Cult",
                "React with key if you have one \n Otherwise react to applicable choices below",
                new Color(231, 0, 0));

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void fungalHeadcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("fungal_cavern", "fungal_Key", "paladin", "warrior", "knight", "mystic", "armorbreak", "slow", "daze", "trickster");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Fungal Cavern",
                "React with key if you have one \n Otherwise react to applicable choices below",
                Color.CYAN);

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void shattersHeadcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("shatters_portal", "shatters_key", "paladin", "warrior", "knight", "curse", "armorbreak", "slow", "planewalker");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Shatters",
                "React with key if you have one\nOtherwise react to applicable choices below\n[Shatters puzzle solver](https://osanc.net/solver)",
                new Color(1, 92, 33));

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void nestHeadcount(Message initial, Member member) {
        List<Emote> emoteList = Utils.getEmotes("nest_portal", "nest_key", "paladin", "warrior", "knight", "curse", "armorbreak", "slow", "daze");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for Nest",
                "React with key if you have one \n Otherwise react to applicable choices below",
                new Color(255, 148, 34));

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private void eventHeadcount(Message initial, Member member, String dungeon) {
        List<Emote> emoteList = Utils.getEmotes("event_portal", "event_key");

        MessageBuilder messageBuilder = buildHeadcount(
                "Headcount for " + (dungeon.isEmpty() ? "Random Dungeons" : dungeon),
                "React with key if you have one \n Otherwise just vibe",
                CrusadeBot.CRUSADEBOT_PURPLE);

        sendHeadcount(initial, member, messageBuilder, emoteList);
    }

    private MessageBuilder buildHeadcount(String title, String description, Color color)
    {
        MessageBuilder messageBuilder = new MessageBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        messageBuilder.append("@here ").append(title);

        embedBuilder.setTitle(title);
        embedBuilder.setColor(color);
        embedBuilder.setDescription(description);
        embedBuilder.setTimestamp(new Date().toInstant());

        messageBuilder.setEmbeds(embedBuilder.build());

        return messageBuilder;
    }

    private void sendHeadcount(Message initial, Member member, MessageBuilder messageBuilder, List<Emote> emoteList) {
        TextChannel headCountChannel;
        Message headcount;

        try {
            headCountChannel = member.getGuild().getTextChannelById(ConfigDatabase.retrieve("rsaChannelId", member.getGuild().getId()));

            headcount = headCountChannel.sendMessage(messageBuilder.build()).complete();
        } catch (Exception e) {
            initial.reply("Unable to retrieve headcount channel").queue();
            return;
        }

        for (Emote emote : emoteList)
        {
            headcount.addReaction(emote).queue();
        }
    }
}
