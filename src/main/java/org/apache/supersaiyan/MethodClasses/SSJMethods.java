package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SSJMethods {

    private final SSJ ssj;

    public SSJMethods(SSJ ssj) {
        this.ssj = ssj;
    }

    public void callTransformItem(Player p) {

        ItemStack transformitem = new ItemStack(Material.BLAZE_POWDER);

        p.getInventory().addItem(transformitem);

    }

    public void callDeChargeItem(Player p) {

        ItemStack detransformitem = new ItemStack(Material.PHANTOM_MEMBRANE);

        p.getInventory().addItem(detransformitem);

    }

    public void callChargeItem(Player p) {

        ItemStack chargeitem = new ItemStack(Material.MAGMA_CREAM);

        p.getInventory().addItem(chargeitem);

    }

    public void callAuraReleaseItem(Player p) {

        ItemStack aurareleaseitem = new ItemStack(Material.GHAST_TEAR);

        p.getInventory().addItem(aurareleaseitem);

    }

    public void callMenuItem(Player p) {

        ItemStack menu = new ItemStack(Material.PAPER);

        p.getInventory().addItem(menu);

    }

    public void callStartingItems(Player p) {

        callTransformItem(p);

        callDeChargeItem(p);

        callChargeItem(p);

        callAuraReleaseItem(p);

        callMenuItem(p);

    }

    public void callOtherScoreboards() {

        for (Player online : Bukkit.getOnlinePlayers()) {

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(online);

        }

    }

    public void callScoreboard(Player p) {

        SSJScoreBoards ssjsb = ssj.getSSJSB().createScore(p);

        ssjsb.setTitle("&aCurrent Stats");

        ssjsb.setSlot(9, "&7&m--------------------------------");

        ssjsb.setSlot(8, "&aPlayer&f: " + p.getName());

        ssjsb.setSlot(7, " ");

        ssjsb.setSlot(6, "Level: " + ssj.getSSJPCM().getLevel(p));

        ssjsb.setSlot(5, " ");

        ssjsb.setSlot(4, "BP: " + ssj.getSSJPCM().getBattlePower(p));

        ssjsb.setSlot(3, " ");

        ssjsb.setSlot(2, "Current Form: " + ssj.getSSJPCM().getForm(p));

        ssjsb.setSlot(1, "&7&m--------------------------------");

    }
}
