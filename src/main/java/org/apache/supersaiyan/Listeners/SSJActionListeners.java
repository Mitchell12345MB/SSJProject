package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.MethodClasses.SSJBossBar;
import org.apache.supersaiyan.MethodClasses.SSJParticles;
import org.apache.supersaiyan.MethodClasses.SSJXPBar;
import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJActionListeners implements Listener {

    private final SSJ ssj;

    private final Map<UUID, SSJBossBar> bossBars;

    public SSJActionListeners(SSJ ssj) {

        this.ssj = ssj;

        this.bossBars = new HashMap<>();

    }

    @EventHandler
    private void onPlayerInteractTransform(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.BLAZE_POWDER) {

            if (ssj.getSSJPCM().getStart(p)) {

                p.sendMessage("WOOSH");

                SSJXPBar xpBar = new SSJXPBar(ssj, p);

                xpBar.start();

                xpBar.removeXP(5);

                SSJBossBar bossBar = bossBars.computeIfAbsent(p.getUniqueId(), uuid ->

                        new SSJBossBar(ssj, "Diamond Boss Bar", new HashMap<>()));

                bossBar.show(p);

                bossBar.addProgress(p, 10);

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        } else if (bossBars.containsKey(p.getUniqueId())) {

            SSJBossBar bossBar = bossBars.get(p.getUniqueId());

            bossBar.hide(p);

            bossBars.remove(p.getUniqueId());

        }

    }

    @EventHandler
    private void onPlayerInteractPowerDown(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.PHANTOM_MEMBRANE) {

            if (ssj.getSSJPCM().getStart(p)) {

                p.sendMessage("deWOOSH");

                if (bossBars.containsKey(p.getUniqueId())) {

                    SSJBossBar bossBar = bossBars.get(p.getUniqueId());

                    bossBar.removeProgress(p, 10);

                }

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractCharge(PlayerInteractEvent e) { //Charges the player's energy and multiplies the player's "Battle Power" over time depending on the player's

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.MAGMA_CREAM) {

            if (ssj.getSSJPCM().getStart(p)) {

                p.sendMessage("Charging");

                SSJXPBar xpBar = new SSJXPBar(ssj, p);

                xpBar.start();

                xpBar.addXP(5);

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractContainAura(PlayerInteractEvent e) { //contains your "aura" and normalises the player's stats: Speed, Strength, Damage resistance, Damage out-put, and multiplies player's "Battle Power."

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.GHAST_TEAR) {

            if (ssj.getSSJPCM().getStart(p)) {

                SSJXPBar xpBar = new SSJXPBar(ssj, p);

                p.sendMessage("woosh woosh woosh");

                SSJParticles ssjparticles = new SSJParticles(ssj, p, Particle.FLAME, 50, 3);
                ssjparticles.createParticles();

                xpBar.start();

                xpBar.removeXP(5);

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractReleaseAura(PlayerInteractEvent e) { //Releases your "aura" and boost plays stats: Speed, Strength, Damage resistance, Damage out-put, and multiplies player's "Battle Power."

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.GHAST_TEAR) {

            if (ssj.getSSJPCM().getStart(p)) {

                SSJXPBar xpBar = new SSJXPBar(ssj, p);

                p.sendMessage("woosh woosh woosh");

                SSJParticles ssjparticles = new SSJParticles(ssj, p, Particle.FLAME, 50, 3);
                ssjparticles.createParticles();

                xpBar.start();

                xpBar.removeXP(5);

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    //Below are the menu and score board actions.

    @EventHandler
    private void onPlayerInteractOpenMenu(PlayerInteractEvent e) { //Opens the player's stats menu.

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.PAPER) {

            if (ssj.getSSJPCM().getStart(p)) {

                ssj.getSSJGui().openInventory(e.getPlayer());

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractRemoveSB(PlayerInteractEvent e) { //Removes scoreboard from the right side of screen that contains the player's stats.

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.TNT) {

            if (ssj.getSSJPCM().getStart(p)) {

                if (ssj.getSSJSB().hasScore(p)) {

                    ssj.getSSJSB().removeScore(p);

                    p.sendMessage(ChatColor.RED + "Scoreboard removed.");

                } else {

                    ssj.getSSJMethods().callScoreboard(p);

                    p.sendMessage(ChatColor.RED + "Scoreboard added.");

                }

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }
        }
    }
}
