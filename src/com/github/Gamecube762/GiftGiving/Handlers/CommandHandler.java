package com.github.Gamecube762.GiftGiving.Handlers;

import com.github.Gamecube762.GiftGiving.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;


public class CommandHandler implements CommandExecutor {

    Manager Manager;

    public CommandHandler(Manager c) {Manager = c;}


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Boolean PlayerSender = (commandSender instanceof Player);
        Boolean hasPermission = commandSender.hasPermission(command.getPermission());
        String cmdName = command.getName().toLowerCase();

        if (!hasPermission) return NoPermission(commandSender,command);

        if (cmdName.equalsIgnoreCase("gift")) {
            if (!PlayerSender) return NotPlayer(commandSender);
            if (args.length == 0) return false;

            Player a = Bukkit.getPlayer(args[0]);

            //if player is not online
            if (a==null) {
                OfflinePlayer b = Bukkit.getOfflinePlayer(args[0]);

                if (!Manager.getPlayerConfig(b).exists()) return NoPlayerConfigFound(commandSender, b.getName());

                ((Player)commandSender).openInventory(Manager.loadGift(b.getName()));

                return true;
            }

            //if player is online
            if(!Manager.getPlayerConfig(a).exists()){//create inv and make player open it(will save on close)
                ((Player)commandSender).openInventory( Bukkit.createInventory(null, 54, a.getName()+"'s Gifts") );
                return true;
            }

            //open Player's inv if player is online and found file
            ((Player)commandSender).openInventory(Manager.loadGift(a.getName()));
            return true;
        }

        if(cmdName.equalsIgnoreCase("mygift")|cmdName.equalsIgnoreCase("mygifts")) {
            if (!PlayerSender) return NotPlayer(commandSender);

            Player p = ((Player)commandSender);
            Inventory i;

            if(!Manager.getPlayerConfig(p).exists()){
                p.sendMessage(ChatColor.RED + "Gifts not found! Creating Gift Box so people can give you Gifts!");
                i = Bukkit.createInventory(null, 54, p.getName()+"'s Gifts");
                Manager.saveGift(p.getName(), i);
            }
            else i = Manager.loadGift(p.getName());

            if( Manager.isConfig_PresentsUnlocked()|p.hasPermission("GiftGiving.can.open.own.gift") )
                    p.openInventory(i);
            else p.sendMessage("I see you are trying to take an early peek at your gifts!");
        }

        if(cmdName.equalsIgnoreCase("giftadmin")){
            if (args.length == 0) return false;

            if(args[0].equalsIgnoreCase("unlockgifts")){
                if(!commandSender.hasPermission(command.getPermission()+".unlockgifts")) NoPermission(commandSender);
                if(Manager.isConfig_PresentsUnlocked()){
                    commandSender.sendMessage(ChatColor.GREEN + "Gifts are already unlocked!");
                    return true;
                }else{
                    Manager.setConfig_PresentsUnlocked(true);
                    for (Player v: Bukkit.getOnlinePlayers()) v.sendMessage(ChatColor.GREEN + "Gifts are now unlocked!");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("lockgifts")){
                if(!commandSender.hasPermission(command.getPermission()+".lockgifts")) NoPermission(commandSender);
                if(!Manager.isConfig_PresentsUnlocked()){
                    commandSender.sendMessage(ChatColor.GREEN + "Gifts are already locked!");
                }else{
                    Manager.setConfig_PresentsUnlocked(true);
                    for (Player v: Bukkit.getOnlinePlayers()) v.sendMessage(ChatColor.RED + "Gifts are now locked!");
                }
            }
            if(args[0].equalsIgnoreCase("togglegifts")){
                if(!commandSender.hasPermission(command.getPermission()+"togglegifts")) NoPermission(commandSender);
                if(Manager.isConfig_PresentsUnlocked())
                    for (Player v: Bukkit.getOnlinePlayers()) v.sendMessage(ChatColor.GREEN + "Gifts are now locked!");
                else
                    for (Player v: Bukkit.getOnlinePlayers()) v.sendMessage(ChatColor.GREEN + "Gifts are now unlocked!");

                Manager.setConfig_PresentsUnlocked(!Manager.isConfig_PresentsUnlocked());
            }
            if(args[0].equalsIgnoreCase("")){}
        }





        return true;
    }

    private boolean NoPermission(CommandSender s, Command c){
        s.sendMessage(ChatColor.RED +""+ c.getPermissionMessage());
        return true;
    }
    private boolean NoPermission(CommandSender s, String perm){
        s.sendMessage(ChatColor.RED +"You don't have permission "+ perm);
        return true;
    }
    private boolean NoPermission(CommandSender s){
        s.sendMessage(ChatColor.RED +"You don't have permission!");
        return true;
    }
    private boolean NotPlayer(CommandSender s){
        s.sendMessage(ChatColor.RED + "Needs to be a player sending command!");
        return false;
    }

    private boolean NoPlayerConfigFound(CommandSender s, String p){
        s.sendMessage(ChatColor.RED + "Could not find "+ s + "'s Gifts!");
        s.sendMessage(ChatColor.RED + "Player needs to be online first to create Gifts");
        return false;
    }
}