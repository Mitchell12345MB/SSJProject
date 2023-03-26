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

    public Inventory inv;

    private void SSJguicall(Player p) {

        inv = Bukkit.createInventory(null, 18, p.getName() + "'s Menu");

        initializeItems(p);

    }

    private void initializeItems(Player p) {

        inv.addItem(createGuiItem(Material.EGG, "§aLevel", String.valueOf(ssj.getSSJPCM().getLevel(p)), "§aYour current level."));

        inv.addItem(createGuiItem(Material.IRON_INGOT, "§aBattle Power", String.valueOf(ssj.getSSJRpgSys().getBaseBP(p)), "§aYour current battle power."));

        inv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(ssj.getSSJPCM().getAP(p)), "§aYour current (spendable) action points."));

        inv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(ssj.getSSJPCM().getHealth(p)), "§aImproves your health."));

        inv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(ssj.getSSJPCM().getPower(p)), "§aImproves how much power you have."));

        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(ssj.getSSJPCM().getStrength(p)), "§aImproves your attack damage."));

        inv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(ssj.getSSJPCM().getSpeed(p)), "§aImproves your speed."));

        inv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(ssj.getSSJPCM().getStamina(p)), "§aImproves your stamina."));

        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(ssj.getSSJPCM().getDefence(p)), "§aImproves your defence."));

        inv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", ssj.getSSJPCM().getTransformations(p), "§aYour current unlocked transformations."));

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

    public void openInventory(final Player p) {

        SSJguicall(p);

        p.openInventory(inv);
    }
}
