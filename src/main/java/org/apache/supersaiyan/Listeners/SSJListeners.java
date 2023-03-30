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
    private void onPlayerJoin(PlayerJoinEvent e){

        for (Player online : Bukkit.getOnlinePlayers()) {

            ssj.getSSJPCM().createUserCheck(online);

            ssj.getSSJPCM().getPlayerConfig(online);

            ssj.getSSJMethodChecks().checkPPCPlayerName(online);

            ssj.getSSJMethods().callScoreboard(online);

            ssj.getSSJTimers().saveTimer();

        }

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
}