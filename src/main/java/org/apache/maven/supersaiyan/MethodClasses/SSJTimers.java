package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SSJTimers {

    private final SSJ ssj;

    public SSJTimers(SSJ ssj) {
        this.ssj = ssj;
    }

    public void saveTimer() {

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {

            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

                ssj.getSSJpPc().callSavePlayerConfig(p);

                ssj.getSSJmethods().callScoreboard(p);

            }, 0, 20 * 10);
        }
    }
}
