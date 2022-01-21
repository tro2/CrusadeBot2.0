package main;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import setup.ConfigCommandsDatabase;
import setup.ConfigDatabase;

public class Listeners extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        System.out.println("New Guild Joined");

        ConfigDatabase.addNewGuild(event.getGuild().getId());
        ConfigCommandsDatabase.addNewGuild(event.getGuild().getId());
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        CrusadeBot.config.emoteServer = CrusadeBot.jda.getGuildById("842619102636015656"); //ES-H

        System.out.println("Bot Ready");
    }
}
