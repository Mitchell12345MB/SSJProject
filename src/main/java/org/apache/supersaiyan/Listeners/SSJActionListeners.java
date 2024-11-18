package org.apache.supersaiyan.Listeners;

import org.apache.supersaiyan.MethodClasses.SSJBossBar;
import org.apache.supersaiyan.MethodClasses.SSJParticles;
import org.apache.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SSJActionListeners implements Listener {

    private final SSJ ssj;

    private final Map<UUID, SSJBossBar> bossBars;

    private final Map<UUID, BukkitRunnable> transformBarTimeouts = new HashMap<>();

    private final Map<UUID, Long> chargeToggleCooldowns = new HashMap<>();
    
    private static final long CHARGE_TOGGLE_COOLDOWN = 500; // 500ms cooldown

    public final Set<UUID> holdingCharge = new HashSet<>();

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
                    k -> new SSJBossBar(ssj, "§5Transformation Progress", new HashMap<>(), false));
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
    public void onPlayerInteractCharge(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();
        
        if (heldItem.getType() != Material.MAGMA_CREAM) {
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

        // Handle hold-to-charge mode
        if (ssj.getSSJConfigs().getHoldChargeItem()) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(true); // Prevent item use
                if (!holdingCharge.contains(p.getUniqueId())) {
                    holdingCharge.add(p.getUniqueId());
                    ssj.getSSJChargeSystem().startCharging(p);
                }
            }
            return;
        }

        // Handle toggle mode
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
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

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (holdingCharge.contains(p.getUniqueId())) {
            Location from = e.getFrom();
            Location to = e.getTo();
            if (to != null && (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                holdingCharge.remove(p.getUniqueId());
                if (ssj.getSSJChargeSystem().isCharging(p)) {
                    ssj.getSSJChargeSystem().stopCharging(p);
                    p.sendMessage(ChatColor.RED + "Stopped charging due to movement.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        if (holdingCharge.contains(p.getUniqueId())) {
            holdingCharge.remove(p.getUniqueId());
            if (ssj.getSSJChargeSystem().isCharging(p)) {
                ssj.getSSJChargeSystem().stopCharging(p);
                p.sendMessage(ChatColor.RED + "Stopped charging.");
            }
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
    private void onPlayerInteractReleaseAura(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack heldItem = p.getInventory().getItemInMainHand();

        if ((e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) && heldItem.getType() == Material.GHAST_TEAR) {
            if (ssj.getSSJPCM().getStart(p)) {
                p.sendMessage("woosh woosh woosh");

                // Create base particle effect
                SSJParticles ssjparticles = new SSJParticles(ssj, p, Particle.FLAME, 50, 3);
                ssjparticles.createParticles();

                // Only add lightning effect for specific forms
                String currentForm = ssj.getSSJPCM().getForm(p);
                if (currentForm.equals("Super Saiyan 2") || 
                    currentForm.equals("Super Saiyan 3") || 
                    currentForm.equals("Super Saiyan 4") || 
                    currentForm.equals("Super Saiyan 5") || 
                    currentForm.equals("Super Saiyan God") || 
                    currentForm.equals("Super Saiyan Blue") || 
                    currentForm.equals("Super Saiyan Rose") || 
                    currentForm.equals("Super Saiyan Rage") || 
                    currentForm.equals("Super Saiyan Blue Evolution") || 
                    (currentForm.equals("Kaioken Transformation") && 
                     (currentForm.contains("x50") || currentForm.contains("x100")))) {
                    new SSJParticles(ssj, p, Particle.WAX_OFF, 10, 3).createLightningEffect();
                }
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
        if (transformBarTimeouts.containsKey(playerId)) {
            transformBarTimeouts.get(playerId).cancel();
        }

        BukkitRunnable timeout = new BukkitRunnable() {
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
        
        transformBarTimeouts.put(playerId, timeout);
        timeout.runTaskLater(ssj, 60L); // 3 second timeout
    }

    public Map<UUID, SSJBossBar> getBossBars() {
        return bossBars;
    }

    @EventHandler
    private void onPlayerPlacePluginItem(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        Material type = item.getType();
        
        // Check if the item is one of our plugin items
        if (type == Material.BLAZE_POWDER || 
            type == Material.PHANTOM_MEMBRANE || 
            type == Material.MAGMA_CREAM || 
            type == Material.GHAST_TEAR || 
            type == Material.PAPER || 
            type == Material.TNT) {
                
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot place plugin items!");
            }
        }
    }

    @EventHandler
    private void onPlayerDropPluginItem(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        Material type = item.getType();
        
        if (type == Material.BLAZE_POWDER || 
            type == Material.PHANTOM_MEMBRANE || 
            type == Material.MAGMA_CREAM || 
            type == Material.GHAST_TEAR || 
            type == Material.PAPER || 
            type == Material.TNT) {
                
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "You cannot drop plugin items!");
            }
        }
    }

    @EventHandler
    private void onInventoryMovePluginItem(InventoryClickEvent e) {
        ItemStack item = e.getCurrentItem();
        if (item == null) return;
        
        Material type = item.getType();
        if (type == Material.BLAZE_POWDER || 
            type == Material.PHANTOM_MEMBRANE || 
            type == Material.MAGMA_CREAM || 
            type == Material.GHAST_TEAR || 
            type == Material.PAPER || 
            type == Material.TNT) {
                
            if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                // Only prevent moving to other inventories, allow hotbar movement
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    e.setCancelled(true);
                    ((Player)e.getWhoClicked()).sendMessage(ChatColor.RED + "You cannot move plugin items to other inventories!");
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getWhoClicked();
        
        // Cancel the event for all custom inventories
        if (e.getInventory().equals(ssj.getSSJGui().genstatinv) ||
            e.getInventory().equals(ssj.getSSJGui().transformationsinv) ||
            e.getInventory().equals(ssj.getSSJGui().skillsinv) ||
            e.getInventory().equals(ssj.getSSJGui().settingsinv)) {
            
            e.setCancelled(true);
            
            // Only process clicks if there's an actual item
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }
            
            // Process the click based on inventory type
            if (e.getInventory().equals(ssj.getSSJGui().genstatinv)) {
                ssj.getSSJMethodChecks().callGenStatMenuChecks(p, e);
            } else if (e.getInventory().equals(ssj.getSSJGui().transformationsinv)) {
                ssj.getSSJMethodChecks().callTransformationsMenuChecks(p, e);
            } else if (e.getInventory().equals(ssj.getSSJGui().skillsinv)) {
                ssj.getSSJMethodChecks().callSkillsMenuChecks(p, e);
            } else if (e.getInventory().equals(ssj.getSSJGui().settingsinv)) {
                ssj.getSSJMethodChecks().callSettingsMenuChecks(p, e);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (ssj.getSSJPCM().hasSkill(player, "Fly")) {
            event.setCancelled(true);
            
            // If player is in creative mode, don't interfere
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            
            // Handle double jump to fly
            if (!player.isFlying()) {
                ssj.getSSJSkillManager().handleSkillActivation(player, "Fly");
                // Start energy drain
                ssj.getSSJEnergyManager().startEnergyDrain(player);
            } else {
                ssj.getSSJSkillManager().disableFlight(player);
                // Stop energy drain
                ssj.getSSJEnergyManager().stopEnergyDrain(player);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveFlightCheck(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isFlying() && ssj.getSSJPCM().hasSkill(player, "Fly")) {
            int energyCost = ssj.getSSJConfigs().getSCFile().getInt("Fly.Energy_Cost");
            if (ssj.getSSJPCM().getEnergy(player) < energyCost) {
                ssj.getSSJSkillManager().disableFlight(player);
                ssj.getSSJEnergyManager().stopEnergyDrain(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!ssj.getSSJPCM().hasSkill(player, "Fly")) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;
        
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;
        
        // Check if player is jumping
        if (to.getY() > from.getY() && player.getVelocity().getY() > 0) {
            // Allow flight on jump
            player.setAllowFlight(true);
        }
    }

}
