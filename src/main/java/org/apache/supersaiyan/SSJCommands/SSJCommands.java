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

        if (!(sender instanceof Player)) {

            sender.sendMessage(ChatColor.RED + "This command can only be used by players!");

            return true;

        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("ssj")) {

            if (args.length == 0) {

                player.sendMessage(ChatColor.RED + "Do what?");

                player.sendMessage(ChatColor.RED + "Type /ssj help for more info");

                return true;

            }

            switch (args[0].toLowerCase()) {

                case "help":

                    sendHelpMessage(player);

                    break;

                case "start":

                    ssj.getSSJMethodChecks().checkStartCommand(player);

                    break;

                case "items":

                    ssj.getSSJMethods().callStartingItems(player);

                    break;

                // Admin Commands

                case "addap":

                    handleAddAP(player, args);

                    break;

                case "addstat":

                    handleAddStat(player, args);

                    break;

                case "setstat":

                    handleSetStat(player, args);

                    break;

                case "addskill":

                    handleAddSkill(player, args);

                    break;

                case "viewskills":

                    handleViewSkills(player, args);

                    break;

                case "viewstats":

                    handleViewStats(player, args);

                    break;

                case "viewtransforms":

                    handleViewTransforms(player, args);

                    break;

                case "addskilllevel":

                    handleAddSkillLevel(player, args);

                    break;

                case "setskilllevel":

                    handleSetSkillLevel(player, args);

                    break;

                case "addtransform":

                    handleAddTransform(player, args);

                    break;

                case "addsaiyanability":

                    handleAddSaiyanAbility(player, args);

                    break;

                case "setsaiyanability":

                    handleSetSaiyanAbility(player, args);

                    break;

                case "subsaiyanability":

                    handleSubtractSaiyanAbility(player, args);

                    break;

                case "subskilllevel":

                    handleSubtractSkillLevel(player, args);

                    break;

                case "removetransform":

                    handleRemoveTransform(player, args);

                    break;

                case "subap":

                    handleSubtractAP(player, args);

                    break;

                case "substat":

                    handleSubtractStat(player, args);

                    break;

                default:

                    player.sendMessage(ChatColor.RED + "Unknown command. Type /ssj help for help.");

                    break;

            }

            return true;

        }

        return false;

    }

    private void sendHelpMessage(Player player) {

        player.sendMessage(ChatColor.GOLD + "=== SSJ Commands ===");

        player.sendMessage(ChatColor.YELLOW + "/ssj start " + ChatColor.WHITE + "- Start your Saiyan journey");

        player.sendMessage(ChatColor.YELLOW + "/ssj items " + ChatColor.WHITE + "- Get SSJ plugin items");

        // Default commands available to all players

        player.sendMessage(ChatColor.YELLOW + "/ssj viewskills [player] " + ChatColor.WHITE + "- View skills");

        player.sendMessage(ChatColor.YELLOW + "/ssj viewstats [player] " + ChatColor.WHITE + "- View stats");

        player.sendMessage(ChatColor.YELLOW + "/ssj viewtransforms [player] " + ChatColor.WHITE + "- View transformations");

        // Admin commands

        if (player.hasPermission("ssj.admin")) {

            player.sendMessage(ChatColor.RED + "=== Admin Commands ===");

            player.sendMessage(ChatColor.RED + "/ssj addap <player> <amount> " + ChatColor.WHITE + "- Add action points");

            player.sendMessage(ChatColor.RED + "/ssj addstat <player> <stat> <amount> " + ChatColor.WHITE + "- Add to stats");

            player.sendMessage(ChatColor.RED + "/ssj setstat <player> <stat> <amount> " + ChatColor.WHITE + "- Set stats");

            player.sendMessage(ChatColor.RED + "/ssj addskill <player> <skill> " + ChatColor.WHITE + "- Add skill");

            player.sendMessage(ChatColor.RED + "/ssj addtransform <player> <transform> " + ChatColor.WHITE + "- Add transformation");

            player.sendMessage(ChatColor.RED + "/ssj addsaiyanability <player> <amount> " + ChatColor.WHITE + "- Add Saiyan ability level");

            player.sendMessage(ChatColor.RED + "/ssj setsaiyanability <player> <amount> " + ChatColor.WHITE + "- Set Saiyan ability level");

            player.sendMessage(ChatColor.RED + "/ssj subsaiyanability <player> <amount> " + ChatColor.WHITE + "- Subtract Saiyan ability level");

            player.sendMessage(ChatColor.RED + "/ssj subap <player> <amount> " + ChatColor.WHITE + "- Subtract action points");

            player.sendMessage(ChatColor.RED + "/ssj substat <player> <stat> <amount> " + ChatColor.WHITE + "- Subtract from stats");

        }

    }

    private void handleAddAP(Player sender, String[] args) {

        if (!sender.hasPermission("ssj.admin")) {

            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");

            return;

        }

        if (args.length != 3) {

            sender.sendMessage(ChatColor.RED + "Usage: /ssj addap <player> <amount>");

            return;

        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {

            sender.sendMessage(ChatColor.RED + "Player not found!");

            return;

        }

        try {

            int amount = Integer.parseInt(args[2]);

            int currentAP = ssj.getSSJPCM().getActionPoints(target);

            ssj.getSSJPCM().setActionPoints(target, currentAP + amount);

            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " AP to " + target.getName());

            target.sendMessage(ChatColor.GREEN + "You received " + amount + " AP!");

        } catch (NumberFormatException e) {

            sender.sendMessage(ChatColor.RED + "Invalid amount!");

        }

    }

    private void handleAddStat(Player sender, String[] args) {

        if (!sender.hasPermission("ssj.admin")) {

            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");

            return;

        }

        if (args.length != 4) {

            sender.sendMessage(ChatColor.RED + "Usage: /ssj addstat <player> <stat> <amount>");

            sender.sendMessage(ChatColor.RED + "Stats: Health, Power, Strength, Speed, Stamina, Defence");

            return;

        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {

            sender.sendMessage(ChatColor.RED + "Player not found!");

            return;

        }

        String stat = args[2].toLowerCase();

        try {

            int amount = Integer.parseInt(args[3]);

            String configPath = "Base." + stat.substring(0, 1).toUpperCase() + stat.substring(1);

            int currentValue = ssj.getSSJPCM().getPlayerConfig(target).getInt(configPath);

            ssj.getSSJPCM().setPlayerConfigValue(target, configPath, currentValue + amount);

            // Update stats and UI

            ssj.getSSJRpgSys().updateAllStatBoosts(target);

            ssj.getSSJMethodChecks().checkScoreboard();

            ssj.getSSJMethods().callScoreboard(target);

            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " to " + target.getName() + "'s " + stat);

            target.sendMessage(ChatColor.GREEN + "Your " + stat + " was increased by " + amount + "!");

        } catch (NumberFormatException e) {

            sender.sendMessage(ChatColor.RED + "Invalid amount!");

        }

    }

    private void handleViewSkills(Player sender, String[] args) {

        Player target;

        if (args.length > 1) {

            target = Bukkit.getPlayer(args[1]);

            if (target == null) {

                sender.sendMessage(ChatColor.RED + "Player not found!");

                return;

            }

        } else {

            target = sender;

        }

        // Open skills GUI for the target player

        ssj.getSSJGui().openSkillsInventory(target);

    }

    private void handleViewStats(Player sender, String[] args) {

        Player target;

        if (args.length > 1) {

            target = Bukkit.getPlayer(args[1]);

            if (target == null) {

                sender.sendMessage(ChatColor.RED + "Player not found!");

                return;

            }

        } else {

            target = sender;

        }

        // Open stats GUI for the target player

        ssj.getSSJGui().openGenStatInventory(target);

    }

    private void handleViewTransforms(Player sender, String[] args) {

        Player target;

        if (args.length > 1) {

            target = Bukkit.getPlayer(args[1]);

            if (target == null) {

                sender.sendMessage(ChatColor.RED + "Player not found!");

                return;

            }

        } else {

            target = sender;

        }

        // Open transformations GUI for the target player

        ssj.getSSJGui().openTransformationsInventory(target);

    }

    private void handleSetStat(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj setstat <player> <stat> <amount>");
            sender.sendMessage(ChatColor.RED + "Stats: Health, Power, Strength, Speed, Stamina, Defence");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String stat = args[2].toLowerCase();
        try {
            int amount = Integer.parseInt(args[3]);
            String configPath = "Base." + stat.substring(0, 1).toUpperCase() + stat.substring(1);
            ssj.getSSJPCM().setPlayerConfigValue(target, configPath, amount);
            
            // Update stats and UI
            ssj.getSSJRpgSys().updateAllStatBoosts(target);
            ssj.getSSJMethodChecks().checkScoreboard();
            ssj.getSSJMethods().callScoreboard(target);
            
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s " + stat + " to " + amount);
            target.sendMessage(ChatColor.GREEN + "Your " + stat + " was set to " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleAddSkill(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj addskill <player> <skill>");
            sender.sendMessage(ChatColor.RED + "Skills: Fly, Jump, Kaioken, Potential, God");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String skill = args[2];
        if (!ssj.getSSJPCM().hasSkill(target, skill)) {
            ssj.getSSJPCM().unlockSkill(target, skill);
            sender.sendMessage(ChatColor.GREEN + "Added " + skill + " skill to " + target.getName());
            target.sendMessage(ChatColor.GREEN + "You've been granted the " + skill + " skill!");
        } else {
            sender.sendMessage(ChatColor.RED + target.getName() + " already has this skill!");
        }
    }

    private void handleAddSkillLevel(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj addskilllevel <player> <skill> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String skill = args[2];
        try {
            int amount = Integer.parseInt(args[3]);
            int currentLevel = ssj.getSSJSkillManager().getSkillLevel(target, skill);
            ssj.getSSJSkillManager().setSkillLevel(target, skill, currentLevel + amount);
            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " levels to " + target.getName() + "'s " + skill + " skill");
            target.sendMessage(ChatColor.GREEN + "Your " + skill + " skill level was increased by " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleSetSkillLevel(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj setskilllevel <player> <skill> <level>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String skill = args[2];
        try {
            int level = Integer.parseInt(args[3]);
            ssj.getSSJSkillManager().setSkillLevel(target, skill, level);
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s " + skill + " skill level to " + level);
            target.sendMessage(ChatColor.GREEN + "Your " + skill + " skill level was set to " + level + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid level!");
        }
    }

    private void handleAddTransform(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj addtransform <player> <transform>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String transformId = args[2];
        if (!ssj.getSSJPCM().hasTransformation(target, transformId)) {
            ssj.getSSJTransformationManager().unlockTransformation(target, transformId);
            sender.sendMessage(ChatColor.GREEN + "Added transformation " + transformId + " to " + target.getName());
            target.sendMessage(ChatColor.GREEN + "You've unlocked a new transformation!");
        } else {
            sender.sendMessage(ChatColor.RED + target.getName() + " already has this transformation!");
        }
    }

    private void handleAddSaiyanAbility(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj addsaiyanability <player> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        try {
            int amount = Integer.parseInt(args[2]);
            int currentLevel = ssj.getSSJSaiyanAbilityManager().getSaiyanAbilityLevel(target);
            ssj.getSSJSaiyanAbilityManager().setSaiyanAbilityLevel(target, currentLevel + amount);
            sender.sendMessage(ChatColor.GREEN + "Added " + amount + " to " + target.getName() + "'s Saiyan ability level");
            target.sendMessage(ChatColor.GREEN + "Your Saiyan ability level was increased by " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleSetSaiyanAbility(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj setsaiyanability <player> <level>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        try {
            int level = Integer.parseInt(args[2]);
            ssj.getSSJSaiyanAbilityManager().setSaiyanAbilityLevel(target, level);
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s Saiyan ability level to " + level);
            target.sendMessage(ChatColor.GREEN + "Your Saiyan ability level was set to " + level + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid level!");
        }
    }

    private void handleSubtractSaiyanAbility(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj subsaiyanability <player> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        try {
            int amount = Integer.parseInt(args[2]);
            int currentLevel = ssj.getSSJSaiyanAbilityManager().getSaiyanAbilityLevel(target);
            int newLevel = Math.max(0, currentLevel - amount);
            ssj.getSSJSaiyanAbilityManager().setSaiyanAbilityLevel(target, newLevel);
            sender.sendMessage(ChatColor.GREEN + "Subtracted " + amount + " from " + target.getName() + "'s Saiyan ability level");
            target.sendMessage(ChatColor.GREEN + "Your Saiyan ability level was decreased by " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleSubtractSkillLevel(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj subskilllevel <player> <skill> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String skill = args[2];
        try {
            int amount = Integer.parseInt(args[3]);
            int currentLevel = ssj.getSSJSkillManager().getSkillLevel(target, skill);
            int newLevel = Math.max(0, currentLevel - amount);
            ssj.getSSJSkillManager().setSkillLevel(target, skill, newLevel);
            sender.sendMessage(ChatColor.GREEN + "Subtracted " + amount + " levels from " + target.getName() + "'s " + skill + " skill");
            target.sendMessage(ChatColor.GREEN + "Your " + skill + " skill level was decreased by " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleRemoveTransform(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj removetransform <player> <transform>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String transformId = args[2];
        if (ssj.getSSJPCM().hasTransformation(target, transformId)) {
            ssj.getSSJTransformationManager().removeTransformation(target, transformId);
            sender.sendMessage(ChatColor.GREEN + "Removed transformation " + transformId + " from " + target.getName());
            target.sendMessage(ChatColor.RED + "One of your transformations was removed!");
        } else {
            sender.sendMessage(ChatColor.RED + target.getName() + " doesn't have this transformation!");
        }
    }

    private void handleSubtractAP(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj subap <player> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        try {
            int amount = Integer.parseInt(args[2]);
            int currentAP = ssj.getSSJPCM().getActionPoints(target);
            int newAP = Math.max(0, currentAP - amount);
            ssj.getSSJPCM().setActionPoints(target, newAP);
            sender.sendMessage(ChatColor.GREEN + "Subtracted " + amount + " AP from " + target.getName());
            target.sendMessage(ChatColor.RED + "You lost " + amount + " Action Points!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

    private void handleSubtractStat(Player sender, String[] args) {
        if (!sender.hasPermission("ssj.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /ssj substat <player> <stat> <amount>");
            sender.sendMessage(ChatColor.RED + "Stats: Health, Power, Strength, Speed, Stamina, Defence");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return;
        }

        String stat = args[2].toLowerCase();
        try {
            int amount = Integer.parseInt(args[3]);
            String configPath = "Base." + stat.substring(0, 1).toUpperCase() + stat.substring(1);
            int currentValue = ssj.getSSJPCM().getPlayerConfig(target).getInt(configPath);
            int newValue = Math.max(0, currentValue - amount);
            ssj.getSSJPCM().setPlayerConfigValue(target, configPath, newValue);
            
            // Update stats and UI
            ssj.getSSJRpgSys().updateAllStatBoosts(target);
            ssj.getSSJMethodChecks().checkScoreboard();
            ssj.getSSJMethods().callScoreboard(target);
            
            sender.sendMessage(ChatColor.GREEN + "Subtracted " + amount + " from " + target.getName() + "'s " + stat);
            target.sendMessage(ChatColor.RED + "Your " + stat + " was decreased by " + amount + "!");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid amount!");
        }
    }

}