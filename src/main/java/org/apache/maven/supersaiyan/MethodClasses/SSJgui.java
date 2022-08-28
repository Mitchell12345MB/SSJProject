package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class SSJgui{

    private final SSJ ssj;

    public SSJgui(SSJ ssj) {
        this.ssj = ssj;
    }

    public Inventory inv;

    private void SSJguicall() {

        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 9, "Example");

        // Put the items into the inventory
        initializeItems();

    }

    // You can call this whenever you want to put the items in
    private void initializeItems() {

        inv.addItem(createGuiItem(Material.DIAMOND_SWORD, "Example Sword", "§aFirst line of the lore", "§bSecond line of the lore"));

        inv.addItem(createGuiItem(Material.IRON_HELMET, "§bExample Helmet", "§aFirst line of the lore", "§bSecond line of the lore"));

    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {

        final ItemStack item = new ItemStack(material, 1);

        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {

        SSJguicall();

        ent.openInventory(inv);
    }
}
