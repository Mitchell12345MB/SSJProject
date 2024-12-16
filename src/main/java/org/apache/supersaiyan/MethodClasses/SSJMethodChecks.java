package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SSJMethodChecks {

    private final SSJ ssj;

    public SSJMethodChecks(SSJ ssj) {

        this.ssj = ssj;

    }

    public void checkStartCommandMethod(Player p) {
        if (ssj.getSSJPCM().getFile(p).exists() && ssj.getSSJPCM().getStart(p)) {
            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");
            return;
        }

        if (!ssj.getSSJPCM().getFile(p).exists()) {
            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");
            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");
            return;
        }

        if (ssj.getSSJPCM().getFile(p).exists() && !ssj.getSSJPCM().getStart(p)) {
            // Reset stats first
            ssj.getSSJRpgSys().resetAllStatBoosts(p);
            
            // Set initial stats and action points
            int startingStatPoints = ssj.getSSJConfigs().getSSP();
            int startingActionPoints = ssj.getSSJConfigs().getSAP();
            
            ssj.getSSJPCM().setPlayerConfigValue(p, "Action_Points", startingActionPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Health", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Power", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Strength", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Speed", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Stamina", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Base.Defence", startingStatPoints);
            
            // Give starting items and set start to true
            ssj.getSSJMethods().callStartingItems(p);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Start", true);
            
            // Update stats and open inventory
            ssj.getSSJRpgSys().updateAllStatBoosts(p);
            ssj.getSSJGui().openGenStatInventory(p);
            
            p.sendMessage(ChatColor.GREEN + "Your Saiyan journey has started!");
            p.sendMessage(ChatColor.GREEN + "You have been given " + startingActionPoints + " Action Points!");
            p.sendMessage(ChatColor.GREEN + "All your base stats have been set to " + startingStatPoints + "!");
            ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");
        }
    }

    public void callGenStatMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().genstatinv)) return;

        switch (e.getRawSlot()) {
            case 3:
                handleStatIncrease(p, "Base.Health", ssj.getSSJPCM().getHealth(p));
                break;
            case 4:
                handleStatIncrease(p, "Base.Power", ssj.getSSJPCM().getPower(p));
                break;
            case 5:
                handleStatIncrease(p, "Base.Strength", ssj.getSSJPCM().getStrength(p));
                break;
            case 6:
                handleStatIncrease(p, "Base.Speed", ssj.getSSJPCM().getSpeed(p));
                break;
            case 7:
                handleStatIncrease(p, "Base.Stamina", ssj.getSSJPCM().getStamina(p));
                break;
            case 8:
                handleStatIncrease(p, "Base.Defence", ssj.getSSJPCM().getDefence(p));
                break;
            case 9:
                if (ssj.getSSJSaiyanAbilityManager().canIncreaseSaiyanAbility(p)) {
                    ssj.getSSJSaiyanAbilityManager().increaseSaiyanAbility(p);
                    ssj.getSSJGui().openGenStatInventory(p);
                }
                break;
            case 10:
                ssj.getSSJGui().openTransformationsInventory(p);
                break;
            case 11:
                ssj.getSSJGui().openSkillsInventory(p);
                break;
            case 12:
                ssj.getSSJGui().openSettingsInventory(p);
                break;
            case 17: // Back button
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            default:
                break;
        }
    }

    private void handleStatIncrease(Player p, String statKey, int currentValue) {
        if (ssj.getSSJPCM().getActionPoints(p) > 0) {
            ssj.getSSJPCM().setPlayerConfigValue(p, "Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);
            ssj.getSSJPCM().setPlayerConfigValue(p, statKey, currentValue + 1);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Level", ssj.getSSJRpgSys().addLevel(p));
            ssj.getSSJPCM().setPlayerConfigValue(p, "Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));
            
            // Update all stat boosts whenever any stat increases
            ssj.getSSJRpgSys().updateAllStatBoosts(p);
            
            ssj.getSSJGui().openGenStatInventory(p);
            scoreBoardCheck();
            ssj.getSSJMethods().callScoreboard(p);
            ssj.getSSJTransformationManager().reapplyCurrentForm(p);
        } else {
            p.sendMessage(ChatColor.RED + "You have no more action points to spend!");
        }
    }

    public void callSkillStatMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().skillstatinv)) return;

        switch (e.getRawSlot()) {
            case 1:
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            case 2:
                ssj.getSSJGui().openSettingsInventory(p);
                break;
            case 17: // Back button
                ssj.getSSJGui().openSkillsInventory(p);
                break;
            default:
                break;
        }
    }

    public void callSettingsMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().settingsinv)) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        int slot = e.getRawSlot();

        // Handle Explosion Effects toggle (Slot 0)
        if (slot == 0) {
            toggleGeneralSetting(p, "See_Explosion_Effects", "Explosion Effects");
            return;
        }

        // Handle Lightning Effects toggle (Slot 1)
        if (slot == 1) {
            toggleGeneralSetting(p, "See_Lightning_Effects", "Lightning Effects");
            return;
        }

        // Handle Sound Effects toggle (Slot 2)
        if (slot == 2) {
            toggleGeneralSetting(p, "Hear_Sound_Effects", "Sound Effects");
            return;
        }

        int currentSlot = 3;

        // Staff Flight (if applicable)
        if ((p.isOp() || p.hasPermission("ssj.staff")) && slot == currentSlot) {
            boolean currentState = ssj.getSSJPCM().isStaffFlightEnabled(p);
            ssj.getSSJPCM().setStaffFlightEnabled(p, !currentState);
            ssj.getSSJGui().openSettingsInventory(p);
            ssj.getSSJMethods().toggleStaffFlight(p, !currentState);
            p.sendMessage("Staff Flight has been " + (!currentState ? "enabled" : "disabled") + ".");
            return;
        }

        if (p.isOp() || p.hasPermission("ssj.staff")) {
            currentSlot++;
        }

        // Saiyan Ability
        if (slot == currentSlot) {
            boolean saEnabled = ssj.getSSJPCM().isSaiyanAbilityEnabled(p);
            ssj.getSSJPCM().setSaiyanAbilityEnabled(p, !saEnabled);
            ssj.getSSJGui().openSettingsInventory(p);
            p.sendMessage("Saiyan Ability has been " + (!saEnabled ? "enabled" : "disabled") + ".");
            return;
        }
        currentSlot++;

        // Skills
        String[] skills = {"Fly", "Jump", "Kaioken", "Potential", "God"};
        for (int i = 0; i < skills.length; i++) {
            if (slot == currentSlot + i) {
                String skillName = skills[i];
                toggleSkillSetting(p, skillName);
                return;
            }
        }

        // Back button
        if (slot == 17) {
            ssj.getSSJGui().openGenStatInventory(p);
        }
    }

    private void toggleSkillSetting(Player p, String skillName) {
        boolean currentValue = ssj.getSSJPCM().isSkillEnabled(p, skillName);
        ssj.getSSJPCM().setSkillEnabled(p, skillName, !currentValue);
        
        // Special handling for flight skill
        if (skillName.equals("Fly") && currentValue) {
            // If disabling flight, stop flying and energy drain
            p.setFlying(false);
            p.setAllowFlight(false);
            ssj.getSSJEnergyManager().stopEnergyDrain(p, "flight");
        }
        
        ssj.getSSJGui().openSettingsInventory(p);
        p.sendMessage(skillName + " Skill has been " + (!currentValue ? "enabled" : "disabled") + ".");
    }

    private void toggleGeneralSetting(Player p, String settingKey, String settingName) {
        boolean currentValue = ssj.getSSJPCM().getPlayerConfig(p).getBoolean(settingKey, true);
        ssj.getSSJPCM().setPlayerConfigValue(p, settingKey, !currentValue);
        ssj.getSSJGui().openSettingsInventory(p);
        p.sendMessage(settingName + " have been " + (!currentValue ? "enabled" : "disabled") + ".");
    }

    public void onEnableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

                ssj.getSSJConfigs().updateConfigs();

                ssj.getSSJConfigs().loadConfigs();

            }

        }

    }

    public void onDisableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                ssj.getSSJPCM().savePlayerConfig(online, ssj.getSSJPCM().getPlayerConfig(online));

                scoreBoardCheck();

                ssj.getSSJConfigs().saveConfigs();

            }

        }

    }

    public void scoreBoardCheck() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                if (ssj.getSSJSB().hasScore(online)) {

                    ssj.getSSJSB().removeScore(online);

                }

            }

        }

    }

    public void checkPPCPlayerName(Player p) {

        if (ssj.getSSJPCM().getFile(p).exists()) {

            if (!(p.getName().equals(ssj.getSSJPCM().getName(p)))) {

                ssj.getSSJPCM().setPlayerConfigValue(p, "Playe_Name", p.getName());

                ssj.getLogger().warning(p.getName() + "'s.yml has been updated!");
            }
        }
    }

    public void callTransformationsMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().transformationsinv)) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String transformName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        switch (e.getRawSlot()) {
            case 26: // Back button
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            default:
                // Get the transformation ID from the name
                String transformId = null;
                String[] categories = {"Base_Forms", "Kaioken_Forms", "Saiyan_Forms", 
                                     "Legendary_Saiyan_Forms", "Saiyan_God_Forms"};
                
                for (String category : categories) {
                    ConfigurationSection section = ssj.getSSJConfigs().getTCFile().getConfigurationSection(category);
                    if (section != null) {
                        for (String key : section.getKeys(false)) {
                            String desc = section.getString(key + ".Desc");
                            if (desc != null && desc.equals(transformName)) {
                                transformId = section.getString(key + ".TransformationID");
                                break;
                            }
                        }
                    }
                    if (transformId != null) break;
                }

                if (transformId != null) {
                    // Check if the transformation is already unlocked
                    String unlockedTransforms = ssj.getSSJPCM().getTransformations(p);
                    if (unlockedTransforms.contains(transformId)) {
                        // If unlocked, try to transform
                        if (ssj.getSSJTransformationManager().canTransform(p, transformId)) {
                            ssj.getSSJTransformationManager().transform(p, transformId);
                        }
                    } else {
                        // If not unlocked, try to unlock
                        ssj.getSSJTransformationManager().handleTransformationUnlockClick(p, transformId);
                    }
                    // Refresh the inventory
                    ssj.getSSJGui().openTransformationsInventory(p);
                }
                break;
        }
    }

    public void callSkillsMenuChecks(Player p, InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        
        if (e.getCurrentItem().getType() == Material.BARRIER) {
            ssj.getSSJGui().openGenStatInventory(p);
            return;
        }
        
        String skillName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        if (ssj.getSSJPCM().hasSkill(p, skillName)) {
            // Handle upgrade
            int upgradeCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Upgrade_Acion_Points_Cost");
            if (ssj.getSSJPCM().getActionPoints(p) >= upgradeCost) {
                ssj.getSSJPCM().setActionPoints(p, ssj.getSSJPCM().getActionPoints(p) - upgradeCost);
                ssj.getSSJSkillManager().upgradeSkill(p, skillName);
                p.sendMessage("§aUpgraded " + skillName + " to level " + ssj.getSSJSkillManager().getSkillLevel(p, skillName) + "!");
            } else {
                p.sendMessage("§cNot enough AP to upgrade " + skillName + "!");
            }
        } else if (ssj.getSSJSkillManager().canUseSkill(p, skillName)) {
            // Handle unlock
            int apCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Acion_Points_Cost");
            ssj.getSSJPCM().setActionPoints(p, ssj.getSSJPCM().getActionPoints(p) - apCost);
            ssj.getSSJPCM().unlockSkill(p, skillName);
            p.sendMessage("§aUnlocked " + skillName + "!");
        }
        
        // Refresh inventory
        ssj.getSSJGui().openSkillsInventory(p);
    }

    public void handlePlayerKillReward(Player killer, Player victim) {
        // Get levels
        int killerLevel = ssj.getSSJPCM().getLevel(killer);
        int victimLevel = ssj.getSSJPCM().getLevel(victim);
        
        // Get reward values from config
        int baseReward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Player_Kills.Base_Reward", 5);
        int levelBonus = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Player_Kills.Level_Bonus", 2);
        
        // Calculate level difference and reward
        int levelDifference = victimLevel - killerLevel;
        int reward;
        if (levelDifference > 0) {
            reward = baseReward + (levelDifference * levelBonus);
        } else {
            reward = Math.max(1, baseReward + levelDifference);
        }
        
        // Get current AP and add reward
        int currentAP = ssj.getSSJPCM().getActionPoints(killer);
        int newAP = currentAP + reward;
        
        // Save new AP value using PCM
        ssj.getSSJPCM().setActionPoints(killer, newAP);
        
        // Notify killer
        killer.sendMessage(ChatColor.GREEN + "You gained " + reward + " Action Points for defeating " + victim.getName() + "!");
    }

    public void handleMobKillReward(Player killer, String mobType) {
        int reward;
        
        // Get reward values from config
        switch(mobType.toUpperCase()) {
            case "ENDER_DRAGON":
            case "WITHER":
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Boss_Mobs.Reward", 25);
                break;
                
            case "ELDER_GUARDIAN":
            case "WARDEN":
            case "RAVAGER":
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Strong_Mobs.Reward", 15);
                break;
                
            case "BLAZE":
            case "WITCH":
            case "VINDICATOR":
            case "PIGLIN_BRUTE":
            case "IRON_GOLEM":
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Medium_Strong_Mobs.Reward", 10);
                break;
                
            case "ZOMBIE":
            case "SKELETON":
            case "SPIDER":
            case "CREEPER":
            case "ENDERMAN":
            case "PIGLIN":
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Medium_Mobs.Reward", 5);
                break;
                
            case "CHICKEN":
            case "COW":
            case "PIG":
            case "SHEEP":
            case "RABBIT":
            case "BAT":
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Weak_Mobs.Reward", 1);
                break;
                
            default:
                reward = ssj.getSSJConfigs().getCFile().getInt("Action_Points_Rewards.Mob_Kills.Default_Reward", 2);
                break;
        }
        
        // Get current AP and add reward
        int currentAP = ssj.getSSJPCM().getActionPoints(killer);
        int newAP = currentAP + reward;
        
        // Save new AP value using PCM
        ssj.getSSJPCM().setActionPoints(killer, newAP);
        
        // Debug log
        ssj.getLogger().info("Adding " + reward + " AP to " + killer.getName() + " for killing " + mobType);
        ssj.getLogger().info("Old AP: " + currentAP + ", New AP: " + newAP);
        
        // Notify killer
        killer.sendMessage(ChatColor.GREEN + "You gained " + reward + " Action Points for killing a " + mobType.toLowerCase().replace("_", " ") + "!");
    }

    public void handleEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;  // Only proceed if killed by a player
        
        Player killer = event.getEntity().getKiller();
        
        if (event.getEntity() instanceof Player) {
            // Player kill
            Player victim = (Player) event.getEntity();
            handlePlayerKillReward(killer, victim);
        } else {
            // Mob kill
            String mobType = event.getEntityType().name();
            handleMobKillReward(killer, mobType);
        }
    }

}