package commands;

import main.CrusadeBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.Arrays;
import java.util.List;

public class TestCommand extends Command {

    public TestCommand() {
        this.aliases = new String[]{"test"};
        this.minRoleId = "botDaddy";
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        List<Button> buttonList = Arrays.asList(Button.primary("0", "Click me!"),
                Button.danger("1", "Click me!"),
                Button.secondary("2", "Click me!"),
                Button.success("3", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        List<Button> buttonList2 = Arrays.asList(Button.primary("011", "Click me!"),
                Button.danger("113", "Click me!"),
                Button.secondary("213", "Click me!"),
                Button.success("313", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        List<Button> buttonList3 = Arrays.asList(Button.primary("0112", "Click me!"),
                Button.danger("1134", "Click me!"),
                Button.secondary("2134", "Click me!"),
                Button.success("3134", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        List<Button> buttonList4 = Arrays.asList(Button.primary("0113", "Click me!"),
                Button.danger("115", "Click me!"),
                Button.secondary("215", "Click me!"),
                Button.success("315", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        List<Button> buttonList5 = Arrays.asList(Button.primary("0114", "Click me!"),
                Button.danger("116", "Click me!"),
                Button.secondary("216", "Click me!"),
                Button.success("316", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        List<Button> buttonList6 = Arrays.asList(Button.primary("0115", "Click me!"),
                Button.danger("117", "Click me!"),
                Button.secondary("217", "Click me!"),
                Button.success("317", "Success!"),
                Button.link("https://www.youtube.com/", "Link!"));

        Message testMessage = message.getChannel().sendMessage("Message of the testing variety").setActionRows(
                ActionRow.of(buttonList),
                ActionRow.of(buttonList2),
                ActionRow.of(buttonList3),
                ActionRow.of(buttonList4),
                ActionRow.of(buttonList5)
        ).complete();

        CrusadeBot.waiter.waitForEvent(ButtonClickEvent.class, e ->
                        e.getChannel().equals(message.getChannel()) &&
                        e.getInteraction().getMember().equals(member) &&
                        ("testButtonPrimary").contains(e.getInteraction().getComponentId()), e -> {
                    System.out.println("Primary button clicked");
                });
    }

}
