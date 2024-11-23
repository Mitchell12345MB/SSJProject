package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.ChatColor;

public class SSJSkillManager {
    private final SSJ ssj;
    
    public SSJSkillManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public boolean canUseSkill(Player player, String skillName) {
        FileConfiguration skillConfig = ssj.getSSJConfigs().getSCFile();
        ConfigurationSection skillSection = skillConfig.getConfigurationSection(skillName);
        
        if (skillSection == null) return false;

        // Check if skill is enabled in player's settings
        if (!ssj.getSSJPCM().isSkillEnabled(player, skillName)) {
            player.sendMessage("§c" + skillName + " skill is disabled in your settings!");
            return false;
        }
        
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
            
            switch (stat) {
                case "Battle_Power_Multiplier":
                    ssj.getSSJEnergyManager().setBPMultiplier(player, totalBoost);
                    break;
                case "Speed":
                    // Convert the speed boost to a value suitable for fly speed (float between 0 and 1)
                    float flySpeed = (float) Math.min(0.1 + (totalBoost * 0.01), 1.0); // Adjust as needed
                    player.setFlySpeed(flySpeed);
                    break;
                // Handle other stats like "Jump" if necessary
            }
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
        
        // Apply updated boosts
        applySkillBoosts(player, skillName);
        
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
        
        if (skillName.equals("Fly")) {            
            // Proceed with SSJ flight activation with energy cost
            int energyCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Energy_Cost");
            if (ssj.getSSJPCM().getEnergy(player) >= energyCost) {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.sendMessage("§aFlight enabled!");
                ssj.getSSJEnergyManager().modifyEnergy(player, -energyCost);
                // Start energy drain
                ssj.getSSJEnergyManager().startEnergyDrain(player, "flight");
            }
        }
    }
    
    public void disableFlight(Player player) {
        // Check if 'Staff Flight' is enabled
        if (ssj.getSSJPCM().isStaffFlightEnabled(player)) {
            // Do not disable flight for staff
            return;
        }

        // Revoke flight permissions
        player.setFlying(false);
        player.setAllowFlight(false);

        // Stop energy drain associated with flying
        ssj.getSSJEnergyManager().stopEnergyDrain(player, "flight");

        // Notify the player
        player.sendMessage(ChatColor.RED + "Your flight ability has been disabled.");
    }
    
    public void enableFlight(Player player) {
        // Check if 'Staff Flight' is enabled
        if (ssj.getSSJPCM().isStaffFlightEnabled(player)) {
            // Allow flight without energy
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§aStaff Flight enabled!");
            return;
        }

        if (!canUseSkill(player, "Fly")) return;

        int energyCost = ssj.getSSJConfigs().getSCFile().getInt("Fly.Energy_Cost");
        if (ssj.getSSJPCM().getEnergy(player) >= energyCost) {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§aFlight enabled!");
            ssj.getSSJEnergyManager().modifyEnergy(player, -energyCost);
            ssj.getSSJEnergyManager().startEnergyDrain(player, "flight");
        } else {
            player.sendMessage("§cNot enough energy to fly!");
        }
    }
    
    public void handleSkillDisabled(Player player, String skillName) {
        if (skillName.equalsIgnoreCase("Fly")) {
            disableFlight(player);
        }
        // Handle other skills if necessary
    }
}
