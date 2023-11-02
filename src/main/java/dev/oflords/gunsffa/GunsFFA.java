package dev.oflords.gunsffa;

import dev.jorel.commandapi.CommandAPI;
import dev.oflords.gunsffa.commands.GetGunsCommand;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunTask;
import dev.oflords.gunsffa.listeners.GunListener;
import dev.oflords.gunsffa.listeners.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class GunsFFA extends JavaPlugin {
    private static GunsFFA gunsFFA;
    @Getter private GunTask gunsTask;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        CommandAPI.registerCommand(GetGunsCommand.class);

        Arrays.asList(
                new GunListener(),
                new PlayerListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        gunsTask = new GunTask();
        gunsTask.runTaskTimer(this, 40L, 1L);

        gunsFFA = this;
        Gun.init();
    }

    public static GunsFFA get() {
        return gunsFFA;
    }
}
