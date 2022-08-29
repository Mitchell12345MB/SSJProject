package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class SSJScoreboard {

    private final SSJ ssj;

    public SSJScoreboard(SSJ ssj) {
        this.ssj = ssj;
    }

    public void callScoreboard(Player p) {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

            ScoreboardManager manager = Bukkit.getScoreboardManager();

            final Scoreboard board = manager.getNewScoreboard();

            final Objective objective = board.registerNewObjective("test", "dummy");

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName(ChatColor.RED + "Your current stats");

            Score score = objective.getScore("Score10");

            score.setScore(10);

            Score score1 = objective.getScore("Score9");

            score1.setScore(9);

            Score score2 = objective.getScore("Score8");

            score2.setScore(8);

            Score score3 = objective.getScore("§6Colors");

            score3.setScore(7);

            p.setScoreboard(board);

        },0, 20 * 10);

    }

    public void callBelowName(Player p) {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

            ScoreboardManager manager = Bukkit.getScoreboardManager();

            final Scoreboard board = manager.getNewScoreboard();

            final Objective objective = board.registerNewObjective("Belowname", "dummy");

            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);

            objective.setDisplayName(ChatColor.RED + "YourBelowName");

        },0, 20 * 10);

    }

}
