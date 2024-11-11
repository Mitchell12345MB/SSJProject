package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.MethodClasses.SSJBossBar;
import org.apache.supersaiyan.MethodClasses.SSJParticles;
import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SSJActionListeners implements Listener {

    private final SSJ ssj;

    private final Map<UUID, SSJBossBar> bossBars;

    private final Map<UUID, BukkitRunnable> transformBarTimeouts = new HashMap<>();

    private final Map<UUID, Long> chargeToggleCooldowns = new HashMap<>();
    private static final long CHARGE_TOGGLE_COOLDOWN = 500; // 500ms cooldown

    private final Map<UUID, Boolean> isHoldingRightClick = new HashMap<>();

    public SSJActionListeners(SSJ ssj) {

        this.ssj = ssj;

        this.bossBars = new HashMap<>();

    }

    @EventHandler
    private void onPlayerInteractTransform(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.BLAZE_POWDER) {

            if (!ssj.getSSJPCM().getStart(p)) {
                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");
                return;
            }

            // Stop charging if player is currently charging
            if (ssj.getSSJChargeSystem().isCharging()) {
                ssj.getSSJChargeSystem().stopCharging(p);
                p.sendMessage(ChatColor.YELLOW + "Stopped charging to transform.");
            }

            String currentForm = ssj.getSSJPCM().getForm(p);
            String nextForm = ssj.getSSJTransformationManager().getNextTransformation(p, currentForm);

            if (nextForm != null) {
                SSJBossBar bossBar = bossBars.computeIfAbsent(p.getUniqueId(), 
                    k -> new SSJBossBar(ssj, "Transformation Progress", new HashMap<>(), true));
                bossBar.show(p);

                if (ssj.getSSJTransformationManager().canTransform(p, nextForm)) {
                    ssj.getSSJTransformationManager().transform(p, nextForm);
                    ssj.getSSJMethodChecks().scoreBoardCheck();
                    ssj.getSSJMethods().callScoreboard(p);
                    bossBar.hide(p);
                    bossBars.remove(p.getUniqueId());
                } else {
                    bossBar.addProgress(p, 10);
                    scheduleBarRemoval(p);
                }
            } else {
                if (bossBars.containsKey(p.getUniqueId())) {
                    SSJBossBar bossBar = bossBars.get(p.getUniqueId());
                    bossBar.hide(p);
                    bossBars.remove(p.getUniqueId());
                }
                p.sendMessage(ChatColor.RED + "You haven't unlocked any transformations yet!");
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

            if (!ssj.getSSJPCM().getStart(p)) {
                p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");
                return;
            }

            String currentForm = ssj.getSSJPCM().getForm(p);
            if (currentForm.equals("Base")) {
                p.sendMessage(ChatColor.RED + "You are already in base form!");
                return;
            }

            // Perform detransformation
            ssj.getSSJTransformationManager().detransform(p);
            p.sendMessage(ChatColor.GREEN + "You have powered down to base form!");

            // Update UI elements
            if (bossBars.containsKey(p.getUniqueId())) {
                SSJBossBar bossBar = bossBars.get(p.getUniqueId());
                bossBar.removeProgress(p, 10);
                bossBar.hide(p);
                bossBars.remove(p.getUniqueId());
            }

            // Update scoreboard
            ssj.getSSJMethodChecks().scoreBoardCheck();
            ssj.getSSJMethods().callScoreboard(p);
        }
    }

    @EventHandler
    private void onPlayerInteractCharge(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        
        if (heldItem.getType() != Material.MAGMA_CREAM) {
            if (ssj.getSSJChargeSystem().isCharging(p)) {
                ssj.getSSJChargeSystem().stopCharging(p);
            }
            return;
        }

        if (!ssj.getSSJPCM().getStart(p)) {
            p.sendMessage(ChatColor.RED + "You haven't started your Saiyan journey!");
            return;
        }

        if (!ssj.getSSJChargeSystem().isCharging(p) && bossBars.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "Cannot charge while transforming!");
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ssj.getSSJConfigs().getHoldChargeItem()) {
                if (!ssj.getSSJChargeSystem().isCharging(p)) {
                    ssj.getSSJChargeSystem().startCharging(p);
                }
            } else {
                // Toggle charging
                long currentTime = System.currentTimeMillis();
                long lastToggleTime = chargeToggleCooldowns.getOrDefault(p.getUniqueId(), 0L);
                if (currentTime - lastToggleTime < CHARGE_TOGGLE_COOLDOWN) {
                    return;
                }
                chargeToggleCooldowns.put(p.getUniqueId(), currentTime);
                
                if (!ssj.getSSJChargeSystem().isCharging(p)) {
                    ssj.getSSJChargeSystem().startCharging(p);
                } else {
                    ssj.getSSJChargeSystem().stopCharging(p);
                }
            }
        }
    }

    @EventHandler
    private void onPlayerStopHoldingCharge(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if (ssj.getSSJConfigs().getHoldChargeItem() && 
            ssj.getSSJChargeSystem().isCharging(p) && 
            isHoldingRightClick.getOrDefault(p.getUniqueId(), false)) {
            isHoldingRightClick.put(p.getUniqueId(), false);
            p.sendMessage(ChatColor.RED + "Stopped charging.");
            ssj.getSSJChargeSystem().stopCharging(p);
        }
    }

    @EventHandler
    private void onPlayerInteractContainAura(PlayerInteractEvent e) { //contains your "aura" and normalises the player's stats: Speed, Strength, Damage resistance, Damage out-put, and multiplies player's "Battle Power."

        Player p = e.getPlayer();

        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.GHAST_TEAR) {

            if (ssj.getSSJPCM().getStart(p)) {

                p.sendMessage("woosh woosh woosh");

                SSJParticles ssjparticles = new SSJParticles(ssj, p, Particle.FLAME, 50, 3);
                ssjparticles.createParticles();

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

                p.sendMessage("woosh woosh woosh");

                SSJParticles ssjparticles = new SSJParticles(ssj, p, Particle.FLAME, 50, 3);
                ssjparticles.createParticles();

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

                ssj.getSSJGui().openGenStatInventory(e.getPlayer());

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

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent e) {
        if (!(e.getPlayer() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getPlayer();
        
        // Check if player is charging and trying to open enchanting table
        if (ssj.getSSJChargeSystem().isCharging() && 
            e.getInventory().getType() == InventoryType.ENCHANTING) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot use the enchanting table while charging energy!");
        }
    }

    private void scheduleBarRemoval(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Cancel existing timeout if any
        if (transformBarTimeouts.containsKey(playerId)) {
            transformBarTimeouts.get(playerId).cancel();
        }
        
        // Schedule new timeout
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (bossBars.containsKey(playerId)) {
                    SSJBossBar bossBar = bossBars.get(playerId);
                    bossBar.hide(player);
                    bossBars.remove(playerId);
                }
                transformBarTimeouts.remove(playerId);
            }
        };
        
        transformBarTimeouts.put(playerId, task);
        task.runTaskLater(ssj, 100L); // 5 seconds (20 ticks per second)
    }

    public Map<UUID, SSJBossBar> getBossBars() {
        return bossBars;
    }
}
