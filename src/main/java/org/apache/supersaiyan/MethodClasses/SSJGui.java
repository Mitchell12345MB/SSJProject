package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SSJGui {

    private final SSJ ssj;

    public SSJGui(SSJ ssj) {
        this.ssj = ssj;
    }

    public Inventory genstatinv;
    public Inventory skillstatinv;
    public Inventory settingsinv;
    public Inventory transformationsinv;
    public Inventory skillsinv;

    private void SSJGenStatInvCall(Player p) {
        genstatinv = Bukkit.createInventory(null, 18, p.getName() + "'s Stats");
        initializeGenStatInvItems(p);
    }

    private void SSJSkillStatInvCall(Player p) {
        skillstatinv = Bukkit.createInventory(null, 9, p.getName() + "'s Skills");
        initializeSkillStatInvItems(p);
    }

    private void SSJSettingsInvCall(Player p) {
        settingsinv = Bukkit.createInventory(null, 18, p.getName() + "'s Settings");
        initializeSettingsInvItems(p);
    }

    private void SSJTransformationsInvCall(Player p) {
        transformationsinv = Bukkit.createInventory(null, 27, p.getName() + "'s Transformations");
        initializeTransformationsInvItems(p);
    }

    private void SSJSkillsInvCall(Player p) {
        skillsinv = Bukkit.createInventory(null, 54, p.getName() + "'s Skills");
        FileConfiguration skillConfig = ssj.getSSJConfigs().getSCFile();

        for (String skillName : skillConfig.getKeys(false)) {
            ConfigurationSection skillSection = skillConfig.getConfigurationSection(skillName);
            if (skillSection == null) continue;

            boolean hasSkill = ssj.getSSJPCM().hasSkill(p, skillName);
            int skillLevel = hasSkill ? ssj.getSSJPCM().getSkillLevel(p, skillName) : 0;
            int apCost = skillSection.getInt("Acion_Points_Cost");
            int upgradeCost = skillSection.getInt("Upgrade_Acion_Points_Cost") + skillLevel;

            List<String> lore = new ArrayList<>();
            lore.add("§7" + skillSection.getString("Desc", "No description"));
            lore.add("");
            
            // Add stat requirements to lore
            ConfigurationSection statReq = skillSection.getConfigurationSection("Stat_Requirements");
            if (statReq != null) {
                lore.add("§eRequirements:");
                for (String stat : statReq.getKeys(false)) {
                    int required = statReq.getInt(stat);
                    int playerStat = ssj.getSSJSkillManager().getPlayerStat(p, stat);
                    String color = playerStat >= required ? "§a" : "§c";
                    lore.add(color + "- " + stat + ": " + required);
                }
            }

            if (hasSkill) {
                addUpgradeInfo(lore, p, skillName, skillLevel, upgradeCost);
            } else {
                addUnlockInfo(lore, p, apCost);
            }

            skillsinv.addItem(createGuiItem(Material.BOOK, "§b" + skillName, lore.toArray(new String[0])));
        }

        // Add back button
        skillsinv.setItem(53, createGuiItem(Material.BARRIER, "§cBack", "§7Return to previous menu"));
    }

    private void initializeGenStatInvItems(Player p) {
        genstatinv.addItem(createGuiItem(
            Material.EGG, 
            "§6§lLevel", 
            "§7Your current level is: §e" + ssj.getSSJPCM().getLevel(p), 
            "§7Level up to unlock new abilities!"
        ));
        genstatinv.addItem(createGuiItem(
            Material.IRON_INGOT, 
            "§b§lBattle Power", 
            "§7Your current battle power is: §e" + ssj.getSSJRpgSys().getBaseBP(p), 
            "§7Increase your battle power to become stronger."
        ));
        genstatinv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(ssj.getSSJPCM().getActionPoints(p)), "§aYour current (spendable) action points."));
        genstatinv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(ssj.getSSJPCM().getHealth(p)), "§aImproves your health."));
        genstatinv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(ssj.getSSJPCM().getPower(p)), "§aImproves how much power you have."));
        genstatinv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(ssj.getSSJPCM().getStrength(p)), "§aImproves your attack damage."));
        genstatinv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(ssj.getSSJPCM().getSpeed(p)), "§aImproves your speed."));
        genstatinv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(ssj.getSSJPCM().getStamina(p)), "§aImproves your stamina."));
        genstatinv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(ssj.getSSJPCM().getDefence(p)), "§aImproves your defence."));
        genstatinv.addItem(createGuiItem(
            Material.FIRE_CHARGE, 
            "§bSaiyan Ability", 
            String.valueOf(ssj.getSSJPCM().getSaiyanAbility(p)),
            "§aImproves transformation speed and unlocks more saiyan transformations.",
            "§eCost to upgrade: " + ssj.getSSJSaiyanAbilityManager().calculateSaiyanAbilityCost(p) + " AP"
        ));
        
        genstatinv.addItem(createGuiItem(Material.NETHER_STAR, "§bTransformations", "§aView your transformations."));
        genstatinv.addItem(createGuiItem(Material.BOOK, "§bSkills", "§aView available skills."));
        genstatinv.addItem(createGuiItem(Material.REDSTONE, "§bSettings", "§aAccess your settings."));
    }

    private void initializeTransformationsInvItems(Player p) {
        transformationsinv.clear();
        FileConfiguration transformConfig = ssj.getSSJConfigs().getTCFile();

        for (String category : transformConfig.getKeys(false)) {
            ConfigurationSection categorySection = transformConfig.getConfigurationSection(category);
            if (categorySection == null) continue;

            for (String transformName : categorySection.getKeys(false)) {
                ConfigurationSection transformSection = categorySection.getConfigurationSection(transformName);
                if (transformSection == null) continue;

                String transformationId = transformSection.getString("TransformationID");
                if (transformationId == null || transformationId.equals("base")) continue; // Skip base form

                // Check if player has required skills to see the transformation
                boolean canSee = true;
                boolean hasRequirements = true;

                // Check Kaioken requirement
                if (transformSection.contains("Kaioken_Ability_Lock")) {
                    if (!ssj.getSSJPCM().hasSkill(p, "Kaioken")) {
                        canSee = false;
                    } else {
                        int requiredLevel = transformSection.getInt("Kaioken_Ability_Lock");
                        int playerLevel = ssj.getSSJSkillManager().getSkillLevel(p, "Kaioken");
                        hasRequirements = hasRequirements && (playerLevel >= requiredLevel);
                    }
                }

                // Check God requirement
                if (transformSection.contains("God_Ability_Lock")) {
                    if (!ssj.getSSJPCM().hasSkill(p, "God")) {
                        canSee = false;
                    } else {
                        int requiredLevel = transformSection.getInt("God_Ability_Lock");
                        int playerLevel = ssj.getSSJSkillManager().getSkillLevel(p, "God");
                        hasRequirements = hasRequirements && (playerLevel >= requiredLevel);
                    }
                }

                // Check Potential requirement
                if (transformSection.contains("Potential_Skill_Lock")) {
                    if (!ssj.getSSJPCM().hasSkill(p, "Potential")) {
                        canSee = false;
                    } else {
                        int requiredLevel = transformSection.getInt("Potential_Skill_Lock");
                        int playerLevel = ssj.getSSJSkillManager().getSkillLevel(p, "Potential");
                        hasRequirements = hasRequirements && (playerLevel >= requiredLevel);
                    }
                }

                // Check Saiyan Ability requirement
                if (transformSection.contains("Saiyan_Ability_Lock")) {
                    int requiredLevel = transformSection.getInt("Saiyan_Ability_Lock");
                    int playerLevel = ssj.getSSJPCM().getSaiyanAbility(p);
                    hasRequirements = hasRequirements && (playerLevel >= requiredLevel);
                }

                // Check Level requirement
                if (transformSection.contains("Level_Lock")) {
                    int requiredLevel = transformSection.getInt("Level_Lock");
                    int playerLevel = ssj.getSSJPCM().getLevel(p);
                    hasRequirements = hasRequirements && (playerLevel >= requiredLevel);
                }

                // Skip if player can't see this transformation
                if (!canSee) {
                    continue;
                }

                // Create transformation item
                String desc = transformSection.getString("Desc", "Unknown Transformation");
                Material material = Material.BLAZE_POWDER;
                List<String> lore = new ArrayList<>();

                // Check if transformation is unlocked
                boolean isUnlocked = ssj.getSSJPCM().getTransformations(p).contains(transformationId);

                if (isUnlocked) {
                    lore.add("§aUnlocked");
                } else {
                    if (hasRequirements) {
                        int apCost = transformSection.getInt("Acion_Points_Cost", 0);
                        int playerAP = ssj.getSSJPCM().getActionPoints(p);
                        lore.add("§eRequirements met!");
                        lore.add("§eCost: " + apCost + " Action Points");
                        if (playerAP >= apCost) {
                            lore.add("§aClick to unlock!");
                        } else {
                            lore.add("§cNot enough Action Points!");
                        }
                    } else {
                        lore.add("§cRequirements not met:");
                        if (transformSection.contains("Level_Lock")) {
                            int required = transformSection.getInt("Level_Lock");
                            int current = ssj.getSSJPCM().getLevel(p);
                            lore.add(current >= required ? "§a" : "§c" + "Level: " + current + "/" + required);
                        }
                        if (transformSection.contains("Saiyan_Ability_Lock")) {
                            int required = transformSection.getInt("Saiyan_Ability_Lock");
                            int current = ssj.getSSJPCM().getSaiyanAbility(p);
                            lore.add(current >= required ? "§a" : "§c" + "Saiyan Ability: " + current + "/" + required);
                        }
                        if (transformSection.contains("Kaioken_Ability_Lock")) {
                            int required = transformSection.getInt("Kaioken_Ability_Lock");
                            int current = ssj.getSSJSkillManager().getSkillLevel(p, "Kaioken");
                            lore.add(current >= required ? "§a" : "§c" + "Kaioken: " + current + "/" + required);
                        }
                        if (transformSection.contains("God_Ability_Lock")) {
                            int required = transformSection.getInt("God_Ability_Lock");
                            int current = ssj.getSSJSkillManager().getSkillLevel(p, "God");
                            lore.add(current >= required ? "§a" : "§c" + "God: " + current + "/" + required);
                        }
                    }
                }

                ItemStack item = createGuiItem(material, "§b" + desc, lore.toArray(new String[0]));
                transformationsinv.addItem(item);
            }
        }

        // Add back button
        transformationsinv.setItem(26, createGuiItem(Material.BARRIER, "§cBack to Stats", "§aReturn to stats menu."));
    }

    private void initializeSkillStatInvItems(Player p) {
        skillstatinv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", ssj.getSSJPCM().getTransformations(p), "§aYour current unlocked transformations."));
        skillstatinv.addItem(createGuiItem(Material.GREEN_DYE, "§bStat Page", "§aGoes to your stat page."));
        skillstatinv.addItem(createGuiItem(Material.RED_DYE, "§bSettings Page", "§aGoes to your settings page."));
        skillstatinv.setItem(9, createGuiItem(Material.BARRIER, "§cBack", "§aReturn to previous menu."));
    }

    private void initializeSettingsInvItems(Player p) {
        settingsinv.clear();

        // Back button
        settingsinv.setItem(17, createGuiItem(Material.BARRIER, "§cBack", "§aReturn to previous menu."));

        // Slot assignments:
        // 0 to 2 - Explosion Effects, Lightning Effects, Sound Effects
        // 3 - Staff Flight (if applicable)
        // 4 - Saiyan Ability
        // 5 to 9 - Skill Toggles (Fly, Jump, Kaioken, Potential, God)

        // Add Explosion Effects setting
        settingsinv.setItem(0, createToggleItem(
            Material.GOLD_INGOT,
            "§bExplosion Effects",
            ssj.getSSJPCM().getExplosionEffects(p)
        ));

        // Add Lightning Effects setting
        settingsinv.setItem(1, createToggleItem(
            Material.IRON_INGOT,
            "§bLightning Effects",
            ssj.getSSJPCM().getLightningEffects(p)
        ));

        // Add Sound Effects setting
        settingsinv.setItem(2, createToggleItem(
            Material.DIAMOND,
            "§bSound Effects",
            ssj.getSSJPCM().getSoundEffects(p)
        ));

        int currentSlot = 3;

        // If the player has staff permissions, add Staff Flight item
        if (p.isOp() || p.hasPermission("ssj.staff")) {
            ItemStack staffFlightItem = createStaffFlightItem(p);
            settingsinv.setItem(currentSlot, staffFlightItem);
            currentSlot++;
        }

        // Place Saiyan Ability item at the next slot
        ItemStack saiyanAbilityItem = createToggleItem(
            Material.FIRE_CORAL_FAN,
            ChatColor.GOLD + "Saiyan Ability",
            ssj.getSSJPCM().isSaiyanAbilityEnabled(p)
        );
        settingsinv.setItem(currentSlot, saiyanAbilityItem);
        currentSlot++;

        // Define the skills and their materials
        String[] skills = {"Fly", "Jump", "Kaioken", "Potential", "God"};
        Material[] materials = {
            Material.FEATHER,       // Fly
            Material.RABBIT_FOOT,   // Jump
            Material.REDSTONE,      // Kaioken
            Material.EMERALD,       // Potential
            Material.NETHER_STAR    // God
        };

        // Place skill toggle items starting from the next slot
        for (int i = 0; i < skills.length; i++) {
            ItemStack skillItem = createToggleItem(
                materials[i],
                ChatColor.GOLD + skills[i] + " Skill",
                ssj.getSSJPCM().isSkillEnabled(p, skills[i])
            );
            settingsinv.setItem(currentSlot + i, skillItem);
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openGenStatInventory(final Player p) {
        SSJGenStatInvCall(p);
        p.openInventory(genstatinv);
    }

    public void openSkillStatInventory(final Player p) {
        SSJSkillStatInvCall(p);
        p.openInventory(skillstatinv);
    }

    public void openSettingsInventory(final Player p) {
        SSJSettingsInvCall(p);
        p.openInventory(settingsinv);
    }

    public void openTransformationsInventory(final Player p) {
        SSJTransformationsInvCall(p);
        p.openInventory(transformationsinv);
    }

    public void openSkillsInventory(final Player p) {
        SSJSkillsInvCall(p);
        p.openInventory(skillsinv);
    }

    private void addUpgradeInfo(List<String> lore, Player p, String skillName, int skillLevel, int upgradeCost) {
        lore.add("");
        lore.add("§aSkill Level: " + skillLevel + "/" + ssj.getSSJSkillManager().getMaxSkillLevel(skillName));
        
        if (ssj.getSSJSkillManager().isSkillMaxLevel(p, skillName)) {
            lore.add("§6MAXIMUM LEVEL");
        } else {
            lore.add("§eUpgrade Cost: " + upgradeCost + " AP");
            if (ssj.getSSJPCM().getActionPoints(p) >= upgradeCost) {
                lore.add("§aClick to upgrade!");
            } else {
                lore.add("§cNot enough AP to upgrade!");
            }
        }
    }

    private void addUnlockInfo(List<String> lore, Player p, int apCost) {
        lore.add("");
        lore.add("§eUnlock Cost: " + apCost + " AP");
        if (ssj.getSSJPCM().getActionPoints(p) >= apCost) {
            lore.add("§aClick to unlock!");
        } else {
            lore.add("§cNot enough AP to unlock!");
        }
    }

    private ItemStack createStaffFlightItem(Player p) {
        boolean isEnabled = ssj.getSSJPCM().isStaffFlightEnabled(p);
        Material material = isEnabled ? Material.FEATHER : Material.LEAD;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GOLD + "Staff Flight");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "Toggle flight without energy.");
        lore.add(ChatColor.WHITE + "Current: " + (isEnabled ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createToggleItem(Material material, String name, boolean enabled) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(
            "§7Status: " + (enabled ? "§aEnabled" : "§cDisabled"),
            "§eClick to toggle"
        ));
        item.setItemMeta(meta);
        return item;
    }
}
