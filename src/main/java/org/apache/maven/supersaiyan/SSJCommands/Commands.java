package org.apache.maven.supersaiyan.SSJCommands;


import org.apache.maven.supersaiyan.Listeners.PlayerConfig;
import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Commands implements CommandExecutor {

    private final SSJ ssj;

    public Commands(SSJ ssj) {
        this.ssj = ssj;
    }

    //private File pConfigFile;

    //private FileConfiguration pConfig;


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            //pConfigFile = new File(ssj.getDataFolder(), File.separator + "PlayerConfigs" + File.separator + p.getUniqueId() + ".yml");

            //pConfig = new YamlConfiguration();

            if (cmd.getName().equalsIgnoreCase("ssj")) {

                if (args.length == 0) {

                    p.sendMessage(ChatColor.RED + "Do what?");

                    p.sendMessage(ChatColor.RED + "Type /ssj help for more info");

                } else if (args[0].equalsIgnoreCase("help")) {

                    p.sendMessage(ChatColor.RED + "/ssj start - start your Saiyan journey!");

                } else if (args[0].equalsIgnoreCase("start")) {

                    if (ssj.getpPc().getpConfigFile(p).exists()) {

                        try {

                            ssj.getpPc().getpConfig(p).load(ssj.getpPc().getpConfigFile(p));

                            //pConfig.load(pConfigFile);

                            if (!ssj.getpPc().getpConfig(p).getBoolean("Start")) {

                                //pConfig.set("Start", true);

                                ssj.getpPc().getpConfig(p).set("Start", true);

                                ssj.getssjgui().openInventory(p);

                                p.sendMessage(ChatColor.RED + "Your Saiyan journey has started!");

                                ssj.getLogger().warning(p.getName() + "'s.yml Has been updated!");

                            } else {

                                p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

                            }

                        } catch (IOException | InvalidConfigurationException ex) {

                            ex.printStackTrace();

                        }

                    } else {

                        p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

                        p.sendMessage(ChatColor.RED + "Please re-log or tell the server owner.");

                    }

                }

            }

        } else {

            ssj.getLogger().warning("Plugin doesn't support console commands yet.");

            return true;

        }

        return true;
    }
}

