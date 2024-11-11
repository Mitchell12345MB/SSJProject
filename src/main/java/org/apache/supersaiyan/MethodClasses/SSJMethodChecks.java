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

    public void callGenStatMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().genstatinv)) return;

        switch (e.getRawSlot()) {
            case 3:
                handleStatIncrease(p, "Base.Health", ssj.getSSJPCM().getHealth(p));
                break;
            case 4:
                handleStatIncrease(p, "Base.Power", ssj.getSSJPCM().getPower(p));
                break;
            case 5:
                handleStatIncrease(p, "Base.Strength", ssj.getSSJPCM().getStrength(p));
                break;
            case 6:
                handleStatIncrease(p, "Base.Speed", ssj.getSSJPCM().getSpeed(p));
                break;
            case 7:
                handleStatIncrease(p, "Base.Stamina", ssj.getSSJPCM().getStamina(p));
                break;
            case 8:
                handleStatIncrease(p, "Base.Defence", ssj.getSSJPCM().getDefence(p));
                break;
            case 10:
                ssj.getSSJGui().openSkillStatInventory(p);
                break;
            case 11:
                ssj.getSSJGui().openSettingsInventory(p);
                break;
            default:
                break;
        }
    }

    private void handleStatIncrease(Player p, String statKey, int currentValue) {
        if (ssj.getSSJPCM().getActionPoints(p) > 0) {
            ssj.getSSJPCM().setPlayerConfigValue(p, "Action_Points", ssj.getSSJPCM().getActionPoints(p) - 1);
            ssj.getSSJPCM().setPlayerConfigValue(p, statKey, currentValue + 1);
            ssj.getSSJPCM().setPlayerConfigValue(p, "Level", ssj.getSSJRpgSys().addLevel(p));
            ssj.getSSJPCM().setPlayerConfigValue(p, "Battle_Power", ssj.getSSJRpgSys().addBaseBP(p));
            
            // Update all stat boosts whenever any stat increases
            ssj.getSSJRpgSys().updateAllStatBoosts(p);
            
            ssj.getSSJGui().openGenStatInventory(p);
            scoreBoardCheck();
            ssj.getSSJMethods().callScoreboard(p);
        } else {
            p.sendMessage(ChatColor.RED + "You have no more action points to spend!");
        }
    }

    public void callSkillStatMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().skillstatinv)) return;

        switch (e.getRawSlot()) {
            case 1:
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            case 2:
                ssj.getSSJGui().openSettingsInventory(p);
                break;
            default:
                break;
        }
    }

    public void callSettingsMenuChecks(Player p, InventoryClickEvent e) {
        if (!e.getInventory().equals(ssj.getSSJGui().settingsinv)) return;

        switch (e.getRawSlot()) {
            case 0:
                toggleSetting(p, "See_Explosion_Effects");
                break;
            case 1:
                toggleSetting(p, "See_Lightning_Effects");
                break;
            case 2:
                toggleSetting(p, "Hear_Sound_Effects");
                break;
            case 3:
                ssj.getSSJGui().openGenStatInventory(p);
                break;
            case 4:
                ssj.getSSJGui().openSkillStatInventory(p);
                break;
            default:
                break;
        }
    }

    private void toggleSetting(Player p, String settingKey) {
        boolean currentValue = (boolean) ssj.getSSJPCM().getPlayerConfigValue(p, settingKey).orElse(false);
        ssj.getSSJPCM().setPlayerConfigValue(p, settingKey, !currentValue);
        ssj.getSSJGui().openSettingsInventory(p);
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