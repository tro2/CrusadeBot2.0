package main;

import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import setup.ConfigDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static String getJarContainingFolder(Class aclass) throws Exception {
        CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

        File jarFile;

        if (codeSource.getLocation() != null) {
            jarFile = new File(codeSource.getLocation().toURI());
        }
        else {
            String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
            String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
            jarFilePath = URLDecoder.decode(jarFilePath, StandardCharsets.UTF_8);
            jarFile = new File(jarFilePath);
        }
        return jarFile.getParentFile().getAbsolutePath();
    }

    public static Member findMember(String query, Guild guild) {
        try {
            return guild.getMemberById(Long.parseLong(query));
        } catch (NumberFormatException e) {}

        query = query.toLowerCase();

        for (Member member : guild.getMembers().stream().filter(member -> member.getNickname() != null).collect(Collectors.toList())) {
            String memberName = member.getEffectiveName().replaceAll("[^A-Za-z|]", "").toLowerCase();
            String memberTag = member.getUser().getAsTag().toLowerCase();

            if (Arrays.asList(memberName.split("[|]")).contains(query) || memberTag.contains(query))
                return member;
        }

        return null;
    }

    public static String getHighestLeadingRoleName(Member member) {
        List<String> leaderRoleNames = new ArrayList<>(ConfigDatabase.getConfigColumnNames(member.getGuild().getId()).stream().filter(s -> s.endsWith("LeaderRoleId")).map(n -> n.replace("RoleId", "")).toList());

        leaderRoleNames.sort(Comparator.comparingInt(o -> -member.getGuild().getRoleById(ConfigDatabase.retrieve(o + "RoleId", member.getGuild().getId())).getPosition()));

        for (String str : leaderRoleNames) {
            if(member.getRoles().stream().anyMatch(r -> r.getId().equals(ConfigDatabase.retrieve(str + "RoleId", member.getGuild().getId())))) {
                return str;
            }
        }

        return null;
    }

    public static boolean hasSufficientLeadingRole(Member member, String roleName) {
        Role memberHighestLeadingRole = member.getGuild().getRoleById(ConfigDatabase.retrieve(getHighestLeadingRoleName(member) + "RoleId", member.getGuild().getId()));

        Role requiredLeadingRole = member.getGuild().getRoleById(ConfigDatabase.retrieve(roleName + "RoleId", member.getGuild().getId()));

        return memberHighestLeadingRole.getPosition() >= requiredLeadingRole.getPosition();
    }

    public static String getHighestFeedbackableRoleName(Member member) {
        List<String> leaderRoleNames = Arrays.asList("trialRaidLeader", "almostRaidLeader", "raidLeader");

        leaderRoleNames.sort(Comparator.comparingInt(o -> -member.getGuild().getRoleById(ConfigDatabase.retrieve(o + "RoleId", member.getGuild().getId())).getPosition()));

        for (String str : leaderRoleNames) {
            if(member.getRoles().stream().anyMatch(r -> r.getId().equals(ConfigDatabase.retrieve(str + "RoleId", member.getGuild().getId())))) {
                return str;
            }
        }

        return null;
    }

    public static List<Emote> getEmotes (String... emoteNames) {
        List<Emote> emotes = new ArrayList<>();

        for (String name : emoteNames) {
            emotes.add(CrusadeBot.config.emoteServer.getEmotes().stream().filter(e -> e.getName().equalsIgnoreCase(name)).findAny().orElse(null));
        }

        return emotes;
    }
}
