package commands;

import main.CrusadeBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimestampCommand extends Command {

    public TimestampCommand() {
        this.aliases = new String[]{"time", "t"};
        this.guildOnly = false;
    }

    @Override
    public void execute(Message message, Member member, String alias, String[] args) {

        if (args.length == 0) {
            message.reply(Instant.now().getEpochSecond() + "").queue();
        }
        else if (("new").contains(args[0])) {
            launchTimestampCreation(message, member);
        }
        else if (("formats").contains(args[0])) {
            showExampleFormats(message);
        }
        else if(("offset").contains(args[0])) {

        }


    }

    private void launchTimestampCreation(Message message, Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        long time = Instant.now().getEpochSecond();
        String mapLink = "https://www.timeanddate.com/time/map/";
        boolean isDaylightSavings = TimeZone.getDefault().inDaylightTime(new Date());
        String desc;
        Message initial;

        embedBuilder.setTitle("Unix Timestamp Creator");
        embedBuilder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
        embedBuilder.setFooter("Hit `Cancel` to close creator");

        if (!isDaylightSavings) {
            desc = "**Current time:** <t:" + time + ":F>\n" +
                    """
                    ```
                    Enter your offset from UTC
                                    
                    Common offsets:
                    
                    -5 = Eastern Standard Time (EST)
                    -8 = Pacific Standard Time (PST)
                    +1 = Central European Time (CET)
                                    
                    ```
                    """;
        }
        else {
            desc = "**Current time:** <t:" + time + ":F>\n" +
                    """
                    ```
                    Enter your offset from UTC
                                    
                    Common offsets:
                                    
                    ```
                    """;
        }

        embedBuilder.setDescription(desc);

        initial = message.replyEmbeds(embedBuilder.build()).setActionRow(Button.danger("cancel", "Cancel")).complete();

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                e.getMessageId().equals(initial.getId()) &&
                        e.getMember().equals(member) &&
                        ("cancel").contains(e.getComponentId()), e -> {

                e.deferEdit().queue();

                initial.delete().queue();
        }, 5, TimeUnit.MINUTES, null);
    }

    private void collectTimestampFormat(Message initial, Member member, EmbedBuilder embedBuilder, String time) {
        SelectionMenu menu = SelectionMenu
                .create("formatSelect")
                .setPlaceholder("Pick output format")
                .addOption("<t:" + time + ":f>", "f", "Short-form Date Time")
                .addOption("<t:" + time + ":F>", "F", "Long-form Date Time")
                .addOption("<t:" + time + ":d>", "d", "Short-form Date")
                .addOption("<t:" + time + ":D>", "D", "Long-form Date")
                .addOption("<t:" + time + ":t>", "t", "Short-form Time")
                .addOption("<t:" + time + ":T>", "T", "Long-form Time")
                .addOption("<t:" + time + ":R>", "R", "Relative Time")
                .build();

        SelectionMenu menu2 = SelectionMenu
                .create("formatSelect2")
                .setPlaceholder("Pick output format")
                .addOption("Short-form Date Time", "f", "<t:" + time + ":f>")
                .addOption("Long-form Date Time", "F","<t:" + time + ":F>")
                .addOption("Short-form Date", "d", "<t:" + time + ":d>")
                .addOption("Long-form Date", "D", "<t:" + time + ":D>")
                .addOption("Short-form Time", "t", "<t:" + time + ":t>")
                .addOption("Long-form Time", "T", "<t:" + time + ":T>")
                .addOption("Relative Time", "R", "<t:" + time + ":R>")
                .build();

        embedBuilder.appendDescription("```Select format```");

        initial.editMessageEmbeds(embedBuilder.build()).setActionRows(ActionRow.of(menu2), ActionRow.of(Button.danger("cancel", "Cancel"))).queue();
    }

    private void showExampleFormats(Message message) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        long time = Instant.now().getEpochSecond();
        long relativeExampleTime = time + 7200;

        String desc = "`<t:" + time + ":f>` - " + "**<t:" + time + ":f>**\n\n" +
                "`<t:" + time + ":F>` - " + "**<t:" + time + ":F>**\n\n" +
                "`<t:" + time + ":d>` - " + "**<t:" + time + ":d>**\n\n" +
                "`<t:" + time + ":D>` - " + "**<t:" + time + ":D>**\n\n" +
                "`<t:" + time + ":t>` - " + "**<t:" + time + ":t>**\n\n" +
                "`<t:" + time + ":T>` - " + "**<t:" + time + ":T>**\n\n" +
                "`<t:" + relativeExampleTime + ":R>` - " + "**<t:" + relativeExampleTime + ":R>**\n\n";


        embedBuilder.setTitle("Unix Timestamp Formatting");
        embedBuilder.setColor(CrusadeBot.CRUSADEBOT_PURPLE);
        embedBuilder.setDescription(desc);

        message.replyEmbeds(embedBuilder.build()).queue();
    }

    private long createTimestamp(long time, long secondsOffset) {
        return time + secondsOffset;
    }
}
