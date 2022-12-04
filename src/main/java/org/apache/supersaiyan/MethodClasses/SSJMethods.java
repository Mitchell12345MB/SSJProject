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

        ssjsb.setSlot(6, "Level: " + ssj.getSSJPPC(ssj, p).getLevel());

        ssjsb.setSlot(5, " ");

        ssjsb.setSlot(4, "BP: " + ssj.getSSJPPC(ssj, p).getBP());

        ssjsb.setSlot(3, " ");

        ssjsb.setSlot(2, "Current Form: " + ssj.getSSJPPC(ssj, p).getForm());

        ssjsb.setSlot(1, "&7&m--------------------------------");

    }

    public void addEnergy(Player p) {

        int adde = ssj.getSSJPPC(ssj, p).getEnergy() * ssj.getSSJPPC(ssj, p).getPower();

        if (ssj.getSSJPPC(ssj, p).getEnergy() == 0) {

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Energy", 5);

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(p);

        } else if (ssj.getSSJPPC(ssj, p).getEnergy() < ssj.getSSJPPC(ssj, p).getLimit()) {

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Energy", adde);

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(p);

        }

    }

    public void multBP(Player p) {

        int multbp = ((ssj.getSSJPPC(ssj, p).getEnergy() + ssj.getSSJPPC(ssj, p).getBaseBP()) * ssj.getSSJConfigs().getBPM());

        if (ssj.getSSJPPC(ssj, p).getBP() == 0) {

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", addBaseBP(p));

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(p);

        } else if (ssj.getSSJPPC(ssj, p).getEnergy() == 0) {

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", addBaseBP(p));

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(p);

        } else if (ssj.getSSJPPC(ssj, p).getEnergy() < ssj.getSSJPPC(ssj, p).getLimit()) {

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", multbp);

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJMethodChecks().scoreBoardCheck();

            callScoreboard(p);

        }

    }

    public int addBaseBP(Player p) {

        return ssj.getSSJPPC(ssj, p).getBaseBP() * ssj.getSSJConfigs().getBPM();
    }

    public int addLevel(Player p) {

        return ssj.getSSJPPC(ssj, p).getBaseBP() / 150;
    }
}
