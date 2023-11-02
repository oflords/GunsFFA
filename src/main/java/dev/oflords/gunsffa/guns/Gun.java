package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.utils.NBTEditor;
import dev.oflords.lordutils.chat.CC;
import dev.oflords.lordutils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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

    @Getter private final String identifier;
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
    public Gun(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;

        guns.add(this);
    }

    public static void init() {
        FileConfiguration config = GunsFFA.get().getConfig();
        ConfigurationSection guns = config.getConfigurationSection("Guns");

        if (guns != null) {
            for (String key : guns.getKeys(false)) {
                var gun = guns.getConfigurationSection(key);

                Gun newGun = new Gun(key, gun.getString("name"));
                newGun.setItem(Material.getMaterial(gun.getString("item")));
                newGun.setBulletDamage(gun.getDouble("bullet-damage"));
                newGun.setBulletVelocity(gun.getDouble("bullet-velocity"));
                newGun.setBulletAmount(gun.getInt("bullet-amount"));
                newGun.setShootingCooldown(gun.getInt("shooting-cooldown"));
                newGun.setShootSound(Sound.valueOf(gun.getString("shooting-sound")));
                newGun.setSoundPitch(Float.parseFloat(gun.getString("shooting-sound-pitch")));
                newGun.setSoundVolume(Float.parseFloat(gun.getString("shooting-sound-volume")));
                newGun.setScatterArea(Float.parseFloat(gun.getString("scatter-area")));
                newGun.setBasePenalty(Float.parseFloat(gun.getString("penalty-base")));
                newGun.setSprintPenalty(Float.parseFloat(gun.getString("sprint-base")));
                newGun.setJumpPenalty(Float.parseFloat(gun.getString("jump-base")));
            }
        }
    }

    public ItemStack makeItem() {
        ItemStack gun = new ItemBuilder(this.item).unbreakable(true).name(CC.WHITE + this.name).lore(CC.DARK_GRAY + this.name).build();
        gun = NBTEditor.set(gun, this.name, "gunsFFA");
        gun = NBTEditor.set(gun, 0L, "gunsFFA-cooldown");
        return gun;
    }

    public static Gun isGun(ItemStack itemStack) {
        String gunName = NBTEditor.getString(itemStack, "gunsFFA");
        if (gunName != null) {
            return Gun.getByName(gunName);
        }

        return null;
    }

    public static Gun canShoot(ItemStack itemStack) {
        String gunName = NBTEditor.getString(itemStack, "gunsFFA");
        if (gunName != null) {
            if (NBTEditor.getLong(itemStack, "gunsFFA-cooldown") < System.currentTimeMillis()) {
                return Gun.getByName(gunName);
            }
        }

        return null;
    }

    public void shoot(Player player) {
        player.playSound(player.getLocation(), this.shootSound, this.soundVolume, this.soundPitch);
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
            loc.setPitch((loc1.getPitch() + (ThreadLocalRandom.current().nextFloat(-this.scatterArea, this.scatterArea) * penalty)));
            loc.setYaw((loc1.getYaw() + (ThreadLocalRandom.current().nextFloat(-this.scatterArea, this.scatterArea) * penalty)));
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

    public static Gun getByIdentifier(String identifier) {
        for (Gun gun : Gun.guns) {
            if (gun.getIdentifier().equals(identifier)) {
                return gun;
            }
        }

        return null;
    }
}
