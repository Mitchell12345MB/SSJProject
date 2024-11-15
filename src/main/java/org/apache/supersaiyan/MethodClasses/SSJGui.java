package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

import java.util.Arrays;

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
        settingsinv = Bukkit.createInventory(null, 9, p.getName() + "'s Settings");
        initializeSettingsInvItems(p);
    }

    private void SSJTransformationsInvCall(Player p) {
        transformationsinv = Bukkit.createInventory(null, 27, p.getName() + "'s Transformations");
        initializeTransformationsInvItems(p);
    }

    private void SSJSkillsInvCall(Player p) {
        skillsinv = Bukkit.createInventory(null, 18, p.getName() + "'s Available Skills");
        initializeSkillsInvItems(p);
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
        genstatinv.addItem(createGuiItem(Material.FIRE_CHARGE, "§bSaiyan Ability", String.valueOf(ssj.getSSJPCM().getSaiyanAbility(p)), "§aImproves transformation speed, and unlocks more saiyan transformations."));
        
        genstatinv.addItem(createGuiItem(Material.NETHER_STAR, "§bTransformations", "§aView your transformations."));
        genstatinv.addItem(createGuiItem(Material.BOOK, "§bSkills", "§aView available skills."));
        genstatinv.addItem(createGuiItem(Material.REDSTONE, "§bSettings", "§aAccess your settings."));
    }

    private void initializeTransformationsInvItems(Player p) {
        String unlockedTransforms = ssj.getSSJPCM().getTransformations(p);
        String currentForm = ssj.getSSJPCM().getForm(p);

        // Base Forms
        if (unlockedTransforms.contains("potential")) {
            transformationsinv.addItem(createGuiItem(Material.DIAMOND, "§aPotential Unleashed", 
                "§bStatus: " + (currentForm.equals("Potential Unleashed") ? "§aActive" : "§cInactive"),
                "§aA powerful non-Saiyan transformation."));
        }

        // Kaioken Forms
        if (unlockedTransforms.contains("x1")) {
            transformationsinv.addItem(createGuiItem(Material.REDSTONE, "§cKaioken", 
                "§bStatus: " + (currentForm.contains("Kaioken") ? "§aActive" : "§cInactive"),
                "§aMultiplies your power level."));
        }

        // Saiyan Forms
        if (unlockedTransforms.contains("1")) {
            transformationsinv.addItem(createGuiItem(Material.GOLD_INGOT, "§eSuper Saiyan", 
                "§bStatus: " + (currentForm.equals("Super Saiyan") ? "§aActive" : "§cInactive"),
                "§aThe legendary Super Saiyan transformation."));
        }

        // Add back button
        transformationsinv.setItem(26, createGuiItem(Material.BARRIER, "§cBack to Stats", "§aReturn to stats menu."));
    }

    private void initializeSkillsInvItems(Player p) {
        // Add available skills from skills.yml
        if (ssj.getSSJSkillManager().canUseSkill(p, "Fly")) {
            skillsinv.addItem(createGuiItem(Material.FEATHER, "§bFlight", 
                "§aAllows you to fly.",
                "§bEnergy Cost: §e10",
                "§bRequirements:",
                "§7- Power: 10",
                "§7- Strength: 10"));
        }

        // Add more skills here as needed

        // Add back button
        skillsinv.setItem(17, createGuiItem(Material.BARRIER, "§cBack to Stats", "§aReturn to stats menu."));
    }

    private void initializeSkillStatInvItems(Player p) {
        skillstatinv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", ssj.getSSJPCM().getTransformations(p), "§aYour current unlocked transformations."));
        skillstatinv.addItem(createGuiItem(Material.GREEN_DYE, "§bStat Page", "§aGoes to your stat page."));
        skillstatinv.addItem(createGuiItem(Material.RED_DYE, "§bSettings Page", "§aGoes to your settings page."));
        skillstatinv.setItem(9, createGuiItem(Material.BARRIER, "§cBack", "§aReturn to previous menu."));
    }

    private void initializeSettingsInvItems(Player p) {
        settingsinv.addItem(createGuiItem(Material.GOLD_INGOT, "§bExplosion Effects", String.valueOf(ssj.getSSJPCM().getExplosionEffects(p)), "§aYour current explosion effects."));
        settingsinv.addItem(createGuiItem(Material.IRON_INGOT, "§bLightning Effects", String.valueOf(ssj.getSSJPCM().getLightningEffects(p)), "§aYour current lightning effects settings."));
        settingsinv.addItem(createGuiItem(Material.DIAMOND, "§bSound Effects", String.valueOf(ssj.getSSJPCM().getSoundEffects(p)), "§aYour current sound settings."));
        settingsinv.addItem(createGuiItem(Material.GREEN_DYE, "§bStat Page", "§aGoes to your stat page."));
        settingsinv.addItem(createGuiItem(Material.RED_DYE, "§bSkill Page", "§aGoes to your skill stat page."));
        settingsinv.setItem(8, createGuiItem(Material.BARRIER, "§cBack", "§aReturn to previous menu."));
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
}
