package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class SSJSkillManager {
    private final SSJ ssj;
    
    public SSJSkillManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public boolean canUseSkill(Player player, String skillName) {
        FileConfiguration skillConfig = ssj.getSSJConfigs().getSCFile();
        ConfigurationSection skillSection = skillConfig.getConfigurationSection(skillName);
        
        if (skillSection == null) return false;
        
        // Check AP requirement
        int requiredAP = skillSection.getInt("Acion_Points_Cost");
        int playerAP = ssj.getSSJPCM().getActionPoints(player);
        if (playerAP < requiredAP) return false;
        
        // Check stat requirements
        ConfigurationSection statReq = skillSection.getConfigurationSection("Stat_Requirements");
        if (statReq != null) {
            for (String stat : statReq.getKeys(false)) {
                int required = statReq.getInt(stat);
                int playerStat = getPlayerStat(player, stat);
                if (playerStat < required) return false;
            }
        }
        
        return true;
    }
    
    int getPlayerStat(Player player, String stat) {
        return switch (stat.toLowerCase()) {
            case "power" -> ssj.getSSJPCM().getPower(player);
            case "strength" -> ssj.getSSJPCM().getStrength(player);
            case "speed" -> ssj.getSSJPCM().getSpeed(player);
            default -> 0;
        };
    }
    
    public void applySkillBoosts(Player player, String skillName) {
        ConfigurationSection skillSection = ssj.getSSJConfigs().getSCFile().getConfigurationSection(skillName);
        if (skillSection == null) return;
        
        ConfigurationSection boosts = skillSection.getConfigurationSection("Stat_Boosts");
        if (boosts == null) return;
        
        int skillLevel = getSkillLevel(player, skillName);
        
        for (String stat : boosts.getKeys(false)) {
            double baseBoost = boosts.getDouble(stat);
            double totalBoost = baseBoost + (skillLevel * 5.0); // 5 points per level
            
            if (stat.equals("Battle_Power_Multiplier")) {
                ssj.getSSJEnergyManager().setBPMultiplier(player, totalBoost);
            }
            // Add other stat boost applications as needed
        }
    }
    
    public int getSkillLevel(Player player, String skillName) {
        return ssj.getSSJPCM().getPlayerConfig(player).getInt("Skills." + skillName + ".Level", 0);
    }
    
    public void upgradeSkill(Player player, String skillName) {
        int currentLevel = getSkillLevel(player, skillName);
        int maxLevel = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Max_Level", 10);
        
        if (currentLevel >= maxLevel) {
            player.sendMessage("§c" + skillName + " is already at maximum level!");
            return;
        }
        
        // Increase skill level
        ssj.getSSJPCM().setPlayerConfigValue(
            player, 
            "Skills." + skillName + ".Level", 
            currentLevel + 1
        );
        
        // Apply any level-up bonuses
        ConfigurationSection skillSection = ssj.getSSJConfigs().getSCFile().getConfigurationSection(skillName);
        if (skillSection != null) {
            ConfigurationSection boosts = skillSection.getConfigurationSection("Stat_Boosts");
            if (boosts != null) {
                for (String stat : boosts.getKeys(false)) {
                    double baseBoost = boosts.getDouble(stat);
                    double totalBoost = baseBoost * (currentLevel + 1);
                    
                    // Apply the stat boost based on the stat type
                    switch (stat.toLowerCase()) {
                        case "battle_power_multiplier":
                            ssj.getSSJEnergyManager().setBPMultiplier(player, totalBoost);
                            break;
                        case "energy_gain_multiplier":
                            ssj.getSSJEnergyManager().setEnergyGainMultiplier(player, totalBoost);
                            break;
                        case "energy_limit_multiplier":
                            ssj.getSSJEnergyManager().setEnergyLimitMultiplier(player, totalBoost);
                            break;
                        // Add other stat boost types as needed
                    }
                }
            }
        }
        
        // Update player stats after skill upgrade
        ssj.getSSJRpgSys().updateAllStatBoosts(player);
    }
    
    public int getMaxSkillLevel(String skillName) {
        return ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Max_Level", 10);
    }
    
    public boolean isSkillMaxLevel(Player player, String skillName) {
        return getSkillLevel(player, skillName) >= getMaxSkillLevel(skillName);
    }
    
    public void handleSkillActivation(Player player, String skillName) {
        if (!ssj.getSSJPCM().hasSkill(player, skillName)) return;
        
        // Special handling for Fly skill
        if (skillName.equals("Fly")) {
            int energyCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Energy_Cost");
            if (ssj.getSSJPCM().getEnergy(player) >= energyCost) {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("§aFlight enabled!");
                ssj.getSSJEnergyManager().modifyEnergy(player, -energyCost);
            } else {
                player.sendMessage("§cNot enough energy to fly!");
            }
        }
        
        // Handle other skills...
    }
    
    public void disableFlight(Player player) {
        if (player.isFlying()) {
            player.setFlying(false);
            player.setAllowFlight(false);
            player.sendMessage("§cFlight disabled!");
        }
    }
}
