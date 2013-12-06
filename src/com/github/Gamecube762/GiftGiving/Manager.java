package com.github.Gamecube762.GiftGiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Manager {

    public Plugin plugin;
    public FileConfiguration config;


    private File pluginFolder;
    private File inventoriesFolder;


    private int config_DebugMessages;
    private boolean config_PresentsUnlocked;

    private boolean config_unlockDate_Enabled;
    private int config_unlockDate_month;
    private int config_unlockDate_day;

    public Manager(Plugin p) {
        plugin = p;
        config = plugin.getConfig();
        pluginFolder = plugin.getDataFolder();
        inventoriesFolder = new File(pluginFolder, "Gifts");

        if (!inventoriesFolder.exists()) inventoriesFolder.mkdirs();
        if (!( new File(pluginFolder, "config.yml") ).exists() ) plugin.saveDefaultConfig();


        loadConfig();
    }

    private void loadConfig(){
        config_DebugMessages = config.getInt("settings.debug");
        config_PresentsUnlocked = config.getBoolean("settings.presentsUnlocked");

        config_unlockDate_Enabled = config.getBoolean("settings.unlockDate.enabled");
        config_unlockDate_Enabled = config.getBoolean("settings.unlockDate.month");
        config_unlockDate_Enabled = config.getBoolean("settings.unlockDate.day");

    }



    public void saveGift(String s, Inventory i){
        YamlConfiguration a = new YamlConfiguration();
        File b = new File(this.inventoriesFolder, s+".yml");

        a.set("Gift.InventoryName", s + "'s Gifts");

        int z=0;
        for (ItemStack item : i){
            a.set("Gift.Inventory."+z, item);
            z++;
        }

        try {
            a.save(b);
        } catch (IOException e) {
            DebugMessage(2, "Could not save "+s+"! IOExeption was thrown!", true);
            DebugMessage(4, e);
        }
    }


    public Inventory loadGift(String p){
        YamlConfiguration a = YamlConfiguration.loadConfiguration(new File(this.inventoriesFolder, p + ".yml"));
        Inventory b = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',a.getString("Gift.InventoryName") ) );

        int z = 0;
        while (z <= 54){
            ItemStack y = a.getItemStack("Gift.Inventory."+z);

            if (y!=null)b.addItem(y);
            else b.addItem(new ItemStack(Material.AIR));

            z++;
        }

        return b;
    }


    public File getInventoriesFolder() {return inventoriesFolder;}
    public File getPluginFolder() {return pluginFolder;}

    public boolean isConfig_PresentsUnlocked() {return config_PresentsUnlocked;}
    public void setConfig_PresentsUnlocked(Boolean b) {
        config_PresentsUnlocked = b;
        plugin.saveConfig();
    }

    public File getPlayerConfig(Player p) {return new File(inventoriesFolder, p.getName()+".yml");}
    public File getPlayerConfig(OfflinePlayer p) {return new File(inventoriesFolder, p.getName()+".yml");}
    public File getPlayerConfig(String s) {return new File(inventoriesFolder, s+".yml");}


    //debuging methods
    public void DebugMessage(int i, String m, boolean severe){
        if (i>=config_DebugMessages){
            if (severe) this.plugin.getLogger().severe("[Debug] "+m);
            else this.plugin.getLogger().info("[Debug] "+m);
        }
    }
    public void DebugMessage(int i, IOException e){if (i>=config_DebugMessages) e.printStackTrace();}
    public void DebugMessage(int i, List<ItemStack> l, boolean severe){
        if (i>=config_DebugMessages){
            int z = 0;
            for (ItemStack a : l){
                if (severe) this.plugin.getLogger().severe("[Debug] "+a.toString());
                else this.plugin.getLogger().info("[Debug] " + a.toString());
            }
            DebugMessage(i,z+" Objects found",severe);


        }
    }
}
