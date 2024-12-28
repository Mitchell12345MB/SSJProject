package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
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
    private static final HashMap<UUID, SSJScoreBoards> players = new HashMap<>();
    private Scoreboard scoreboard;
    private Objective sidebar;

    // Constructor for static manager instance
    @SuppressWarnings("deprecation")
    public SSJScoreBoards(SSJ ssj) {
        this.ssj = ssj;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebar = scoreboard.registerNewObjective("ssjstats", "dummy", ChatColor.GOLD + "SSJ Stats");
        this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    // Constructor for player-specific instances
    @SuppressWarnings("deprecation")
    private SSJScoreBoards(SSJ ssj, Player player) {
        this.ssj = ssj;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sidebar = scoreboard.registerNewObjective("ssjstats", "dummy", ChatColor.GOLD + "SSJ Stats");
        this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(this.scoreboard);
    }

    public boolean hasScore(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public SSJScoreBoards createScore(Player player) {
        SSJScoreBoards scoreBoard = new SSJScoreBoards(ssj, player);
        players.put(player.getUniqueId(), scoreBoard);
        return scoreBoard;
    }

    public void removeScore(Player player) {
        players.remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getSidebar() {
        return sidebar;
    }

    public static SSJScoreBoards getPlayerScoreboard(Player player) {
        return players.get(player.getUniqueId());
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
        if (team != null) {
            team.setPrefix(pre);
        }
        if (team != null) {
            team.setSuffix(suf);
        }
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