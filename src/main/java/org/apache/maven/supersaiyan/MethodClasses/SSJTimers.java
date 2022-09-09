package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SSJTimers {

    private final SSJ ssj;

    public SSJTimers(SSJ ssj) {
        this.ssj = ssj;
    }

    public void saveTimer() {

            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

                if (!Bukkit.getOnlinePlayers().isEmpty()) {

                    for (Player p : Bukkit.getOnlinePlayers()) {

                        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

                        user.loadUserFile();

                        user.saveUserFile();

                        ssj.getSSJmethods().callScoreboard(p);

                        ssj.getSSJXPB().addEnergy(p);
                    }
                }
            }, 0, 20 * 10);
    }
}
