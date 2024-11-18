package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;

public class SSJSaiyanAbilityManager {
    private final SSJ ssj;
    
    public SSJSaiyanAbilityManager(SSJ ssj) {
        this.ssj = ssj;
    }
    
    public boolean canIncreaseSaiyanAbility(Player player) {
        if (!ssj.getSSJPCM().getStart(player)) {
            return false;
        }
        int currentPoints = ssj.getSSJPCM().getActionPoints(player);
        int cost = calculateSaiyanAbilityCost(player);
        return currentPoints >= cost;
    }
    
    public int calculateSaiyanAbilityCost(Player player) {
        int currentLevel = ssj.getSSJPCM().getSaiyanAbility(player);
        return 10 + (currentLevel * 5); // Base cost 10, increases by 5 per level
    }
    
    public void increaseSaiyanAbility(Player player) {
        if (!canIncreaseSaiyanAbility(player)) {
            player.sendMessage("§cNot enough action points to increase Saiyan Ability!");
            return;
        }
        
        int cost = calculateSaiyanAbilityCost(player);
        int currentLevel = ssj.getSSJPCM().getSaiyanAbility(player);
        int currentPoints = ssj.getSSJPCM().getActionPoints(player);
        
        // Deduct action points
        ssj.getSSJPCM().setActionPoints(player, currentPoints - cost);
        
        // Increase Saiyan Ability level
        ssj.getSSJPCM().setSaiyanAbility(player, currentLevel + 1);
        
        // Check for newly unlocked transformations
        checkUnlockTransformations(player);
        
        // Update stats
        ssj.getSSJRpgSys().multBP(player);
        ssj.getSSJMethodChecks().scoreBoardCheck();
        ssj.getSSJMethods().callScoreboard(player);
        
        player.sendMessage("§aYour Saiyan Ability has increased to level " + (currentLevel + 1) + "!");
    }
    
    private void checkUnlockTransformations(Player player) {
        int saiyanAbility = ssj.getSSJPCM().getSaiyanAbility(player);
        String currentTransforms = ssj.getSSJPCM().getTransformations(player);
        
        for (String category : new String[]{"Saiyan_Forms", "Saiyan_God_Forms"}) {
            ConfigurationSection forms = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
            if (forms == null) continue;
            
            for (String key : forms.getKeys(false)) {
                ConfigurationSection form = forms.getConfigurationSection(key);
                if (form == null) continue;
                
                int requiredLevel = form.getInt("Saiyan_Ability_Lock", 0);
                String transformId = form.getString("TransformationID");
                
                if (requiredLevel <= saiyanAbility && transformId != null && !currentTransforms.contains(transformId)) {
                    // Unlock new transformation
                    String newTransforms = currentTransforms.isEmpty() ? transformId : currentTransforms + "," + transformId;
                    ssj.getSSJPCM().setTransformations(player, newTransforms);
                    player.sendMessage("§aUnlocked new transformation: " + form.getString("Desc", "Unknown"));
                }
            }
        }
    }
    
    public void upgradeAbility(Player player, String abilityName) {
        if (!ssj.getSSJPCM().getStart(player)) {
            player.sendMessage("§cYou haven't started your Saiyan journey!");
            return;
        }

        int currentLevel = ssj.getSSJPCM().getAbilityLevel(player, abilityName);
        int maxLevel = ssj.getSSJConfigs().getMaxAbilityLevel();
        
        if (currentLevel >= maxLevel) {
            player.sendMessage("§cThis ability is already at maximum level!");
            return;
        }

        int upgradeCost = calculateUpgradeCost(currentLevel);
        int currentPoints = ssj.getSSJPCM().getActionPoints(player);

        if (currentPoints < upgradeCost) {
            player.sendMessage("§cYou need " + upgradeCost + " ability points to upgrade this ability!");
            return;
        }

        // Deduct points and increase ability level
        ssj.getSSJPCM().setPlayerConfigValue(player, "Ability_Points", currentPoints - upgradeCost);
        ssj.getSSJPCM().setAbilityLevel(player, abilityName, currentLevel + 1);
        
        player.sendMessage("§aSuccessfully upgraded " + abilityName + " to level " + (currentLevel + 1));
        
        // Update stats and UI
        ssj.getSSJRpgSys().multBP(player);
        ssj.getSSJMethodChecks().scoreBoardCheck();
        ssj.getSSJMethods().callScoreboard(player);
    }

    private int calculateUpgradeCost(int currentLevel) {
        // Base cost is 1, increases by 1 for each level
        return currentLevel + 1;
    }
} 