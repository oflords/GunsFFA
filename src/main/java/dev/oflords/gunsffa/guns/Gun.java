package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.GunsFFA;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

public abstract class Gun {

    @Getter private String name;
    private Material item;
    private double bulletDamage;
    private int tickCooldown;
    private double bulletVelocity;
    private Sound shootSound;
    private float soundPitch;
    private float soundVolume;
    public Gun(String name) {

    }

    private void shoot(Player player) {
        player.playSound(player.getLocation(), this.shootSound, this.soundPitch, this.soundVolume);
        Location loc1 = player.getLocation();
        Snowball s = player.launchProjectile(Snowball.class);
        s.setMetadata("gun", new FixedMetadataValue(GunsFFA.get(), this.name));
        s.setVelocity(loc1.getDirection().multiply(this.bulletVelocity));
    }
}
