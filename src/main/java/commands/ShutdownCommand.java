package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        this.aliases = new String[] {"shutdown"};
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        message.getChannel().sendMessage("Bot shutting down").complete();

        System.exit(0);
    }
}
