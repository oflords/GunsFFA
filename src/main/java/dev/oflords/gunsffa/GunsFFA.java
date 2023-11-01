package dev.oflords.gunsffa;

import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class GunsFFA extends JavaPlugin {
    private static GunsFFA gunsFFA;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        Arrays.asList(
                new GunListener()
                // new BattlegroundsListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        gunsFFA = this;
        Gun.init();
    }

    public static GunsFFA get() {
        return gunsFFA;
    }
}
