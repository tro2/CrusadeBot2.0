package commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class LoucasCommand extends Command{

    public LoucasCommand() {
        this.aliases = new String[]{"loucas", "guguh"};
        this.guildOnly = false;
    }

    @Override
    public void execute(Message message, Member member, String alias, String[] args) {
        MessageBuilder builder = new MessageBuilder();

        builder.setContent("Wah wah, shut the fuck up you goblin child \uD83D\uDC80").setTTS(true);

        message.getChannel().sendMessage(builder.build()).queue();
        message.delete().queue();
    }
}
