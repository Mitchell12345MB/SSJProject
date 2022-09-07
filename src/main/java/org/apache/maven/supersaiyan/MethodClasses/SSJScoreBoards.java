package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

public class SSJScoreBoards {

    private final SSJ ssj;

    public SSJScoreBoards(SSJ ssj) {
        this.ssj = ssj;
    }

    private final HashMap<UUID, SSJScoreBoards> players = new HashMap<>();

    public boolean hasScore(Player player) {

        return players.containsKey(player.getUniqueId());

    }

    public SSJScoreBoards createScore(Player player) {

        return new SSJScoreBoards(ssj, player);

    }

    public SSJScoreBoards removeScore(Player player) {

        return players.remove(player.getUniqueId());

    }

    private Scoreboard scoreboard;
    private Objective sidebar;

    private SSJScoreBoards(SSJ ssj, Player player) {

        this.ssj = ssj;

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("sidebar", "dummy");

        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        for (int i = 1; i <= 15; i++) {

            Team team = scoreboard.registerNewTeam("SLOT_" + i);

            team.addEntry(genEntry(i));

        }

        player.setScoreboard(scoreboard);

        players.put(player.getUniqueId(), this);

    }

    public void setTitle(String title) {

        title = ChatColor.translateAlternateColorCodes('&', title);

        sidebar.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);

    }

    public void setSlot(int slot, String text) {

        Team team = scoreboard.getTeam("SLOT_" + slot);

        String entry = genEntry(slot);

        if (!scoreboard.getEntries().contains(entry)) {

            sidebar.getScore(entry).setScore(slot);

        }

        text = ChatColor.translateAlternateColorCodes('&', text);

        String pre = getFirstSplit(text);

        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));

        team.setPrefix(pre);

        team.setSuffix(suf);

    }

    private String genEntry(int slot) {

        return ChatColor.values()[slot].toString();

    }

    private String getFirstSplit(String s) {

        return s.length() > 16 ? s.substring(0, 16) : s;

    }

    private String getSecondSplit(String s) {

        if (s.length() > 32) {

            s = s.substring(0, 32);

        }

        return s.length() > 16 ? s.substring(16) : "";
    }
}
