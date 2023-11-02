package dev.oflords.gunsffa.listeners;

import dev.oflords.gunsffa.guns.GunPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        for (Entity entity : event.getWorld().getEntities()) {
            if (entity instanceof Arrow || entity instanceof EnderPearl || entity instanceof Item) {
                entity.remove();
            }
        }

        World world = event.getWorld();
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setAutoSave(false);
        world.setTime(6000L);
        world.setDifficulty(Difficulty.HARD);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void blockFormListener(BlockFormEvent event) {
        Material blocktype = event.getNewState().getBlock().getType();
        if (blocktype != Material.AIR && blocktype != Material.SNOW) {
            if (blocktype == Material.ICE || blocktype == Material.WATER) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void meltEvent(BlockFadeEvent event) {
        Material blocktype = event.getBlock().getType();
        if (blocktype == Material.SNOW ||
                blocktype == Material.ICE) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setYield(0);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setYield(0);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if(event.toWeatherState()) event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if(event.toThunderState()) event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM && event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (!event.getPlayer().hasPermission("mythic.staff5") && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }
}
