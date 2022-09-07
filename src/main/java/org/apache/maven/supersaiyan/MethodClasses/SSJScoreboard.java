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

    public void callScoreboard() {

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {

                ScoreboardManager manager = Bukkit.getScoreboardManager();

                final Scoreboard board = manager.getNewScoreboard();

                final Objective objective = board.registerNewObjective("test", "dummy");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                objective.setDisplayName(ChatColor.RED + "Current Stats");

                Score score = objective.getScore("Level");

                score.setScore(ssj.getSSJpPc().getpConfig(p).getInt("Level"));

                Score score1 = objective.getScore("BP");

                score1.setScore(ssj.getSSJpPc().getpConfig(p).getInt("Battle_Power"));

                Score score2 = objective.getScore("Energy");

                score2.setScore(ssj.getSSJpPc().getpConfig(p).getInt("Energy"));

                Score score3 = objective.getScore("Current Form: " + ssj.getSSJpPc().getpConfig(p).getString("Form"));

                score3.setScore(0);

                p.setScoreboard(board);

        }


    }

    public void callBelowName() {

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {

           // Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {



           // }, 0, 20 * 10);

        }


        //Map<UUID, Scoreboard> scoreboards = new HashMap<>();


        //public void ccallScoreboard(Player p) {

        //  if (!scoreboards.containsKey(p.getUniqueId())) {

        //    Scoreboard newScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        //    newScoreboard.registerNewObjective("Stats", "dummy");

        //    scoreboards.put(p.getUniqueId(), newScoreboard);


        // Scoreboard scoreboard = scoreboards.get(p.getUniqueId());

        //Objective objective = scoreboard.registerNewObjective("Stats", "dummy");

        // objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // objective.setDisplayName(ChatColor.RED + "Your current stats");

        // Score score = objective.getScore("Level");

        // score.setScore(ssj.getpPc().getpConfig(p).getInt("Level"));

        // Score score1 = objective.getScore("Battle Power");

        // score1.setScore(ssj.getpPc().getpConfig(p).getInt("Battle_Power"));

        //Score score2 = objective.getScore("Energy");

        // score2.setScore(ssj.getpPc().getpConfig(p).getInt("Energy"));

        // Score score3 = objective.getScore("Current Form: " + ssj.getpPc().getpConfig(p).getString("Form"));

        // score3.setScore(0);

        // p.setScoreboard(scoreboard);

    }
}
