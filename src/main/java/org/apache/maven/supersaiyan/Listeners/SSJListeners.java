package org.apache.maven.supersaiyan.Listeners;

import org.apache.maven.supersaiyan.SSJ;
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

        if (ssj.getpPc().getpConfig(e.getPlayer()).getBoolean("Start")) {

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

        if (ssj.getpPc().getpConfig(e.getPlayer()).getBoolean("Start")) {

            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.BLAZE_ROD)))

                return;

            e.getPlayer().sendMessage("deWOOSH");

        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractCharge(PlayerInteractEvent e) {

        if (ssj.getpPc().getpConfig(e.getPlayer()).getBoolean("Start")) {


            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.FIREWORK_STAR)))

                return;

            e.getPlayer().sendMessage("Charging");

        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    private void onPlayerInteractOpenMenu(PlayerInteractEvent e) {

        if (ssj.getpPc().getpConfig(e.getPlayer()).getBoolean("Start")) {


            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR)

                return;

            if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.PAPER)))

                return;


            ssj.getssjgui().openInventory(e.getPlayer());



        } else {

            e.getPlayer().sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

            e.getPlayer().sendMessage(ChatColor.RED + "So this action won't work!");

        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getssjgui().inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());

    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {

        if (e.getInventory().equals(ssj.getssjgui().inv)) {

            e.setCancelled(true);
        }
    }

    @EventHandler
    private void createAndUpdatePlayerConfig(PlayerJoinEvent e) {

        if (!ssj.getpPc().getpConfigFile(e.getPlayer()).exists()) {

            ssj.getpPc().callCreatePLayerConfig(e.getPlayer());

        } else {

            ssj.getpPc().callUpdatePlayerName(e.getPlayer());

        }

    }

    @EventHandler
    private void savePlayerConfig(PlayerQuitEvent e) {

        if (!ssj.getpPc().getpConfigFile(e.getPlayer()).exists()) {

            ssj.getpPc().callCreatePLayerConfig(e.getPlayer());

        } else {

            ssj.getpPc().callSavePlayerConfig(e.getPlayer());
        }
    }
}