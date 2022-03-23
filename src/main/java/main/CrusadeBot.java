package main;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import setup.Config;
import setup.ConfigDatabase;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class CrusadeBot {

    public static JDA jda;
    public static Config config;
    public static CommandManager commandManager;
    public static JDABuilder builder;
    public static Long timeStarted;
    public static EventWaiter waiter;

    public static final Color CRUSADEBOT_PURPLE = new Color(150, 160, 255);

    public static void main(String[] args) {

        //Checking to make sure Config JSON file is there
        try {
            config = new Config();
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("Failed to start bot, config file invalid");

            System.exit(-1);
        }

        //Checking to make sure Database is there
        if(!ConfigDatabase.testPingDatabase()) {
            System.out.println("Failed to start bot, database invalid");

            System.exit(-1);
        }

        //setting up JDABuilder
        builder = JDABuilder.createDefault(config.token,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_PRESENCES
        );

        builder.setMemberCachePolicy(MemberCachePolicy.ALL);

        //Setting up commands
        commandManager = new CommandManager();
        addCommands();

        //Setting up EventListeners and EventWaiter
        waiter = new EventWaiter();

        addEventListeners();

        try {
            jda = builder.build();

            timeStarted = System.currentTimeMillis();
        } catch (LoginException e) {
            e.printStackTrace();

            System.out.println("Unable to log in, shutting down bot");

            System.exit(-1);
        }
    }

    public static void addCommands() {
        commandManager.add(new PingCommand());
        commandManager.add(new TestCommand());
        commandManager.add(new ShutdownCommand());
        commandManager.add(new SetupCommand());
        commandManager.add(new HeadcountCommand());
        commandManager.add(new TimestampCommand());
        commandManager.add(new ChonomoCommand());
        commandManager.add(new TrodaireCommand());
        commandManager.add(new LoucasCommand());

    }

    public static void addEventListeners() {
        builder.addEventListeners(
                waiter,
                new CommandListener(),
                new Listeners());
    }
}
