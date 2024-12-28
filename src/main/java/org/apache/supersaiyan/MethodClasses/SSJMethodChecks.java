package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SSJMethodChecks implements Listener {

    private final SSJ ssj;

    public SSJMethodChecks(SSJ ssj) {
        this.ssj = ssj;
        Bukkit.getPluginManager().registerEvents(this, ssj);
    }

    public void checkStartCommand(Player player) {
        if (ssj.getSSJPCM().getFile(player).exists() && ssj.getSSJPCM().getStart(player)) {
            player.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");
            return;
        }

        if (!ssj.getSSJPCM().getFile(player).exists()) {
            player.sendMessage(ChatColor.RED + "Your player file doesn't exist!");
            player.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");
            return;
        }

        if (ssj.getSSJPCM().getFile(player).exists() && !ssj.getSSJPCM().getStart(player)) {
            // Reset stats first
            ssj.getSSJRpgSys().resetAllStatBoosts(player);
            
            // Set initial stats and action points
            int startingStatPoints = ssj.getSSJConfigs().getSSP();
            int startingActionPoints = ssj.getSSJConfigs().getSAP();
            
            ssj.getSSJPCM().setPlayerConfigValue(player, "Action_Points", startingActionPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Health", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Power", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Strength", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Speed", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Stamina", startingStatPoints);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Base.Defence", startingStatPoints);
            
            // Give starting items and set start to true
            ssj.getSSJMethods().callStartingItems(player);
            ssj.getSSJPCM().setPlayerConfigValue(player, "Start", true);
            
            // Update stats and open inventory
            ssj.getSSJRpgSys().updateAllStatBoosts(player);
            ssj.getSSJGui().openGenStatInventory(player);
            
            String message = String.format("%sYour Saiyan journey has started!\n%sYou have been given %d Action Points!\n%sAll your base stats have been set to %d!",
                ChatColor.GREEN, ChatColor.GREEN, startingActionPoints, ChatColor.GREEN, startingStatPoints);
            player.sendMessage(message);
            ssj.getLogger().info(String.format("%s's.yml has been updated!", player.getName()));
        }
    }

    public void handleGenStatMenuClick(Player player, InventoryClickEvent event) {
        if (!event.getInventory().equals(ssj.getSSJGui().genstatinv)) {
            return;
        }

        switch (event.getRawSlot()) {
            case 3:
                handleStatIncrease(player, "Base.Health", ssj.getSSJPCM().getHealth(player));
                break;
            case 4:
                handleStatIncrease(player, "Base.Power", ssj.getSSJPCM().getPower(player));
                break;
            case 5:
                handleStatIncrease(player, "Base.Strength", ssj.getSSJPCM().getStrength(player));
                break;
            case 6:
                handleStatIncrease(player, "Base.Speed", ssj.getSSJPCM().getSpeed(player));
                break;
            case 7:
                handleStatIncrease(player, "Base.Stamina", ssj.getSSJPCM().getStamina(player));
                break;
            case 8:
                handleStatIncrease(player, "Base.Defence", ssj.getSSJPCM().getDefence(player));
                break;
            case 9:
                if (ssj.getSSJSaiyanAbilityManager().canIncreaseSaiyanAbility(player)) {
                    ssj.getSSJSaiyanAbilityManager().increaseSaiyanAbility(player);
                    ssj.getSSJGui().openGenStatInventory(player);
                }
                break;
            case 10:
                ssj.getSSJGui().openTransformationsInventory(player);
                break;
            case 11:
                ssj.getSSJGui().openSkillsInventory(player);
                break;
            case 12:
                ssj.getSSJGui().openSettingsInventory(player);
                break;
            case 17: // Back button
                ssj.getSSJGui().openGenStatInventory(player);
                break;
            default:
                break;
        }
    }

    private void handleStatIncrease(Player player, String statKey, int currentValue) {
        if (ssj.getSSJPCM().getActionPoints(player) <= 0) {
            player.sendMessage(ChatColor.RED + "You have no more action points to spend!");
            return;
        }

        ssj.getSSJPCM().setPlayerConfigValue(player, "Action_Points", ssj.getSSJPCM().getActionPoints(player) - 1);
        ssj.getSSJPCM().setPlayerConfigValue(player, statKey, currentValue + 1);
        ssj.getSSJPCM().setPlayerConfigValue(player, "Level", ssj.getSSJRpgSys().addLevel(player));
        ssj.getSSJPCM().setPlayerConfigValue(player, "Battle_Power", ssj.getSSJRpgSys().addBaseBP(player));
        
        // Update all stat boosts whenever any stat increases
        ssj.getSSJRpgSys().updateAllStatBoosts(player);
        
        ssj.getSSJGui().openGenStatInventory(player);
        checkScoreboard();
        ssj.getSSJMethods().callScoreboard(player);
        ssj.getSSJTransformationManager().reapplyCurrentForm(player);
    }

    public void handleSkillStatMenuClick(Player player, InventoryClickEvent event) {
        if (!event.getInventory().equals(ssj.getSSJGui().skillstatinv)) {
            return;
        }

        switch (event.getRawSlot()) {
            case 1:
                ssj.getSSJGui().openGenStatInventory(player);
                break;
            case 2:
                ssj.getSSJGui().openSettingsInventory(player);
                break;
            case 17: // Back button
                ssj.getSSJGui().openSkillsInventory(player);
                break;
            default:
                break;
        }
    }

    public void handleSettingsMenuClick(Player player, InventoryClickEvent event) {
        if (!event.getInventory().equals(ssj.getSSJGui().settingsinv)) {
            return;
        }

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        
        int slot = event.getRawSlot();

        // Handle Explosion Effects toggle (Slot 0)
        if (slot == 0) {
            toggleGeneralSetting(player, "See_Explosion_Effects", "Explosion Effects");
            return;
        }

        // Handle Lightning Effects toggle (Slot 1)
        if (slot == 1) {
            toggleGeneralSetting(player, "See_Lightning_Effects", "Lightning Effects");
            return;
        }

        // Handle Sound Effects toggle (Slot 2)
        if (slot == 2) {
            toggleGeneralSetting(player, "Hear_Sound_Effects", "Sound Effects");
            return;
        }

        int currentSlot = 3;

        // Staff Flight (if applicable)
        if ((player.isOp() || player.hasPermission("ssj.staff")) && slot == currentSlot) {
            boolean currentState = ssj.getSSJPCM().isStaffFlightEnabled(player);
            ssj.getSSJPCM().setStaffFlightEnabled(player, !currentState);
            ssj.getSSJGui().openSettingsInventory(player);
            ssj.getSSJMethods().toggleStaffFlight(player, !currentState);
            player.sendMessage(String.format("Staff Flight has been %s.", !currentState ? "enabled" : "disabled"));
            return;
        }

        if (player.isOp() || player.hasPermission("ssj.staff")) {
            currentSlot++;
        }

        // Saiyan Ability
        if (slot == currentSlot) {
            boolean saEnabled = ssj.getSSJPCM().isSaiyanAbilityEnabled(player);
            ssj.getSSJPCM().setSaiyanAbilityEnabled(player, !saEnabled);
            ssj.getSSJGui().openSettingsInventory(player);
            player.sendMessage(String.format("Saiyan Ability has been %s.", !saEnabled ? "enabled" : "disabled"));
            return;
        }
        currentSlot++;

        // Skills - only show if player has them unlocked
        String[] skills = {"Fly", "Jump", "Kaioken", "Potential", "God"};
        for (int i = 0; i < skills.length; i++) {
            String skillName = skills[i];
            if (slot == currentSlot + i && ssj.getSSJPCM().hasSkill(player, skillName)) {
                toggleSkillSetting(player, skillName);
                return;
            }
        }

        // Back button
        if (slot == 17) {
            ssj.getSSJGui().openGenStatInventory(player);
        }
    }

    private void toggleSkillSetting(Player player, String skillName) {
        boolean currentValue = ssj.getSSJPCM().isSkillEnabled(player, skillName);
        
        // Special handling for flight skill
        if (skillName.equals("Fly") && currentValue) {
            player.setFlying(false);
            player.setAllowFlight(false);
            ssj.getSSJEnergyManager().stopEnergyDrain(player, "flight");
            // Cancel any pending flight tasks
            Bukkit.getScheduler().cancelTasks(ssj);
            // Force update player's flight state
            Bukkit.getScheduler().runTaskLater(ssj, () -> {
                player.setAllowFlight(false);
                player.setFlying(false);
                player.teleport(player.getLocation().add(0, 0.1, 0)); // Small teleport to prevent "big jump"
            }, 1L);
        }
        
        // Update the setting after handling special cases
        ssj.getSSJPCM().setSkillEnabled(player, skillName, !currentValue);
        
        ssj.getSSJGui().openSettingsInventory(player);
        player.sendMessage(String.format("%s Skill has been %s.", skillName, !currentValue ? "enabled" : "disabled"));
    }

    private void toggleGeneralSetting(Player player, String settingKey, String settingName) {
        boolean currentValue = ssj.getSSJPCM().getPlayerConfig(player).getBoolean(settingKey, true);
        ssj.getSSJPCM().setPlayerConfigValue(player, settingKey, !currentValue);
        ssj.getSSJGui().openSettingsInventory(player);
        player.sendMessage(String.format("%s have been %s.", settingName, !currentValue ? "enabled" : "disabled"));
    }

    public void checkOnEnable() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                checkScoreboard();

                ssj.getSSJMethods().callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

                ssj.getSSJConfigs().updateConfigs();

                ssj.getSSJConfigs().loadConfigs();

            }

        }

    }

    public void checkOnDisable() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                ssj.getSSJPCM().savePlayerConfig(online, ssj.getSSJPCM().getPlayerConfig(online));

                checkScoreboard();

                ssj.getSSJConfigs().saveConfigs();

            }

        }

    }

    public void checkScoreboard() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                if (ssj.getSSJScoreBoards().hasScore(online)) {

                    ssj.getSSJScoreBoards().removeScore(online);

                }

            }

        }

    }

    public void checkPPCPlayerName(Player player) {

        if (ssj.getSSJPCM().getFile(player).exists()) {

            if (!(player.getName().equals(ssj.getSSJPCM().getName(player)))) {

                ssj.getSSJPCM().setPlayerConfigValue(player, "Playe_Name", player.getName());

                ssj.getLogger().warning(player.getName() + "'s.yml has been updated!");
            }
        }
    }

    public void callTransformationsMenuChecks(Player player, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().transformationsinv)) return;

        e.setCancelled(true);
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        String transformName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        switch (e.getRawSlot()) {
            case 26: // Back button
                ssj.getSSJGui().openGenStatInventory(player);
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
                    String unlockedTransforms = ssj.getSSJPCM().getTransformations(player);
                    if (unlockedTransforms.contains(transformId)) {
                        // If unlocked, try to unlock instead of transforming
                        ssj.getSSJTransformationManager().handleTransformationUnlockClick(player, transformId);
                    } else {
                        // If not unlocked, try to unlock
                        ssj.getSSJTransformationManager().handleTransformationUnlockClick(player, transformId);
                    }
                    // Refresh the inventory
                    ssj.getSSJGui().openTransformationsInventory(player);
                }
                break;
        }
    }

    public void callSkillsMenuChecks(Player player, InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        
        if (e.getCurrentItem().getType() == Material.BARRIER) {
            ssj.getSSJGui().openGenStatInventory(player);
            return;
        }
        
        String skillName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        
        // Check if player meets requirements for the skill
        if (!ssj.getSSJSkillManager().canUseSkill(player, skillName)) {
            player.sendMessage(ChatColor.RED + "You don't meet the requirements for this skill!");
            return;
        }
        
        if (ssj.getSSJPCM().hasSkill(player, skillName)) {
            // Handle upgrade
            int upgradeCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Upgrade_Action_Points_Cost");
            if (ssj.getSSJPCM().getActionPoints(player) >= upgradeCost) {
                ssj.getSSJPCM().setActionPoints(player, ssj.getSSJPCM().getActionPoints(player) - upgradeCost);
                ssj.getSSJSkillManager().upgradeSkill(player, skillName);
                player.sendMessage("§aUpgraded " + skillName + " to level " + ssj.getSSJSkillManager().getSkillLevel(player, skillName) + "!");
            } else {
                player.sendMessage("§cNot enough AP to upgrade " + skillName + "!");
            }
        } else if (ssj.getSSJSkillManager().canUseSkill(player, skillName)) {
            // Handle unlock
            int apCost = ssj.getSSJConfigs().getSCFile().getInt(skillName + ".Action_Points_Cost");
            if (ssj.getSSJPCM().getActionPoints(player) >= apCost) {
                ssj.getSSJPCM().setActionPoints(player, ssj.getSSJPCM().getActionPoints(player) - apCost);
                ssj.getSSJPCM().unlockSkill(player, skillName);
                player.sendMessage("§aUnlocked " + skillName + "!");
            } else {
                player.sendMessage("§cNot enough AP to unlock " + skillName + "!");
            }
        }
        
        // Refresh inventory
        ssj.getSSJGui().openSkillsInventory(player);
    }

    public void handlePlayerKillReward(Player killer, Player victim) {
        // Calculate reward based on victim's stats
        int victimBP = ssj.getSSJPCM().getBP(victim);
        int rewardAP = Math.max(1, victimBP / 1000); // 1 AP per 1000 BP, minimum 1
        
        // Award AP to killer
        int currentAP = ssj.getSSJPCM().getActionPoints(killer);
        ssj.getSSJPCM().setPlayerConfigValue(killer, "Action_Points", currentAP + rewardAP);
        
        // Notify killer
        killer.sendMessage(String.format(
            "%sYou've gained %d Action Points for defeating %s!",
            ChatColor.GREEN,
            rewardAP,
            victim.getName()
        ));
        
        // Update killer's stats and UI
        ssj.getSSJRpgSys().updateAllStatBoosts(killer);
        ssj.getSSJGui().openGenStatInventory(killer);
        checkScoreboard();
        ssj.getSSJMethods().callScoreboard(killer);
    }

    public void handleMobKillReward(Player killer, String mobType) {
        // Get mob reward configuration
        ConfigurationSection mobRewards = ssj.getSSJConfigs().getMobRewardsConfig();
        if (mobRewards == null || !mobRewards.contains(mobType)) {
            return;
        }

        // Calculate and apply rewards
        int baseReward = mobRewards.getInt(mobType + ".ap_reward", 1);
        int currentAP = ssj.getSSJPCM().getActionPoints(killer);
        
        // Apply any multipliers based on player's state
        double multiplier = 1.0;
        if (!ssj.getSSJPCM().getForm(killer).equals("Base")) {
            multiplier *= 1.5; // 50% bonus for transformed state
        }
        
        int finalReward = (int) Math.ceil(baseReward * multiplier);
        ssj.getSSJPCM().setPlayerConfigValue(killer, "Action_Points", currentAP + finalReward);
        
        // Notify player
        if (finalReward > 0) {
            killer.sendMessage(String.format(
                "%sYou've gained %d Action Points for defeating a %s!",
                ChatColor.GREEN,
                finalReward,
                mobType
            ));
        }
        
        // Update stats and UI
        ssj.getSSJRpgSys().updateAllStatBoosts(killer);
        ssj.getSSJGui().openGenStatInventory(killer);
        checkScoreboard();
        ssj.getSSJMethods().callScoreboard(killer);
    }

    public void handleEntityDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player killer = event.getEntity().getKiller();
        if (event.getEntity() instanceof Player) {
            handlePlayerKillReward(killer, (Player) event.getEntity());
        } else {
            handleMobKillReward(killer, event.getEntityType().name());
        }
    }

}