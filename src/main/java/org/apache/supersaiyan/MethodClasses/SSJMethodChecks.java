package org.apache.supersaiyan.MethodClasses;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SSJMethodChecks {

    private final SSJ ssj;

    public SSJMethodChecks(SSJ ssj) {

        this.ssj = ssj;

    }

    public void checkStartCommandMethod(Player p) {

        if (ssj.getSSJPPC(ssj, p).getUserFile().exists() && ssj.getSSJPPC(ssj, p).getUserConfig().getBoolean("Start")) {

            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

        }

        if (!ssj.getSSJPPC(ssj, p).getUserFile().exists()) {

            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

        }

        if (ssj.getSSJPPC(ssj, p).getUserFile().exists() && !ssj.getSSJPPC(ssj, p).getUserConfig().getBoolean("Start")) {

            ssj.getSSJMethods().callStartingItems(p);

            ssj.getSSJPPC(ssj, p).loadUserFile();

            ssj.getSSJPPC(ssj, p).getUserConfig().set("Start", true);

            ssj.getSSJPPC(ssj, p).saveUserFile();

            ssj.getSSJGui().openInventory(p);

            p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

            ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

        }

    }

    public void callMenuChecks(Player p, InventoryClickEvent e) {

        if (e.getRawSlot() == 3) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Health", ssj.getSSJPPC(ssj, p).getHealth() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Power", ssj.getSSJPPC(ssj, p).getPower() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Strength", ssj.getSSJPPC(ssj, p).getStrength() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Speed", ssj.getSSJPPC(ssj, p).getSpeed() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Stamina", ssj.getSSJPPC(ssj, p).getStamina() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (ssj.getSSJPPC(ssj, p).getAP() > 0) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Action_Points", ssj.getSSJPPC(ssj, p).getAP() - 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Base.Defence", ssj.getSSJPPC(ssj, p).getDefence() + 1);

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Level", ssj.getSSJMethods().addLevel(p));

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Battle_Power", ssj.getSSJMethods().addBaseBP(p));

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPPC(ssj, p).getAP() <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

    }

    public void onEnableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                ssj.getSSJPPC(ssj, online).loadUserFile();

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

                ssj.getSSJConfigs().updateConfig();

                ssj.getSSJConfigs().loadConfigs();

            }

        }

    }

    public void onDisableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                ssj.getSSJPPC(ssj, online).saveUserFile();

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

    public void checkPPCPlayerName(Player p) {

        if (ssj.getSSJPPC(ssj, p).getUserFile().exists()) {

            if (!(p.getName().equals(ssj.getSSJPPC(ssj, p).getUserConfig().getString("Player_Name")))) {

                ssj.getSSJPPC(ssj, p).loadUserFile();

                ssj.getSSJPPC(ssj, p).getUserConfig().set("Player_Name", p.getName());

                ssj.getSSJPPC(ssj, p).saveUserFile();

                ssj.getLogger().warning(p.getName() + "'s.yml has been updated!");
            }
        }
    }
}
