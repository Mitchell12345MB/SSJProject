package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.supersaiyan.SSJ;
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

                        ssj.getSSJConfigs().saveConfigs();

                        ssj.getSSJMethods().addEnergy(p);

                        ssj.getSSJMethods().multBP(p);

                        ssj.getSSJMethods().callScoreboard(p);
                    }
                }
            }, 0, 10 * 10);
    }
}
