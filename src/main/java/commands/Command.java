package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    protected String[] aliases;
    public boolean guildOnly = false;

    public abstract void execute(Message message, Member member, String aliases, String[] args);

    public MessageEmbed getHelpEmbed() {
        return null;
    }
}
