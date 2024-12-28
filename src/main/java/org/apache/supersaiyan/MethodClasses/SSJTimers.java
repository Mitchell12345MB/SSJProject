package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SSJTimers{

    private final SSJ ssj;

    public SSJTimers(SSJ ssj) {

        this.ssj = ssj;

    }

    public void startTimers() {
        // Start the save timer
        saveTimer();
    }

    public void saveTimer() {

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ssj, () -> {

            if (!Bukkit.getOnlinePlayers().isEmpty()) {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    ssj.getSSJPCM().getPlayerConfig(p);

                    ssj.getSSJPCM().savePlayerConfig(p, ssj.getSSJPCM().getPlayerConfig(p));

                    ssj.getSSJConfigs().saveConfigs();

                }

            }

        }, 0, 20 * 10);

    }
}
