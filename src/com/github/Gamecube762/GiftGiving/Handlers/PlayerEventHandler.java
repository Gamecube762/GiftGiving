package com.github.Gamecube762.GiftGiving.Handlers;

import com.github.Gamecube762.GiftGiving.Manager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEventHandler implements Listener {

    Manager Manager;

    public PlayerEventHandler(Manager c) {Manager = c;}

    @EventHandler
    public void PlayerCloseInventory(InventoryCloseEvent e){
        Manager.DebugMessage(10, "[PlayerCloseInventory]-Event Fired",false);
        if (e.getInventory().getName().contains("'s Gifts")){
            Manager.DebugMessage(10, "[PlayerCloseInventory]-Inv name contains \"'s Gifts\"",false);

            Manager.saveGift(e.getInventory().getName().replace("'s Gifts", ""), e.getInventory());

            Manager.DebugMessage(10, "[PlayerCloseInventory]-SaveGift fired",false);

        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(e.getPlayer().hasPermission("GiftGiving.message.announceGiftUnlockedOnJoin") & Manager.isConfig_PresentsUnlocked())
            p.sendMessage(ChatColor.GREEN + "You can Open your Gifts!!");
    }
}