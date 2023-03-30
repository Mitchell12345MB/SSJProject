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

    public void checkStartCommandMethod(Player p) { // When the player runs command "/ssj start", do this;

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

            ssj.getSSJGui().openGenStatInventory(p);

            p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

            ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

        }

    }

    public void callGenStatMenuChecks(Player p, InventoryClickEvent e) { //When the player clicks in the general stats inv, do this;

        if (e.getRawSlot() == 3 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Health", ssj.getSSJPCM().getHealth(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Power", ssj.getSSJPCM().getPower(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Strength", ssj.getSSJPCM().getStrength(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Speed", ssj.getSSJPCM().getSpeed(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Stamina", ssj.getSSJPCM().getStamina(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Base.Defence", ssj.getSSJPCM().getDefence(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 9 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            if (ssj.getSSJPCM().getActionPoints(p) > 0) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Saiyan_Ability", ssj.getSSJPCM().getSaiyanAbility(p) + 1);

                ssj.getSSJPCM().setPlayerConfigValue(p,"Level", ssj.getSSJRpgSys().addLevel(p));

                ssj.getSSJPCM().setPlayerConfigValue(p,"Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));

                ssj.getSSJGui().openGenStatInventory(p);

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(p);

            } else if (ssj.getSSJPCM().getActionPoints(p) <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 10 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            ssj.getSSJGui().openSkillStatInventory(p);

        }

        if (e.getRawSlot() == 11 && e.getInventory().equals(ssj.getSSJGui().genstatinv)) {

            ssj.getSSJGui().openSettingsInventory(p);

        }

    }

    public void callSkillStatMenuChecks(Player p, InventoryClickEvent e) { //When the player clicks in the general stats inv, do this;

        if (e.getRawSlot() == 1 && e.getInventory().equals(ssj.getSSJGui().skillstatinv)) {

            ssj.getSSJGui().openGenStatInventory(p);

        }

        if (e.getRawSlot() == 2 && e.getInventory().equals(ssj.getSSJGui().skillstatinv)) {

            ssj.getSSJGui().openSettingsInventory(p);

        }

    }

    public void callSettingsMenuChecks(Player p, InventoryClickEvent e) { //When the player clicks in the general stats inv, do this;

        if (e.getRawSlot() == 0 && e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            if (ssj.getSSJPCM().getExplosionEffects(p)) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"See_Explosion_Effects", false);

                ssj.getSSJGui().openSettingsInventory(p);

            } else {

                ssj.getSSJPCM().setPlayerConfigValue(p,"See_Explosion_Effects", true);

                ssj.getSSJGui().openSettingsInventory(p);

            }

        }

        if (e.getRawSlot() == 1 && e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            if (ssj.getSSJPCM().getLightningEffects(p)) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"See_Lightning_Effects", false);

                ssj.getSSJGui().openSettingsInventory(p);

            } else {

                ssj.getSSJPCM().setPlayerConfigValue(p,"See_Lightning_Effects", true);

                ssj.getSSJGui().openSettingsInventory(p);

            }

        }

        if (e.getRawSlot() == 2 && e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            if (ssj.getSSJPCM().getSoundEffects(p)) {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Hear_Sound_Effects", false);

                ssj.getSSJGui().openSettingsInventory(p);

            } else {

                ssj.getSSJPCM().setPlayerConfigValue(p,"Hear_Sound_Effects", true);

                ssj.getSSJGui().openSettingsInventory(p);

            }

        }

        if (e.getRawSlot() == 3 && e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            ssj.getSSJGui().openGenStatInventory(p);

        }

        if (e.getRawSlot() == 4 && e.getInventory().equals(ssj.getSSJGui().settingsinv)) {

            ssj.getSSJGui().openSkillStatInventory(p);

        }

    }

    public void onEnableChecks() {

        if (!Bukkit.getOnlinePlayers().isEmpty()) {

            for (Player online : Bukkit.getOnlinePlayers()) {

                scoreBoardCheck();

                ssj.getSSJMethods().callScoreboard(online);

                ssj.getSSJTimers().saveTimer();

                ssj.getSSJConfigs().updateConfigs();

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
