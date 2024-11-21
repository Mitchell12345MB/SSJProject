package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.Bukkit;

import java.util.Arrays;
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
        ItemStack transformItem = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta transformMeta = transformItem.getItemMeta();
        transformMeta.setDisplayName(ChatColor.GOLD + "✧ " + ChatColor.YELLOW + "Transform" + ChatColor.GOLD + " ✧");
        transformMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to transform",
            ChatColor.GRAY + "into your next available form",
            "",
            ChatColor.YELLOW + "Current Form: " + ChatColor.WHITE + ssj.getSSJPCM().getForm(p)
        ));
        transformItem.setItemMeta(transformMeta);
        p.getInventory().addItem(transformItem);
    }

    private void giveDeChargeItem(Player p) {
        ItemStack dechargeItem = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta dechargeMeta = dechargeItem.getItemMeta();
        dechargeMeta.setDisplayName(ChatColor.RED + "❈ " + ChatColor.DARK_RED + "Power Down" + ChatColor.RED + " ❈");
        dechargeMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to revert",
            ChatColor.GRAY + "back to base form"
        ));
        dechargeItem.setItemMeta(dechargeMeta);
        p.getInventory().addItem(dechargeItem);
    }

    private void giveChargeItem(Player p) {
        ItemStack chargeItem = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta chargeMeta = chargeItem.getItemMeta();
        chargeMeta.setDisplayName(ChatColor.AQUA + "⚡ " + ChatColor.BLUE + "Charge Energy" + ChatColor.AQUA + " ⚡");
        chargeMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to charge your energy",
            ChatColor.GRAY + "Hold to continue charging",
            "",
            ChatColor.AQUA + "Current Energy: " + ChatColor.WHITE + ssj.getSSJPCM().getEnergy(p)
        ));
        chargeItem.setItemMeta(chargeMeta);
        p.getInventory().addItem(chargeItem);
    }

    private void giveAuraReleaseItem(Player p) {
        ItemStack auraItem = new ItemStack(Material.GHAST_TEAR);
        ItemMeta auraMeta = auraItem.getItemMeta();
        auraMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "✺ " + ChatColor.DARK_PURPLE + "Release Aura" + ChatColor.LIGHT_PURPLE + " ✺");
        auraMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to release",
            ChatColor.GRAY + "your power aura"
        ));
        auraItem.setItemMeta(auraMeta);
        p.getInventory().addItem(auraItem);
    }

    private void giveMenuItem(Player p) {
        ItemStack menuItem = new ItemStack(Material.PAPER);
        ItemMeta menuMeta = menuItem.getItemMeta();
        menuMeta.setDisplayName(ChatColor.GREEN + "☘ " + ChatColor.DARK_GREEN + "Settings Menu" + ChatColor.GREEN + " ☘");
        menuMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to open",
            ChatColor.GRAY + "your settings menu",
            "",
            ChatColor.GREEN + "Access stats, skills, and more!"
        ));
        menuItem.setItemMeta(menuMeta);
        p.getInventory().addItem(menuItem);
    }

    private void giveRemoveOrAddHoloItem(Player p) {
        ItemStack holoItem = new ItemStack(Material.TNT);
        ItemMeta holoMeta = holoItem.getItemMeta();
        holoMeta.setDisplayName(ChatColor.YELLOW + "❖ " + ChatColor.GOLD + "Toggle Scoreboard" + ChatColor.YELLOW + " ❖");
        holoMeta.setLore(Arrays.asList(
            ChatColor.GRAY + "Right-click to toggle",
            ChatColor.GRAY + "your stats display"
        ));
        holoItem.setItemMeta(holoMeta);
        p.getInventory().addItem(holoItem);
    }

    public void callStartingItems(Player p) {
        itemActions.keySet().forEach(material -> giveItem(p, material));
    }

    public void callScoreboard(Player player) {
        // Setup the scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        @SuppressWarnings("deprecation")
        Objective objective = board.registerNewObjective("ssj", "dummy", ChatColor.GREEN + "Player Stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Fetch updated stats
        int bp = ssj.getSSJPCM().getBattlePower(player);
        int energy = ssj.getSSJPCM().getEnergy(player);
        String form = ssj.getSSJPCM().getForm(player);

        // Set scores
        objective.getScore(ChatColor.YELLOW + "Battle Power: " + ChatColor.WHITE + bp).setScore(3);
        objective.getScore(ChatColor.YELLOW + "Energy: " + ChatColor.WHITE + energy).setScore(2);
        objective.getScore(ChatColor.YELLOW + "Form: " + ChatColor.WHITE + form).setScore(1);

        // Assign the scoreboard to the player
        player.setScoreboard(board);
    }
}
