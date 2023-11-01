package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.utils.NBTEditor;
import dev.oflords.lordutils.chat.CC;
import dev.oflords.lordutils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Setter
public class Gun {

    @Getter static List<Gun> guns = new ArrayList<>();

    @Getter private final String name;
    private Material item;
    // Bullets
    private double bulletDamage;
    private double bulletVelocity;
    private int bulletAmount = 1;
    // Cooldowns
    @Getter private int shootingCooldown;
    // Sounds
    private Sound shootSound;
    private float soundPitch;
    private float soundVolume;
    // Penalties
    private float scatterArea = 1;
    private float basePenalty = 0;
    private float sprintPenalty = 0;
    private float jumpPenalty = 0;
    public Gun(String name) {
        this.name = name;
        guns.add(this);
    }

    private ItemStack makeItem() {
        ItemStack gun = new ItemBuilder(this.item).name(name).lore(CC.DARK_GRAY + this.name).build();
        NBTEditor.set(gun, this.name, "gunsFFA");
        return gun;
    }

    private void shoot(Player player) {
        player.playSound(player.getLocation(), this.shootSound, this.soundPitch, this.soundVolume);
        Location loc = player.getLocation().clone();
        for (int scatter = 0; scatter < this.bulletAmount; scatter++) {
            Location loc1 = loc.clone();
            float penalty = this.basePenalty;
            if (player.isSprinting()) {
                penalty += this.sprintPenalty;
            }
            if (!player.isOnGround()) {
                penalty += this.jumpPenalty;
            }
            Snowball s = player.launchProjectile(Snowball.class);
            loc.setPitch((loc1.getPitch() + (ThreadLocalRandom.current().nextFloat(this.scatterArea, this.scatterArea) * penalty)));
            loc.setYaw((loc1.getYaw() + (ThreadLocalRandom.current().nextFloat(this.scatterArea, this.scatterArea) * penalty)));
            s.setMetadata("gunsFFA", new FixedMetadataValue(GunsFFA.get(), this.bulletDamage));
            s.setVelocity(loc.getDirection().multiply(this.bulletVelocity));
        }
    }

    public static Gun getByName(String name) {
        for (Gun gun : Gun.guns) {
            if (gun.getName().equals(name)) {
                return gun;
            }
        }

        return null;
    }
}
