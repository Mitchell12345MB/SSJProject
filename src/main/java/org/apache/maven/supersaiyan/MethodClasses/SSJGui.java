package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
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

    public Inventory inv;

    private void SSJguicall(Player p) {

        inv = Bukkit.createInventory(null, 18, p.getName() + "'s Menu");

        initializeItems(p);

    }

    private void initializeItems(Player p) {

        inv.addItem(createGuiItem(Material.EGG, "§aLevel", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Level")), "§aYour current level."));

        inv.addItem(createGuiItem(Material.IRON_INGOT, "§aBattle Power", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Battle_Power")), "§aYour current battle power."));

        inv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Action_Points")), "§aYour current (spendable) action points."));

        inv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Health")), "§aImproves your health."));

        inv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Power")), "§aImproves how much power you have."));

        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Strength")), "§aImproves your attack damage."));

        inv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Speed")), "§aImproves your speed."));

        inv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Stamina")), "§aImproves your stamina."));

        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(ssj.getSSJpPc().getpConfig(p).getInt("Base.Defence")), "§aImproves your defence."));

        inv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", String.valueOf(ssj.getSSJpPc().getpConfig(p).getString("Transformations_Unlocked")), "§aYour current unlocked transformations."));

    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, 1);

        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;

    }

    public void openInventory(final Player p) {

        SSJguicall(p);

        p.openInventory(inv);
    }
}
