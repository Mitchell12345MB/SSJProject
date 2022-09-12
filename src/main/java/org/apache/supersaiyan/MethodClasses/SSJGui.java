package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.Configs.SSJPlayerConfig;
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

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        inv.addItem(createGuiItem(Material.EGG, "§aLevel", String.valueOf(user.getLevel()), "§aYour current level."));

        inv.addItem(createGuiItem(Material.IRON_INGOT, "§aBattle Power", String.valueOf(user.getBaseBP()), "§aYour current battle power."));

        inv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(user.getAP()), "§aYour current (spendable) action points."));

        inv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(user.getHealth()), "§aImproves your health."));

        inv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(user.getPower()), "§aImproves how much power you have."));

        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(user.getStrength()), "§aImproves your attack damage."));

        inv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(user.getSpeed()), "§aImproves your speed."));

        inv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(user.getStamina()), "§aImproves your stamina."));

        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(user.getDefence()), "§aImproves your defence."));

        inv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", user.getTransformations(), "§aYour current unlocked transformations."));

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
