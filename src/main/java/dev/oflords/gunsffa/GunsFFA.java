package dev.oflords.gunsffa;

import org.bukkit.plugin.java.JavaPlugin;

public class GunsFFA extends JavaPlugin {
    private static GunsFFA gunsFFA;

    @Override
    public void onEnable() {
        gunsFFA = this;
    }

    public static GunsFFA get() {
        return gunsFFA;
    }
}
