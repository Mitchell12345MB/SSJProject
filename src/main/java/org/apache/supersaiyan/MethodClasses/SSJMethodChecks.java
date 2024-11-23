package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
            ssj.getSSJRpgSys().resetAllStatBoosts(p);
            
            ssj.getSSJMethods().callStartingItems(p);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Start", true);
            ssj.getSSJGui().openGenStatInventory(p);
            
            ssj.getSSJRpgSys().updateAllStatBoosts(p);
            
            p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");
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

        switch (e.getRawSlot()) {
            case 26: // Back button
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            default:
                ItemStack clicked = e.getCurrentItem();
                if (clicked != null && clicked.hasItemMeta()) {
                    String name = clicked.getItemMeta().getDisplayName();
                    handleTransformationUpgradeClick(p, name);
                    handleTransformationUnlockClick(p, name);
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

    private void handleTransformationUpgradeClick(Player p, String transformName) {
        // Handle transformation activation based on the clicked transformation
        // This should integrate with your existing transformation system
    }

    private void handleTransformationUnlockClick(Player p, String transformName) {
        // Handle transformation activation based on the clicked transformation
        // This should integrate with your existing transformation system
    }


}