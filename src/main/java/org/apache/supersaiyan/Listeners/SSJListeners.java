package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class SSJListeners implements Listener {

    private final SSJ ssj;

    public SSJListeners(SSJ ssj) {
        this.ssj = ssj;
    }

    @EventHandler
    private void onPlayerInteractTransform(PlayerInteractEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, e.getPlayer().getUniqueId());

        if (user.getUserConfig().getBoolean("Start")) {

            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.BLAZE_POWDER)))

                return;

            e.getPlayer().sendMessage("WOOSH");

        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractPowerDown(PlayerInteractEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, e.getPlayer().getUniqueId());

        if (user.getUserConfig().getBoolean("Start")) {

            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.PHANTOM_MEMBRANE)))

                return;

            e.getPlayer().sendMessage("deWOOSH");

        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractCharge(PlayerInteractEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, e.getPlayer().getUniqueId());

        if (user.getUserConfig().getBoolean("Start")) {


            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.MAGMA_CREAM)))

                return;

            e.getPlayer().sendMessage("Charging");

        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractOpenMenu(PlayerInteractEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, e.getPlayer().getUniqueId());

        if (user.getUserConfig().getBoolean("Start")) {


            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.PAPER)))

                return;

            ssj.getSSJGui().openInventory(e.getPlayer());


        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractReleaseAura(PlayerInteractEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, e.getPlayer().getUniqueId());

        if (user.getUserConfig().getBoolean("Start")) {


            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.GHAST_TEAR)))

                return;

            e.getPlayer().sendMessage("woosh woosh woosh");


        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getSSJGui().inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ssj.getSSJMethods().callMenuChecks(p, e);

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {

        if (e.getInventory().equals(ssj.getSSJGui().inv)) {

            e.setCancelled(true);

        }

    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent e) {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                SSJPlayerConfig user = new SSJPlayerConfig(ssj, online.getUniqueId());

                user.createUserCheck(online);

                user.loadUserFile();

                ssj.getSSJMethods().callUpdatePlayerName(online);

                ssj.getSSJMethods().callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

            }

        }

    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent e) {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                SSJPlayerConfig user = new SSJPlayerConfig(ssj, online.getUniqueId());

                ssj.getSSJMethods().scoreBoardCheck(online);

                user.createUserCheck(online);

                user.loadUserFile();

                user.saveUserFile();
            }
        }
    }
}