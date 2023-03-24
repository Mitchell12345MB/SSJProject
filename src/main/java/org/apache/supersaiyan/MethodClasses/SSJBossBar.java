package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class SSJBossBar {

    private final BossBar bossBar;

    private final Map<UUID, Integer> playerStats;

    public SSJBossBar(SSJ ssj, String title, Map<UUID, Integer> playerStats) {

        this.bossBar = ssj.getServer().createBossBar(title, BarColor.RED, BarStyle.SOLID);

        this.playerStats = playerStats;

    }

    public void show(Player player) {

        bossBar.addPlayer(player);

    }

    public void hide(Player player) {

        bossBar.removePlayer(player);

    }

    public void update(Player player) {

        int statValue = playerStats.getOrDefault(player.getUniqueId(), 0);

        float progress = (float) statValue / 100;

        bossBar.setProgress(progress);

    }

    public void addProgress(Player player, int amount) {

        int currentValue = playerStats.getOrDefault(player.getUniqueId(), 0);

        int newValue = Math.min(currentValue + amount, 100);

        playerStats.put(player.getUniqueId(), newValue);

        update(player);

    }

    public void removeProgress(Player player, int amount) {

        int currentValue = playerStats.getOrDefault(player.getUniqueId(), 0);

        int newValue = Math.max(currentValue - amount, 0);

        playerStats.put(player.getUniqueId(), newValue);

        update(player);
    }
}
