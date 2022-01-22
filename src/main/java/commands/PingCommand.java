package commands;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.time.DurationFormatUtils;

import main.CrusadeBot;
import java.time.Duration;
import java.util.Date;

public class PingCommand extends Command {

    public PingCommand() {
        this.aliases = new String[] {"ping"};
        this.minRoleId = "almostRaidLeader";
    }

    @Override
    public void execute(Message message, Member member, String aliases, String[] args) {
        Duration duration = Duration.between(new Date(CrusadeBot.timeStarted).toInstant(), new Date(System.currentTimeMillis()).toInstant());

        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();

        String uptime = (days == 0 ? "" : days + " day" + (days == 1 ? " " : "s "))
                + (hours == 0 ? "" : hours + " hour" + (hours == 1 ? " " : "s "))
                + (minutes == 0 ? "" : minutes + " minute" + (minutes == 1 ? " " : "s "))
                + seconds + " second" + (seconds == 1 ? "" : "s");

        message.getChannel().sendMessage(
                "Ping: `" + message.getJDA().getGatewayPing() + " ms`\n" +
                "Uptime: `" + DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - CrusadeBot.timeStarted, true, false) + "`").queue();
    }
}
