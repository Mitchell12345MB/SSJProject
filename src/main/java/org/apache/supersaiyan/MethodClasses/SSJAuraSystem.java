package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Particle;

public class SSJAuraSystem {
    private final SSJ ssj;
    private final Map<UUID, BukkitTask> auraEffectTasks;
    private static final double AURA_ENERGY_COST_PER_SECOND = 2; // 2 energy per second
    private static final double AURA_BOOST = 0.5; // 50% stat boost

    public SSJAuraSystem(SSJ ssj) {
        this.ssj = ssj;
        this.auraEffectTasks = new HashMap<>();
    }

    public void toggleAura(Player player) {
        UUID playerId = player.getUniqueId();

        // If aura is active, stop it
        if (auraEffectTasks.containsKey(playerId)) {
            stopAura(player);
            return;
        }

        // Start aura if not active
        startAura(player);
    }

    private void startAura(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Apply stat boosts
        ssj.getSSJEnergyManager().setBPMultiplier(player, 1.0 + AURA_BOOST);
        ssj.getSSJRpgSys().updateAllStatBoosts(player);

        // Create particle effects and handle energy drain
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // Check energy and drain it
                int currentEnergy = ssj.getSSJPCM().getEnergy(player);
                if (currentEnergy <= 0) {
                    stopAura(player);
                    player.sendMessage(ChatColor.RED + "Your aura fades as your energy depletes...");
                    return;
                }

                // Drain energy
                ssj.getSSJEnergyManager().modifyEnergy(player, -(int)AURA_ENERGY_COST_PER_SECOND);

                // Create aura particles
                String currentForm = ssj.getSSJPCM().getForm(player);
                Particle particleType = currentForm.equals("Base") ? 
                    Particle.END_ROD : Particle.FLAME;
                
                player.getWorld().spawnParticle(
                    particleType,
                    player.getLocation(),
                    20, // Amount
                    0.5, // Spread X
                    1, // Spread Y
                    0.5, // Spread Z
                    0.1 // Speed
                );
            }
        }.runTaskTimer(ssj, 0L, 20L); // Run every second

        auraEffectTasks.put(playerId, task);
        player.sendMessage(ChatColor.GOLD + "You release your aura, boosting your power!");
    }

    public void stopAura(Player player) {
        UUID playerId = player.getUniqueId();
        if (auraEffectTasks.containsKey(playerId)) {
            // Cancel the task
            auraEffectTasks.get(playerId).cancel();
            auraEffectTasks.remove(playerId);
            
            // Reset stats
            ssj.getSSJEnergyManager().setBPMultiplier(player, 1.0);
            ssj.getSSJRpgSys().updateAllStatBoosts(player);
            player.sendMessage(ChatColor.YELLOW + "Your aura fades away...");
        }
    }

    public boolean hasAuraActive(Player player) {
        return auraEffectTasks.containsKey(player.getUniqueId());
    }
} 