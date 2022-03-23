package commands;

import main.CrusadeBot;
import main.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.crypto.ExemptionMechanism;
import javax.swing.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestCommand extends Command {

    public TestCommand() {
        this.aliases = new String[]{"test"};
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String alias, String[] args) {

//      String url = "https://www.timeanddate.com/time/map/";

//        List<Emote> emoteList = Utils.getEmotes("inc", "helm_rune", "sword_rune", "shield_rune");
//
//        List<Emoji> emojiList = emoteList.stream().map(Emoji::fromEmote).toList();
//
//        SelectionMenu menu = SelectionMenu
//                .create("testId")
//                .setPlaceholder("Select what you are bringing")
//                .addOption("Incantation", "inc", "Wine Cellar Incantation", Emoji.fromEmote(emoteList.get(0)))
//                .addOption("Helmet Rune", "helm", "Rune of the Lost Halls", Emoji.fromEmote(emoteList.get(1)))
//                .addOption("Sword Rune", "sword", "Rune of the Nest", Emoji.fromEmote(emoteList.get(2)))
//                .addOption("Shield Rune", "shield", "Rune of the gigachads", Emoji.fromEmote(emoteList.get(3)))
//                .setRequiredRange(2,4).build();
//
//        List<Button> buttonList = Arrays.asList(
//                Button.secondary("inc", emojiList.get(0)),
//                Button.secondary("sword", emojiList.get(1)),
//                Button.secondary("shield", emojiList.get(2)),
//                Button.secondary("helm", emojiList.get(3)));
//
//        EmbedBuilder builder = new EmbedBuilder();
//
//        builder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
//        builder.setAuthor(null, null, member.getUser().getAvatarUrl());
//        builder.setImage("https://preview.redd.it/1c89shmtzla61.png?width=313&format=png&auto=webp&s=a49d71fb75b9e286b630ca1e63bffcb15840a307");
//        builder.setTitle("Come get clapped by o3 while doky tries to stun");
//        builder.setDescription("""
//                **ETA: <t:1643159381:R>**
//
//                **Location: tbd**
//
//                <:inc:842619472748871731> - <@463436803978821655>
//                <:helm_rune:842619472786358312> - <@463436803978821655>
//                <:sword_rune:842619472824500224> - <@463436803978821655>
//                <:shield_rune:842619472782557195> - <@463436803978821655>""");
//
//        builder.setFooter("Paradise runs", member.getGuild().getIconUrl()).setTimestamp(new Date().toInstant());
//
//        Message message1 = message.replyEmbeds(builder.build()).setActionRows(ActionRow.of(buttonList)).complete();
//
//        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
//                e.getMember().equals(member) &&
//                e.getMessageId().equals(message1.getId()) &&
//                e.getComponentId().equals("inc"), e -> {
//            InteractionHook reply = e.deferReply(true).complete();
//
//            reply.editOriginal("Menu").setActionRow(menu).queue();
//
//        });






//        String content =
//                """
//                ***------------------------------------------------***
//                <:lightblue15:771984533481455626>
//                <:lightblue15:771984533481455626>
//                <:lightblue15:771984533481455626>           ***Yyuji's fat schlong o3's***
//                <:lightblue15:771984533481455626>           <:inc:842619472748871731> -
//                <:lightblue15:771984533481455626>           <:sword_rune:842619472824500224> -
//                <:lightblue15:771984533481455626>           <:shield_rune:842619472782557195> -
//                <:lightblue15:771984533481455626>           <:helm_rune:842619472786358312> -
//                <:lightblue15:771984533481455626>
//                <:lightblue15:771984533481455626>           **ETA: <t:1643159381:R>**
//                <:lightblue15:771984533481455626>
//                <:lightblue15:771984533481455626>
//                ***------------------------------------------------***
//                """;
//
//        message.getChannel().sendMessage(content).setActionRows(ActionRow.of(buttonList)).queue();
    }
}
