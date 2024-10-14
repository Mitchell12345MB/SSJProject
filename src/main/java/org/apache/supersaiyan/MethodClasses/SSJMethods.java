package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SSJMethods {

    private final SSJ ssj;
    private final Map<Material, Consumer<Player>> itemActions = new HashMap<>();

    public SSJMethods(SSJ ssj) {
        this.ssj = ssj;
        initializeItemActions();
    }

    private void initializeItemActions() {
        itemActions.put(Material.BLAZE_POWDER, this::giveTransformItem);
        itemActions.put(Material.PHANTOM_MEMBRANE, this::giveDeChargeItem);
        itemActions.put(Material.MAGMA_CREAM, this::giveChargeItem);
        itemActions.put(Material.GHAST_TEAR, this::giveAuraReleaseItem);
        itemActions.put(Material.PAPER, this::giveMenuItem);
        itemActions.put(Material.TNT, this::giveRemoveOrAddHoloItem);
    }

    public void giveItem(Player p, Material material) {
        itemActions.getOrDefault(material, player -> {}).accept(p);
    }

    private void giveTransformItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.BLAZE_POWDER));
    }

    private void giveDeChargeItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.PHANTOM_MEMBRANE));
    }

    private void giveChargeItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
    }

    private void giveAuraReleaseItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.GHAST_TEAR));
    }

    private void giveMenuItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.PAPER));
    }

    private void giveRemoveOrAddHoloItem(Player p) {
        p.getInventory().addItem(new ItemStack(Material.TNT));
    }

    public void callStartingItems(Player p) {
        itemActions.keySet().forEach(material -> giveItem(p, material));
    }

    public void callScoreboard(Player p) {

        if (ssj.getSSJPCM().getStart(p)) {

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
}
