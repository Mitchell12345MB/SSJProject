package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;

public class SSJBossBar {
    private final SSJ ssj;
    private final String title;
    private final Map<UUID, Integer> playerStats;
    private final boolean isPersistent;
    private BukkitTask updateTask;
    private final boolean isTransformBar;
    private BossBar bossBar;

    public SSJBossBar(SSJ ssj, String title, Map<UUID, Integer> playerStats, boolean isPersistent) {
        this.ssj = ssj;
        this.title = title != null ? title : "Energy: ";
        this.playerStats = playerStats;
        this.isPersistent = isPersistent || ssj.getSSJConfigs().getEnergyBarVisible();
        this.isTransformBar = title != null && title.contains("Transformation");
        
        if (isTransformBar) {
            this.bossBar = Bukkit.createBossBar(this.title, BarColor.PURPLE, BarStyle.SOLID);
        }
        
        if (this.isPersistent) {
            startPersistentUpdates();
        }
    }

    private void startPersistentUpdates() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID playerId : playerStats.keySet()) {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null && player.isOnline()) {
                        update(player);
                    }
                }
            }
        }.runTaskTimer(ssj, 0L, 20L); // Update every second
    }

    public void show(Player player) {
        if (isTransformBar) {
            bossBar.addPlayer(player);
        }
        update(player);
    }

    public void hide(Player player) {
        if (isTransformBar && bossBar != null) {
            bossBar.removePlayer(player);
        }
        if (!isPersistent) {
            playerStats.remove(player.getUniqueId());
            if (updateTask != null) {
                updateTask.cancel();
                updateTask = null;
            }
        }
    }

    public void update(Player player) {
        int statValue = playerStats.getOrDefault(player.getUniqueId(), 0);
        float progress = (float) statValue / 100;

        if (isTransformBar && bossBar != null) {
            bossBar.setProgress(Math.min(1.0, progress));
            bossBar.setTitle(ChatColor.LIGHT_PURPLE + "Transformation Progress: " + 
                            ChatColor.YELLOW + statValue + "%");
        } else {
            StringBuilder energyBar = new StringBuilder();
            int energyBarLength = 20;
            int energyFilledBars = (int) (progress * energyBarLength);
            
            energyBar.append(ChatColor.GOLD).append("Energy: ");
            energyBar.append(ChatColor.GREEN);
            for (int i = 0; i < energyFilledBars; i++) {
                energyBar.append("│");
            }
            energyBar.append(ChatColor.RED);
            for (int i = energyFilledBars; i < energyBarLength; i++) {
                energyBar.append("│");
            }
            energyBar.append(" ").append(ChatColor.YELLOW).append(statValue).append("%");

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(energyBar.toString()));
        }
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

    public Map<UUID, Integer> getPlayerStats() {
        return playerStats;
    }
}
