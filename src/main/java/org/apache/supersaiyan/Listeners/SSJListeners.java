package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.MethodClasses.SSJBossBar;
import org.apache.supersaiyan.MethodClasses.SSJParticles;
import org.apache.supersaiyan.MethodClasses.SSJXPBar;
import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJListeners implements Listener {

    private final int MAX_ENERGY = 100;

    private final SSJ ssj;

    private final Map<UUID, SSJBossBar> bossBars;

    public SSJListeners(SSJ ssj) {

        if (ssj == null) {

            throw new IllegalArgumentException("ssj cannot be null");

        }

        this.ssj = ssj;

        this.bossBars = new HashMap<>();
    }

    @EventHandler
    private void onPlayerInteractTransform(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.BLAZE_POWDER) {

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

                p.sendMessage("WOOSH");

                SSJXPBar xpBar = new SSJXPBar(ssj, p, MAX_ENERGY);

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

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

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
    private void onPlayerInteractCharge(PlayerInteractEvent e) {



        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.MAGMA_CREAM) {

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

                p.sendMessage("Charging");

                SSJXPBar xpBar = new SSJXPBar(ssj, p, MAX_ENERGY);

                xpBar.start();

                xpBar.addXP(5);

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractOpenMenu(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.PAPER) {

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

                ssj.getSSJGui().openInventory(e.getPlayer());

            } else {

                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");

                p.sendMessage(ChatColor.RED + "So this action won't work!");

            }

        }

    }

    @EventHandler
    private void onPlayerInteractReleaseAura(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.GHAST_TEAR) {

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

                SSJXPBar xpBar = new SSJXPBar(ssj, p, MAX_ENERGY);

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
    private void onPlayerInteractRemoveSB(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.TNT) {

            if (ssj.getSSJPCM().getPlayerConfig(e.getPlayer()).getBoolean("Start")) {

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

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        if (!e.getInventory().equals(ssj.getSSJGui().inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();

        ssj.getSSJMethodChecks().scoreBoardCheck();

        ssj.getSSJMethods().callScoreboard(p);

        ssj.getSSJMethodChecks().callMenuChecks(p, e);

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {

        if (e.getInventory().equals(ssj.getSSJGui().inv)) {

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

            ssj.getSSJTimers().bpandEnergyMultiplier();

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