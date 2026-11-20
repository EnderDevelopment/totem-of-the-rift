package com.enderdevelopment.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Totem of Undying plugin enabled!");
        TotemOfUndying totem = new TotemOfUndying(this);
        getCommand("give_totem").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getInventory().addItem(totem.createTotem());
                player.sendMessage("You have been given a Totem of Undying!");
                return true;
            }
            return false;
        });
        getServer().getPluginManager().registerEvents(totem, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Totem of Undying plugin disabled!");
    }
}