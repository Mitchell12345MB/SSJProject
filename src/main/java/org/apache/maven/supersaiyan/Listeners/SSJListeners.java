package org.apache.maven.supersaiyan.Listeners;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SSJListeners implements Listener {

    private final SSJ ssj;

    public SSJListeners(SSJ ssj) {
        this.ssj = ssj;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.BLAZE_POWDER))) return;

        e.getPlayer().sendMessage("WOOSH");

    }

    @EventHandler
    public void onPlayerInteract2(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.BLAZE_ROD))) return;

        e.getPlayer().sendMessage("deWOOSH");

    }

    @EventHandler
    public void onPlayerInteract3(PlayerInteractEvent e) {

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(new ItemStack(Material.FIREWORK_STAR))) return;

        e.getPlayer().sendMessage("Charging");
    }
}
