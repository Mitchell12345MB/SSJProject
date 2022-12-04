package org.apache.supersaiyan.MethodClasses;

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

                    ssj.getSSJPPC(ssj, p).loadUserFile();

                    ssj.getSSJPPC(ssj, p).saveUserFile();

                    ssj.getSSJConfigs().saveConfigs();

                }
            }
        }, 0, 20 * 10);
    }

    public void bpandEnergyMultiplier() {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

            if (!Bukkit.getOnlinePlayers().isEmpty()) {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    ssj.getSSJMethods().addEnergy(p);

                    ssj.getSSJMethods().multBP(p);

                }
            }
        }, 0, 20 * 10);
    }

    public void hologramFollowandUpdate() {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

            if (!Bukkit.getOnlinePlayers().isEmpty()) {

                for (Player p : Bukkit.getOnlinePlayers()) {

                }
            }
        }, 0, 20 * 10);
    }
}
