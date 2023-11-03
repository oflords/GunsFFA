package dev.oflords.gunsffa;

import dev.jorel.commandapi.CommandAPI;
import dev.oflords.gunsffa.commands.GetGunsCommand;
import dev.oflords.gunsffa.commands.KitCommand;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunTask;
import dev.oflords.gunsffa.listeners.GunListener;
import dev.oflords.gunsffa.listeners.PlayerListener;
import dev.oflords.gunsffa.listeners.WorldListener;
import dev.oflords.gunsffa.managers.GameManager;
import dev.oflords.gunsffa.tasks.ScoreboardTask;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GunsFFA extends JavaPlugin {
    private static GunsFFA gunsFFA;
    @Getter private GunTask gunsTask;
    @Getter private GameManager gameManager;

    @Override
    public void onEnable() {
        gunsFFA = this;
        this.saveDefaultConfig();

        CommandAPI.registerCommand(GetGunsCommand.class);
        CommandAPI.registerCommand(KitCommand.class);

        Arrays.asList(
                new GunListener(),
                new PlayerListener(),
                new WorldListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        gunsTask = new GunTask();
        gunsTask.runTaskTimer(this, 40L, 1L);
        new ScoreboardTask().runTaskTimerAsynchronously(this, 20L, 1L);

        Gun.init();

        new BukkitRunnable() {
            @Override
            public void run() {
                gameManager = new GameManager();
            }
        }.runTaskLater(this, 40L);
    }

    public static GunsFFA get() {
        return gunsFFA;
    }
}
