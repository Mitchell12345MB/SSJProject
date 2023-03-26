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

        if (ssj.getSSJPCM().getFile(p).exists() && ssj.getSSJPCM().getStart(p)) {

            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

        }

        if (!ssj.getSSJPCM().getFile(p).exists()) {

            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

        }

        if (ssj.getSSJPCM().getFile(p).exists() && !ssj.getSSJPCM().getStart(p)) {

            ssj.getSSJMethods().callStartingItems(p);

            ssj.getSSJPCM().setPlayerConfigValue(p, "Start", true);

            ssj.getSSJGui().openInventory(p);

            p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

            ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

        }

    }

    public void callMenuChecks(Player p, InventoryClickEvent e) {

        if (e.getRawSlot() == 3) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Health", ssj.getSSJPCM().getHealth(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Power", ssj.getSSJPCM().getPower(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Strength", ssj.getSSJPCM().getStrength(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Speed", ssj.getSSJPCM().getSpeed(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Stamina", ssj.getSSJPCM().getStamina(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Defence", ssj.getSSJPCM().getDefence(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

                ssj.getSSJMethods().callOtherScoreboards();

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

    }

    public void onEnableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

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

                ssj.getSSJPCM().savePlayerConfig(online, ssj.getSSJPCM().getPlayerConfig(online));

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

        if (ssj.getSSJPCM().getFile(p).exists()) {

            if (!(p.getName().equals(ssj.getSSJPCM().getName(p)))) {

                ssj.getSSJPCM().setPlayerConfigValue(p, "Playe_Name", p.getName());

                ssj.getLogger().warning(p.getName() + "'s.yml has been updated!");
            }
        }
    }
}
