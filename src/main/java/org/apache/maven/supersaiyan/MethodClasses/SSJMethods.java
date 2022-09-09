package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SSJMethods {

    private final SSJ ssj;

    public SSJMethods(SSJ ssj) {
        this.ssj = ssj;
    }

    public void checkStartCommandMethod(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        if (user.getUserFile().exists() && user.getUserConfig().getBoolean("Start")) {

            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

        }

        if (!user.getUserFile().exists()) {

            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

        }

        if (user.getUserFile().exists() && !user.getUserConfig().getBoolean("Start")) {

            callStartingItems(p);

            user.loadUserFile();

            user.getUserConfig().set("Start", true);

            user.saveUserFile();

            ssj.getSSJGui().openInventory(p);

            p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

            ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

        }

    }

    public void callMenuChecks(Player p, InventoryClickEvent e) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int ap = user.getUserConfig().getInt("Action_Points");

        int health = user.getUserConfig().getInt("Base.Health");

        int power = user.getUserConfig().getInt("Base.Power");

        int strength = user.getUserConfig().getInt("Base.Strength");

        int speed = user.getUserConfig().getInt("Base.Speed");

        int stamina = user.getUserConfig().getInt("Base.Stamina");

        int defence = user.getUserConfig().getInt("Base.Defence");

        int bpi = ssj.getSSJCConfigs().getCFile().getInt("BP_Multiplier");

        int subap = ap - 1;

        int bp = (health + power + strength + speed + stamina + defence) * bpi;

        if (e.getRawSlot() == 3) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int addh = health + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Health", addh);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int addp = power + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Power", addp);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int addst = strength + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Strength", addst);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int addsp = speed + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Speed", addsp);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int addsta = stamina + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Stamina", addsta);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (user.getUserConfig().getInt("Action_Points") > 0) {

                int adddef = defence + 1;

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", subap);

                user.getUserConfig().set("Base.Defence", adddef);

                user.getUserConfig().set("Battle_Power", bp);

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

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

    public void onEnableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                SSJPlayerConfig user = new SSJPlayerConfig(ssj, online.getUniqueId());

                user.loadUserFile();

                callScoreboard(online);

                callBelowName(online);

                ssj.getSSJTimers().saveTimer();

            }

        }

    }

    public void onDisableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                SSJPlayerConfig user = new SSJPlayerConfig(ssj, online.getUniqueId());

                user.saveUserFile();

            }

        }

    }

    public void scoreBoardCheck(Player p) {

        if (ssj.getSSJSB().hasScore(p)) {

            ssj.getSSJSB().removeScore(p);

        }

    }

    public void belowNameCheck(Player p) {

        if (ssj.getSSJBN().hasBelowName(p)) {

            ssj.getSSJBN().removeBelowName(p);

        }

    }

    public void callScoreboard(Player p) {

        SSJScoreBoards ssjsb = ssj.getSSJSB().createScore(p);

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        ssjsb.setTitle("&aCurrent Stats");

        ssjsb.setSlot(9, "&7&m--------------------------------");

        ssjsb.setSlot(8, "&aPlayer&f: " + p.getName());

        ssjsb.setSlot(7, " ");

        ssjsb.setSlot(6, "Level: " + user.getUserConfig().getInt("Level"));

        ssjsb.setSlot(5, " ");

        ssjsb.setSlot(4, "BP: " + user.getUserConfig().getInt("Battle_Power"));

        ssjsb.setSlot(3, " ");

        ssjsb.setSlot(2, "Current Form: " + user.getUserConfig().getString("Form"));

        ssjsb.setSlot(1, "&7&m--------------------------------");

    }

    public void callBelowName(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int bp = user.getUserConfig().getInt("Battle_Power");

        SSJBelowName ssjbn = ssj.getSSJBN().createBelowName(p);

        ssjbn.setSlot(1, "BP: " + bp);
    }

    public void callUpdatePlayerName(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        if (user.getUserFile().exists()) {

            if (!(p.getName().equals(user.getUserConfig().getString("Player_Name")))) {

                user.loadUserFile();

                user.getUserConfig().set("Player_Name", p.getName());

                user.saveUserFile();

                ssj.getLogger().warning(p.getName() + "'s.yml has been updated!");
            }
        }
    }
}
