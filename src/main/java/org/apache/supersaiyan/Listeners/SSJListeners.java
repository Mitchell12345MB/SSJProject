package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class SSJListeners implements Listener {

    private final SSJ ssj;

    public SSJListeners(SSJ ssj) {

        this.ssj = ssj;

    }

    @EventHandler
    public void onGenStatsInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getSSJGui().genstatinv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ssj.getSSJMethodChecks().scoreBoardCheck();

        ssj.getSSJMethods().callScoreboard(p);

        ssj.getSSJMethodChecks().callGenStatMenuChecks(p, e);

    }

    @EventHandler
    public void onSkillStatsInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getSSJGui().skillstatinv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ssj.getSSJMethodChecks().scoreBoardCheck();

        ssj.getSSJMethods().callScoreboard(p);

        ssj.getSSJMethodChecks().callSkillStatMenuChecks(p, e);

    }

    @EventHandler
    public void onSettingsInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getSSJGui().settingsinv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ssj.getSSJMethodChecks().scoreBoardCheck();

        ssj.getSSJMethods().callScoreboard(p);

        ssj.getSSJMethodChecks().callSettingsMenuChecks(p, e);

    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {

        if (e.getInventory().equals(ssj.getSSJGui().genstatinv) || e.getInventory().equals(ssj.getSSJGui().skillstatinv) ||
                e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Always reset stats first
        ssj.getSSJRpgSys().resetAllStatBoosts(player);
        
        // Check if player's config exists
        if (!ssj.getSSJPCM().getFile(player).exists()) {
            // Create new config with default values
            ssj.getSSJPCM().createUserCheck(player);
            // Reset all stats to default
            resetPlayerStats(player);
        }

        // Only apply stats if they've started their journey
        if (ssj.getSSJPCM().getStart(player)) {
            // Apply all base stat boosts
            ssj.getSSJRpgSys().updateAllStatBoosts(player);
            
            // Update scoreboard
            ssj.getSSJMethodChecks().scoreBoardCheck();
            ssj.getSSJMethods().callScoreboard(player);
        }

    }

    private void resetPlayerStats(Player player) {
        // Reset all player stats to default or zero
        ssj.getSSJPCM().setPlayerConfigValue(player, "Level", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Battle_Power", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Saiyan_Ability", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Form", "Base");
        ssj.getSSJPCM().setPlayerConfigValue(player, "Action_Points", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Health", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Power", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Strength", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Speed", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Stamina", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Defence", 0);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Transformations_Unlocked", "");
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                ssj.getSSJMethodChecks().scoreBoardCheck();

                ssj.getSSJPCM().createUserCheck(online);

                ssj.getSSJPCM().getPlayerConfig(online);

                ssj.getSSJPCM().savePlayerConfig(online, ssj.getSSJPCM().getPlayerConfig(online));
            }
        }
    }

    @EventHandler
    public void onTransformationsInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().transformationsinv)) return;
        
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        final Player p = (Player) e.getWhoClicked();
        ssj.getSSJMethodChecks().callTransformationsMenuChecks(p, e);
    }

    @EventHandler
    public void onSkillsInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().skillsinv)) return;
        
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        final Player p = (Player) e.getWhoClicked();
        ssj.getSSJMethodChecks().callSkillsMenuChecks(p, e);
        }
}