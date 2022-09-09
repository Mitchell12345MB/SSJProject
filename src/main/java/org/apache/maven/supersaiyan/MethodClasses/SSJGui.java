package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.Configs.SSJPlayerConfig;
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

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int l = user.getUserConfig().getInt("Level");

        int bp = user.getUserConfig().getInt("Battle_Power");

        int ap = user.getUserConfig().getInt("Action_Points");

        int h = user.getUserConfig().getInt("Base.Health");

        int pow = user.getUserConfig().getInt("Base.Power");

        int str = user.getUserConfig().getInt("Base.Strength");

        int spe = user.getUserConfig().getInt("Base.Speed");

        int sta = user.getUserConfig().getInt("Base.Stamina");

        int d = user.getUserConfig().getInt("Base.Defence");

        String t = user.getUserConfig().getString("Transformations_Unlocked");

        inv.addItem(createGuiItem(Material.EGG, "§aLevel", String.valueOf(l), "§aYour current level."));

        inv.addItem(createGuiItem(Material.IRON_INGOT, "§aBattle Power", String.valueOf(bp), "§aYour current battle power."));

        inv.addItem(createGuiItem(Material.EMERALD, "§aAction Points", String.valueOf(ap), "§aYour current (spendable) action points."));

        inv.addItem(createGuiItem(Material.POTION, "§aHealth", String.valueOf(h), "§aImproves your health."));

        inv.addItem(createGuiItem(Material.FIREWORK_STAR, "§bPower", String.valueOf(pow), "§aImproves how much power you have."));

        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "§bStrength", String.valueOf(str), "§aImproves your attack damage."));

        inv.addItem(createGuiItem(Material.FEATHER, "§bSpeed", String.valueOf(spe), "§aImproves your speed."));

        inv.addItem(createGuiItem(Material.LEATHER_BOOTS, "§bStamina", String.valueOf(sta), "§aImproves your stamina."));

        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bDefence", String.valueOf(d), "§aImproves your defence."));

        inv.addItem(createGuiItem(Material.GOLD_INGOT, "§bTransformations", t, "§aYour current unlocked transformations."));

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
