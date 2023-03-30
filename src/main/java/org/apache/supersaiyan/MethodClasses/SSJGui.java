package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SSJGui {

    private final SSJ ssj;

    public SSJGui(SSJ ssj) {
        this.ssj = ssj;
    }

    public Inventory genstatinv;

    public Inventory skillstatinv;

    public Inventory settingsinv;

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

    private void initializeGenStatInvItems(Player p) {

        genstatinv.addItem(createGuiItem(Material.EGG, "§aLevel", String.valueOf(ssj.getSSJPCM().getLevel(p)), "§aYour current level."));

        genstatinv.addItem(createGuiItem(Material.IRON_INGOT, "§aBattle Power", String.valueOf(ssj.getSSJRpgSys().getBaseBP(p)), "§aYour current battle power."));

        genstatinv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(ssj.getSSJPCM().getActionPoints(p)), "§aYour current (spendable) action points."));

        genstatinv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(ssj.getSSJPCM().getHealth(p)), "§aImproves your health."));

        genstatinv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(ssj.getSSJPCM().getPower(p)), "§aImproves how much power you have."));

        genstatinv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(ssj.getSSJPCM().getStrength(p)), "§aImproves your attack damage."));

        genstatinv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(ssj.getSSJPCM().getSpeed(p)), "§aImproves your speed."));

        genstatinv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(ssj.getSSJPCM().getStamina(p)), "§aImproves your stamina."));

        genstatinv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(ssj.getSSJPCM().getDefence(p)), "§aImproves your defence."));

        genstatinv.addItem(createGuiItem(Material.FIRE_CHARGE, "§bSaiyan Ability", String.valueOf(ssj.getSSJPCM().getSaiyanAbility(p)), "§aImproves transformation speed, and unlocks more saiyan transformations."));

        genstatinv.addItem(createGuiItem(Material.GREEN_DYE, "§bSkill Page",  "§aGoes to your skill stat page."));

        genstatinv.addItem(createGuiItem(Material.RED_DYE, "§bSettings Page", "§aGoes to your settings page."));

    }

    private void initializeSkillStatInvItems(Player p) {

        skillstatinv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", ssj.getSSJPCM().getTransformations(p), "§aYour current unlocked transformations."));

        //skill code here

        skillstatinv.addItem(createGuiItem(Material.GREEN_DYE, "§bStat Page", "§aGoes to your stat page."));

        skillstatinv.addItem(createGuiItem(Material.RED_DYE, "§bSettings Page", "§aGoes to your settings page."));

    }

    private void initializeSettingsInvItems(Player p) {

        settingsinv.addItem(createGuiItem(Material.GOLD_INGOT, "§bExplosion Effects", String.valueOf(ssj.getSSJPCM().getExplosionEffects(p)), "§aYour current explosion effects."));

        settingsinv.addItem(createGuiItem(Material.IRON_INGOT, "§bLightning Effects", String.valueOf(ssj.getSSJPCM().getLightningEffects(p)), "§aYour current lightning effects settings."));

        settingsinv.addItem(createGuiItem(Material.DIAMOND, "§bSound Effects", String.valueOf(ssj.getSSJPCM().getSoundEffects(p)), "§aYour current sound settings."));

        settingsinv.addItem(createGuiItem(Material.GREEN_DYE, "§bStat Page", "§aGoes to your stat page."));

        settingsinv.addItem(createGuiItem(Material.RED_DYE, "§bSkill Page", "§aGoes to your skill stat page."));

    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, 1);

        final ItemMeta meta = item.getItemMeta();

        assert meta != null;

        meta.setDisplayName(name);

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
}
