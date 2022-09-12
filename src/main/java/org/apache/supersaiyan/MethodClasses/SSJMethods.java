package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.Configs.SSJPlayerConfig;
import org.apache.supersaiyan.SSJ;
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

        if (e.getRawSlot() == 3) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Health", user.getHealth() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Power", user.getPower() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Strength", user.getStrength() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Speed", user.getSpeed() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Stamina", user.getStamina() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (user.getAP() > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", user.getAP() - 1);

                user.getUserConfig().set("Base.Defence", user.getDefence() + 1);

                user.getUserConfig().set("Level", addLevel(p));

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                callScoreboard(p);

                callOtherScoreboards();

            } else if (user.getAP() <= 0) {

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

                scoreBoardCheck();

                callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

                ssj.getSSJConfigs().updateConfig();

                ssj.getSSJConfigs().loadConfigs();

            }

        }

    }

    public void onDisableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                SSJPlayerConfig user = new SSJPlayerConfig(ssj, online.getUniqueId());

                user.saveUserFile();

                scoreBoardCheck();

                ssj.getSSJConfigs().saveConfigs();

            }

        }

    }

    public void scoreBoardCheck() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                if (ssj.getSSJSB().hasScore(online)) {

                    ssj.getSSJSB().removeScore(online);

                }

            }

        }

    }

    public void callOtherScoreboards() {

        for (Player online : Bukkit.getOnlinePlayers()) {

            scoreBoardCheck();

            scoreBoardCheck();

            callScoreboard(online);

        }

    }

    public void callScoreboard(Player p) {

        SSJScoreBoards ssjsb = ssj.getSSJSB().createScore(p);

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        ssjsb.setTitle("&aCurrent Stats");

        ssjsb.setSlot(9, "&7&m--------------------------------");

        ssjsb.setSlot(8, "&aPlayer&f: " + p.getName());

        ssjsb.setSlot(7, " ");

        ssjsb.setSlot(6, "Level: " + user.getLevel());

        ssjsb.setSlot(5, " ");

        ssjsb.setSlot(4, "BP: " + user.getBP());

        ssjsb.setSlot(3, " ");

        ssjsb.setSlot(2, "Current Form: " + user.getForm());

        ssjsb.setSlot(1, "&7&m--------------------------------");

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

    public void addEnergy(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int adde = user.getEnergy() * user.getPower();

        if (user.getEnergy() == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Energy", 5);

            user.saveUserFile();

            scoreBoardCheck();

            callScoreboard(p);

        } else if (user.getEnergy() < user.getLimit()) {

            user.loadUserFile();

            user.getUserConfig().set("Energy", adde);

            user.saveUserFile();

            scoreBoardCheck();

            callScoreboard(p);

        }

    }

    public void multBP(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int multbp = ((user.getEnergy() + user.getBaseBP()) * ssj.getSSJConfigs().getBPM());

        if (user.getBP() == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", addBaseBP(p));

            user.saveUserFile();

            scoreBoardCheck();

            callScoreboard(p);

        } else if (user.getEnergy() == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", addBaseBP(p));

            user.saveUserFile();

            scoreBoardCheck();

            callScoreboard(p);

        } else if (user.getEnergy() < user.getLimit()) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", multbp);

            user.saveUserFile();

            scoreBoardCheck();

            callScoreboard(p);

        }

    }

    public int addBaseBP(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getBaseBP() * ssj.getSSJConfigs().getBPM();
    }

    public int addLevel(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getBaseBP() / 17;
    }
}
