package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SSJXPBar {

    private final SSJ ssj;

    private final Player player;

    private final int maxXP;

    private int currentXP;

    private BukkitRunnable xpBarTask;

    public SSJXPBar(SSJ ssj, Player player, int maxXP) {

        this.ssj = ssj;

        this.player = player;

        this.maxXP = maxXP;

        this.currentXP = 0;

    }

    public void start() {

        xpBarTask = new BukkitRunnable() {
            @Override
            public void run() {

                float xpPercent = (float) currentXP / (float) maxXP;

                player.setExp(xpPercent);

                player.setLevel(currentXP);

                if (currentXP >= maxXP) {

                    player.sendMessage(ChatColor.GREEN + "You have filled up the XP bar!");

                    stop();

                }

            }
        };

        xpBarTask.runTaskTimer(ssj, 0L, 1L);

    }

    public void stop() {

        if (xpBarTask != null) {

            xpBarTask.cancel();

            xpBarTask = null;

            player.setExp(0);

            player.setLevel(0);

        }

    }

    public void setXP(int xp) {

        this.currentXP = Math.max(0, Math.min(xp, maxXP));

    }

    public void addXP(int amount) {

        setXP(currentXP + amount);

    }

    public void removeXP(int amount) {

        setXP(currentXP - amount);
    }
}
