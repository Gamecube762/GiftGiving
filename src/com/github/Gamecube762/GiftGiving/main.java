package com.github.Gamecube762.GiftGiving;

import com.github.Gamecube762.GiftGiving.Handlers.CommandHandler;
import com.github.Gamecube762.GiftGiving.Handlers.PlayerEventHandler;
import mcstats.Metrics;
import net.gravitydevelopment.updater.Updater;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class main extends JavaPlugin {

    @Override
    public void onEnable(){
        getLogger().info("Gifts "+getDescription().getVersion());
        Manager a = new Manager(this);
        CommandHandler b = new CommandHandler(a);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerEventHandler(a), this);


        getCommand("gift").setExecutor(b);
        getCommand("mygift").setExecutor(b);
        getCommand("mygifts").setExecutor(b);
        getCommand("giftadmin").setExecutor(b);


        Updater updater = new Updater(this, 70053, this.getFile(), Updater.UpdateType.DEFAULT, false);

        LoadMCStats();

    }

    private void LoadMCStats(){
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
            getLogger().info("Metrics Failed! D=");
            getLogger().info("(this Won't impact how the plugin works)");
        }
    }

}
