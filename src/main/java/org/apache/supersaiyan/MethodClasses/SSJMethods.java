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

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Health", getHealth(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Power", getPower(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Strength", getStrength(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Speed", getSpeed(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Stamina", getStamina(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (user.getUserConfig().getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (getAP(p) > 0) {

                user.loadUserFile();

                user.getUserConfig().set("Action_Points", getAP(p) - 1);

                user.getUserConfig().set("Base.Defence", getDefence(p) + 1);

                user.getUserConfig().set("Battle_Power", addBaseBP(p));

                user.saveUserFile();

                ssj.getSSJGui().openInventory(p);

                callScoreboard(p);

            } else if (getAP(p) <= 0) {

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

                ssj.getSSJConfigs().saveConfigs();

            }

        }

    }

    public void scoreBoardCheck(Player p) {

        if (ssj.getSSJSB().hasScore(p)) {

            ssj.getSSJSB().removeScore(p);

        }

    }

    public void callScoreboard(Player p) {

        SSJScoreBoards ssjsb = ssj.getSSJSB().createScore(p);

        ssjsb.setTitle("&aCurrent Stats");

        ssjsb.setSlot(9, "&7&m--------------------------------");

        ssjsb.setSlot(8, "&aPlayer&f: " + p.getName());

        ssjsb.setSlot(7, " ");

        ssjsb.setSlot(6, "Level: " + getLevel(p));

        ssjsb.setSlot(5, " ");

        ssjsb.setSlot(4, "BP: " + getBP(p));

        ssjsb.setSlot(3, " ");

        ssjsb.setSlot(2, "Current Form: " + getForm(p));

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

        int adde = getEnergy(p) * getPower(p);

        if (getEnergy(p) == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Energy", 5);

            user.saveUserFile();

        } else if (getEnergy(p) < getLimit(p)) {

            user.loadUserFile();

            user.getUserConfig().set("Energy", adde);

            user.saveUserFile();

        }

    }

    public void multBP(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        int multbp = ((getEnergy(p) + getBaseBP(p)) * getBPM());

        if (getBP(p) == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", addBaseBP(p));

            user.saveUserFile();

        } else if (getEnergy(p) == 0) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", addBaseBP(p));

            user.saveUserFile();

        } else if (getEnergy(p) < getLimit(p)) {

            user.loadUserFile();

            user.getUserConfig().set("Battle_Power", multbp);

            user.saveUserFile();

        }

    }

    public int addBaseBP(Player p) {

        return getBaseBP(p) * getBPM();

    }

    public int getLimit(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Power") * ssj.getSSJConfigs().getCFile().getInt("Limit_Energy_Multiplier");

    }

    public int getHealth(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Health");

    }

    public int getPower(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Power");

    }

    public int getStrength(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Strength");

    }

    public int getSpeed(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Speed");

    }

    public int getStamina(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Stamina");

    }

    public int getDefence(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Base.Defence");

    }

    public int getEnergy(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Energy");

    }

    public int getBaseBP(Player p) {

        return getHealth(p) * getPower(p) + getStrength(p) * getSpeed(p) + getStamina(p) * getDefence(p);

    }

    public int getBP(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Battle_Power");

    }

    public int getAP(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Action_Points");

    }

    public int getLevel(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getInt("Level");

    }

    public String getForm(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getString("Form");

    }

    public int getBPM() {

        return ssj.getSSJConfigs().getCFile().getInt("Base_Battle_Power_Multiplier");

    }

    public int getSAP() {

        return ssj.getSSJConfigs().getCFile().getInt("Starting_Action_Points");

    }

    public String getTransformations(Player p) {

        SSJPlayerConfig user = new SSJPlayerConfig(ssj, p.getUniqueId());

        return user.getUserConfig().getString("Transformations_Unlocked");
    }
}
