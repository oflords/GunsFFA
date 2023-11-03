package dev.oflords.gunsffa;

import dev.jorel.commandapi.CommandAPI;
import dev.oflords.gunsffa.commands.GetGunsCommand;
import dev.oflords.gunsffa.commands.KitCommand;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunTask;
import dev.oflords.gunsffa.listeners.GunListener;
import dev.oflords.gunsffa.listeners.PlayerListener;
import dev.oflords.gunsffa.listeners.WorldListener;
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
    @Getter private static List<Location> spawns = new ArrayList<>();

    @Override
    public void onEnable() {
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

        gunsFFA = this;
        Gun.init();

        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("world");
                spawns.add(new Location(world, -20.5, 100.0, -34.5, -70, 0));
                spawns.add(new Location(world, 17.5, 103, -25.5, -126, 0));
                spawns.add(new Location(world, 41.5, 101, 0.5, 90, 0));
                spawns.add(new Location(world, 41.5, 101, 21.5, 0, 0));
                spawns.add(new Location(world, 48.5, 101, 67.5, 0, 0));
                spawns.add(new Location(world, -13.5, 97, 53.5, 0, 0));
                spawns.add(new Location(world, 6.5, 100, -9.5, 90, 0));
                spawns.add(new Location(world, -40, 103, 68.5, 0, 0));
                spawns.add(new Location(world, -48.5, 96, -8.5, 0, -14));
            }
        }.runTaskLater(this, 40L);
    }

    public static GunsFFA get() {
        return gunsFFA;
    }
}
