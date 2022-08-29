package org.apache.maven.supersaiyan.MethodClasses;

import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class SSJMethods {

    private final SSJ ssj;

    public SSJMethods(SSJ ssj) {
        this.ssj = ssj;
    }

    public void checkStartCommandMethod(Player p) {

        if (ssj.getpPc().getpConfigFile(p).exists() && ssj.getpPc().getpConfig(p).getBoolean("Start")) {

            p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

        }

        if (!ssj.getpPc().getpConfigFile(p).exists()) {

            p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

            p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

        }

        if (ssj.getpPc().getpConfigFile(p).exists() && !ssj.getpPc().getpConfig(p).getBoolean("Start")) {

            callStartingItems(p);

            try {

                ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                ssj.getpPc().getpConfig(p).set("Start", true);

                ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                ssj.getssjgui().openInventory(p);

                p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

                ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

            } catch (IOException | InvalidConfigurationException ex) {

                ex.printStackTrace();

            }
        }
    }

    public void callMenuChecks(Player p, InventoryClickEvent e) {

        int ap = ssj.getpPc().getpConfig(p).getInt("Action_Points");

        int health = ssj.getpPc().getpConfig(p).getInt("Base.Health");

        int power = ssj.getpPc().getpConfig(p).getInt("Base.Power");

        int strength = ssj.getpPc().getpConfig(p).getInt("Base.Strength");

        int speed = ssj.getpPc().getpConfig(p).getInt("Base.Speed");

        int stamina = ssj.getpPc().getpConfig(p).getInt("Base.Stamina");

        int defence = ssj.getpPc().getpConfig(p).getInt("Base.Defence");

        int subap = ap - 1;

        int bp = health * power * strength * speed * stamina * defence / 2;

        if (e.getRawSlot() == 3) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int addh = health + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Health", addh);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 4) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int addp = power + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Power", addp);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 5) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int addst = strength + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Strength", addst);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 6) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int addsp = speed + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Speed", addsp);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 7) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int addsta = stamina + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Stamina", addsta);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

                p.sendMessage(ChatColor.RED + "You have no more action points to spend!");

            }

        }

        if (e.getRawSlot() == 8) {

            if (ssj.getpPc().getpConfig(p).getInt("Action_Points") > 0) {

                try {

                    int adddef = defence + 1;

                    ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                    ssj.getpPc().getpConfig(p).set("Action_Points", subap);

                    ssj.getpPc().getpConfig(p).set("Base.Defence", adddef);

                    ssj.getpPc().getpConfig(p).set("Battle_Power", bp);

                    ssj.getpPc().getpConfig(p).save(ssj.getpPc().getpConfigFile(p));

                    ssj.getssjgui().openInventory(p);

                } catch (IOException | InvalidConfigurationException ex) {

                    ex.printStackTrace();

                }

            } else if (ssj.getpPc().getpConfig(p).getInt("Action_Points") <= 0) {

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
}
