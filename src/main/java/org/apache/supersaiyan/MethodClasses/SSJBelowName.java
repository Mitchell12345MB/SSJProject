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

public class SSJBelowName {

    private final SSJ ssj;

    public SSJBelowName(SSJ ssj) {
        this.ssj = ssj;
    }

    private final HashMap<UUID, SSJBelowName> players = new HashMap<>();

    public boolean hasBelowName(Player player) {

        return players.containsKey(player.getUniqueId());

    }

    public SSJBelowName createBelowName(Player player) {

        return new SSJBelowName(ssj, player);

    }

    public SSJBelowName removeBelowName(Player player) {

        return players.remove(player.getUniqueId());

    }

    private Scoreboard scoreboard;

    private Objective belowname;

    private SSJBelowName(SSJ ssj, Player player) {

        this.ssj = ssj;

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        belowname = scoreboard.registerNewObjective("belowname", "dummy");

        belowname.setDisplaySlot(DisplaySlot.BELOW_NAME);

        for (int i = 1; i <= 15; i++) {

            Team team = scoreboard.registerNewTeam("SLOT_" + i);

            team.addEntry(genEntry(i));

        }

        player.setScoreboard(scoreboard);

        players.put(player.getUniqueId(), this);

    }

    public void setSlot(int slot, String text) {

        Team team = scoreboard.getTeam("SLOT_" + slot);

        String entry = genEntry(slot);

        if (!scoreboard.getEntries().contains(entry)) {

            belowname.getScore(entry).setScore(slot);

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
