package commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.util.Random;


public class ChonomoCommand extends Command{

    public ChonomoCommand() {
        this.aliases = new String[]{"chonomo", "cho"};
        this.guildOnly = false;
    }

    @Override
    public void execute(Message message, Member member, String alias, String[] args) {
        File[] files = new File("Resources/Chonomo").listFiles();
        Random random = new Random();

        try {
            File file = files[random.nextInt(files.length)];

            message.getChannel().sendFile(file).queue();
        } catch (Exception e) {
            message.getChannel().sendMessage("Error getting file").queue();
        }

    }
}
