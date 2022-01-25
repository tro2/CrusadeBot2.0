package commands;

import main.CrusadeBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.util.Arrays;
import java.util.List;

public class TestCommand extends Command {

    public TestCommand() {
        this.aliases = new String[]{"test"};
        this.guildOnly = true;
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        
    }
}
