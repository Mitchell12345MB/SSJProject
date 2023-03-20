package org.apache.supersaiyan.SSJCommands;

import org.apache.supersaiyan.SSJ;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SSJCommands implements CommandExecutor {

    private final SSJ ssj;

    public SSJCommands(SSJ ssj) {

        this.ssj = ssj;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ssj")) {

                if (args.length == 0) {

                    player.sendMessage(ChatColor.RED + "Do what?");

                    player.sendMessage(ChatColor.RED + "Type /ssj help for more info");

                } else if (args[0].equalsIgnoreCase("help")) {

                    player.sendMessage(ChatColor.RED + "/ssj start - starts your Saiyan journey!");

                    player.sendMessage(ChatColor.RED + "/ssj items - Gives you the ssj plugin items.");

                    player.sendMessage(ChatColor.RED + "/ssj giveAP <playername> <int> - Gives AP to a player");

                } else if (args[0].equalsIgnoreCase("start")) {

                    ssj.getSSJMethodChecks().checkStartCommandMethod(player);

                } else if (args[0].equalsIgnoreCase("items")) {

                    ssj.getSSJMethods().callStartingItems(player);

                } else if (args[0].equalsIgnoreCase("giveAP")) {

                    if (args.length < 3) {

                        player.sendMessage(ChatColor.RED + "Usage: /ssj giveAP <playername> <int>");

                        return true;

                    }

                    Player target = Bukkit.getPlayer(args[1]);

                    if (target == null) {

                        player.sendMessage(ChatColor.RED + "Player not found");

                        return true;

                    }

                    try {

                        int ap = Integer.parseInt(args[2]);

                        ssj.getSSJPCM().setPlayerConfigValue(target, "Action_Points", ap);

                        player.sendMessage(ChatColor.GREEN + "Gave " + target.getName() + " " + ap + " AP");

                    } catch (NumberFormatException e) {

                        player.sendMessage(ChatColor.RED + "Invalid AP integer.");

                        return true;

                    }

                }

            }

        } else {

            ssj.getLogger().warning("Plugin doesn't support console commands yet.");

        }

        return true;
    }
}