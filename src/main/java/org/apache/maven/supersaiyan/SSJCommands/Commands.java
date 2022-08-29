package org.apache.maven.supersaiyan.SSJCommands;


import org.apache.maven.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

                    p.sendMessage(ChatColor.RED + "/ssj items - Gives you the ssj items.");

                } else if (args[0].equalsIgnoreCase("start")) {

                    ssj.getSsjmethods().checkStartCommandMethod(p);

                } else if (args[0].equalsIgnoreCase("items")) {

                    ssj.getSsjmethods().callStartingItems(p);

                }

            }

        } else {

            ssj.getLogger().warning("Plugin doesn't support console commands yet.");

            return true;

        }

        return true;
    }
}