package dev.oflords.gunsffa.managers;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.lordutils.chat.CC;
import dev.oflords.lordutils.player.PlayerStateUtil;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    @Getter
    private static List<Location> spawns = new ArrayList<>();
    public GameManager() {
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

    public void handleDeath(Player deadPlayer, Player killer, boolean deathEvent) {
        GunPlayer deadGunPlayer = GunPlayer.getByUUID(deadPlayer.getUniqueId());
        deadGunPlayer.reset();
        deadGunPlayer.incrementDeaths();
        deadGunPlayer.save();

        if (killer != null) {
            GunPlayer killerGunPlayer = GunPlayer.getByUUID(killer.getUniqueId());
            killerGunPlayer.incrementKills();
            killerGunPlayer.incrementKillstreak();
            killerGunPlayer.save();
            int killstreak = killerGunPlayer.getKillstreak();
            Bukkit.broadcastMessage(CC.DARK_PURPLE + "[K] " + CC.RED + deadPlayer.getName() + CC.WHITE + " was killed by " + CC.GREEN + killer.getName() + CC.GOLD + " [" + CC.RED + killstreak + CC.GOLD + "]");
            String actionBar = "";

            if (killer.getHealth() == 20) {
                actionBar += "+5s Regen";
                killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, false, false));
            } else if (killer.getHealth() > 16) {
                actionBar += "+ Full Hearts";
                killer.setHealth(20);
            } else {
                actionBar += "+2 Hearts";
                killer.setHealth(killer.getHealth() + 4);
            }
            if (killstreak % 3 == 0) {
                actionBar += ", +1 Golden Apple";
                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            }
            if (killstreak % 5 == 0) {
                Bukkit.broadcastMessage(CC.DARK_PURPLE + "[K] " + CC.GREEN + killer.getName() + CC.GOLD + " has a killstreak of " + CC.RED + CC.BOLD + killstreak);
            }

            killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(CC.GREEN + actionBar));
        } else {
            Bukkit.broadcastMessage(CC.DARK_PURPLE + "[K] " + CC.RED + deadPlayer.getName() + CC.WHITE + " has died");
        }

        if (!deathEvent) {
            respawn(deadPlayer);
        }
    }

    public void respawn(Player player) {
        PlayerStateUtil.reset(player, false, false);
        player.setGameMode(GameMode.ADVENTURE);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(new Location(Bukkit.getWorld("world"), 61.5, 99, 352.5, 90, 0));
            }
        }.runTask(GunsFFA.get());
    }
}
