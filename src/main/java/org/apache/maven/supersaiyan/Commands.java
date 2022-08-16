package org.apache.maven.supersaiyan;


import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public class Commands implements CommandExecutor {

    private Main main = Main.getInstance();

    private PlayerConfig pconfigs = new PlayerConfig();



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("ssj")) {

                if (args.length == 0) {

                    p.sendMessage(ChatColor.RED + "Do what?");

                    p.sendMessage(ChatColor.RED + "Type /ssj help for more info");

                } else if (args[0].equalsIgnoreCase("help")) {

                    p.sendMessage(ChatColor.RED + "/ssj start - start your saiyan journy!");

                } else if (args[0].equalsIgnoreCase("start")) {

                    p.sendMessage(ChatColor.RED + "Start test");

                }
            }

        } else {

            sender.sendMessage("fuck off console!");

            return true;

        }

        return true;

    }

}

