package org.apache.maven.supersaiyan.SSJCommands;


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


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ssj")) {

                if (args.length == 0) {

                    p.sendMessage(ChatColor.RED + "Do what?");

                    p.sendMessage(ChatColor.RED + "Type /ssj help for more info");

                } else if (args[0].equalsIgnoreCase("help")) {

                    p.sendMessage(ChatColor.RED + "/ssj start - start your Saiyan journey!");

                } else if (args[0].equalsIgnoreCase("start")) {

                    if (ssj.getpPc().getpConfigFile(p).exists() && ssj.getpPc().getpConfig(p).getBoolean("Start")) {

                        p.sendMessage(ChatColor.RED + "You've already started your Saiyan journey!");

                    }

                    if (!ssj.getpPc().getpConfigFile(p).exists()) {

                        p.sendMessage(ChatColor.RED + "Your player file doesn't exist!");

                        p.sendMessage(ChatColor.RED + "Please re-log or tell a server Admin/Owner.");

                    }

                    if (ssj.getpPc().getpConfigFile(p).exists() && !ssj.getpPc().getpConfig(p).getBoolean("Start")) {

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

            }

        } else {

            ssj.getLogger().warning("Plugin doesn't support console commands yet.");

            return true;

        }

        return true;
    }
}

