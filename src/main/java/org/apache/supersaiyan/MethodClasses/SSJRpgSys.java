package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SSJRpgSys {

    private final SSJ ssj;
    private BukkitTask passiveEnergyTask;

    public SSJRpgSys(SSJ ssj) {

        this.ssj = ssj;
        if (ssj.getSSJConfigs().getPassiveEnergyGain()) {
            startPassiveEnergyGain();
        }

    }

    private void startPassiveEnergyGain() {
        passiveEnergyTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (ssj.getSSJPCM().getStart(player)) {
                        addPassiveEnergy(player);
                    }
                }
            }
        }.runTaskTimer(ssj, 0L, 20L * 5); // Run every 5 seconds
    }

    private void addPassiveEnergy(Player player) {
        int currentEnergy = ssj.getSSJPCM().getEnergy(player);
        int energyLimit = ssj.getSSJPCM().getLimit(player);
        
        if (currentEnergy < energyLimit) {
            int passiveGain = ssj.getSSJConfigs().getPEMG();
            int newEnergy = Math.min(energyLimit, currentEnergy + passiveGain);
            
            ssj.getSSJPCM().setPlayerConfigValue(player, "Energy", newEnergy);
            
            // Update battle power and scoreboard
            multBP(player);
            ssj.getSSJMethodChecks().scoreBoardCheck();
            ssj.getSSJMethods().callScoreboard(player);
        }
    }

    // Make sure to clean up the task when the plugin is disabled
    public void stopPassiveEnergyGain() {
        if (passiveEnergyTask != null) {
            passiveEnergyTask.cancel();
            passiveEnergyTask = null;
        }
    }

    public void addEnergy(Player p) {

        int adde = ssj.getSSJPCM().getEnergy(p) * ssj.getSSJPCM().getPower(p);

        if (ssj.getSSJPCM().getEnergy(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p, "Energy", 5);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) < ssj.getSSJPCM().getLimit(p)) {

            ssj.getSSJPCM().setPlayerConfigValue(p, "Energy", adde);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        }

    }

    public void multBP(Player p) {

        int multbp = ((ssj.getSSJPCM().getEnergy(p) + getBaseBP(p)) * ssj.getSSJConfigs().getBPM());

        if (ssj.getSSJPCM().getBattlePower(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", addBaseBP(p));

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) == 0) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", addBaseBP(p));

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        } else if (ssj.getSSJPCM().getEnergy(p) < ssj.getSSJPCM().getLimit(p)) {

            ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", multbp);

            ssj.getSSJMethodChecks().scoreBoardCheck();

            ssj.getSSJMethods().callScoreboard(p);

        }

    }

    public int addBaseBP(Player p) {

        return getBaseBP(p) * ssj.getSSJConfigs().getBPM();

    }

    public int addLevel(Player p) {

        return getBaseBP(p) / 150;

    }

    public int getBaseBP(Player p) { // Multiplies the player's Health, Power, Strength, Speed, and defence and output's the player's base battle power.

        return ssj.getSSJPCM().getHealth(p) * ssj.getSSJPCM().getPower(p) + ssj.getSSJPCM().getStrength(p) *

                ssj.getSSJPCM().getSpeed(p) + ssj.getSSJPCM().getStamina(p) * ssj.getSSJPCM().getDefence(p);

    }

    public void updateAllStatBoosts(Player player) {
        // Health boost
        int healthLevel = ssj.getSSJPCM().getHealth(player);
        if (healthLevel > 0) {
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                double healthBoost = 20 + (healthLevel * 2); // Base health (20) + 2 health per level
                maxHealth.setBaseValue(Math.min(healthBoost, 100)); // Cap at 100 health
            }
        }
        
        // Strength boost
        int strengthLevel = ssj.getSSJPCM().getStrength(player);
        if (strengthLevel > 0) {
            AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attackDamage != null) {
                double damageBoost = 2 + (strengthLevel * 0.5); // Base damage (2) + 0.5 per level
                attackDamage.setBaseValue(Math.min(damageBoost, 20)); // Cap at 20 damage
            }
        }
        
        // Speed boost
        int speedLevel = ssj.getSSJPCM().getSpeed(player);
        if (speedLevel > 0) {
            AttributeInstance moveSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            if (moveSpeed != null) {
                double speedBoost = 0.1 + (speedLevel * 0.01); // Base speed (0.1) + 1% per level
                moveSpeed.setBaseValue(Math.min(speedBoost, 0.5)); // Cap at 5x normal speed
            }
        }
        
        // Defense boost
        int defenseLevel = ssj.getSSJPCM().getDefence(player);
        if (defenseLevel > 0) {
            AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
            AttributeInstance toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
            if (armor != null && toughness != null) {
                double armorBoost = defenseLevel * 2; // 2 armor per level
                double toughnessBoost = defenseLevel; // 1 toughness per level
                armor.setBaseValue(Math.min(armorBoost, 30));
                toughness.setBaseValue(Math.min(toughnessBoost, 20));
            }
        }
    }

    public void resetAllStatBoosts(Player player) {
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        AttributeInstance moveSpeed = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance armor = player.getAttribute(Attribute.GENERIC_ARMOR);
        AttributeInstance toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
        
        if (maxHealth != null) maxHealth.setBaseValue(20); // Default health
        if (attackDamage != null) attackDamage.setBaseValue(2); // Default damage
        if (moveSpeed != null) moveSpeed.setBaseValue(0.1); // Default speed
        if (armor != null) armor.setBaseValue(0); // Default armor
        if (toughness != null) toughness.setBaseValue(0); // Default toughness
    }

    // Add a method to start/stop passive gain based on config changes
    public void updatePassiveGain() {
        if (ssj.getSSJConfigs().getPassiveEnergyGain()) {
            if (passiveEnergyTask == null) {
                startPassiveEnergyGain();
            }
        } else {
            stopPassiveEnergyGain();
        }
    }

}
