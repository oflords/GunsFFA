package dev.oflords.gunsffa.listeners;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.lordutils.chat.CC;
import dev.oflords.lordutils.player.PlayerStateUtil;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(CC.DARK_GRAY + "[" + CC.GREEN + "+" + CC.DARK_GRAY + "] " + CC.YELLOW + event.getPlayer().getName() + CC.GRAY + " has joined!");

        PlayerStateUtil.reset(event.getPlayer());
        event.getPlayer().setGameMode(GameMode.ADVENTURE);
        GunPlayer.createProfile(event.getPlayer().getUniqueId());

        event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 61.5, 99, 352.5, 90, 0));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        GunsFFA.get().getGameManager().handleDeath(event.getEntity(), event.getEntity().getKiller(), true);

        event.getDrops().clear();
        event.setDroppedExp(0);
    }

    @EventHandler
    public void onRepsawn(PlayerRespawnEvent event) {
        GunsFFA.get().getGameManager().respawn(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        GunPlayer.removeProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        GunPlayer.removeProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GunPlayer gunPlayer = GunPlayer.getByUUID(player.getUniqueId());
            if (!gunPlayer.isHasKit() || event.getCause() == EntityDamageEvent.DamageCause.FALL || player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameRotate(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLoss(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.getEntity().setSaturation(2);
        event.getEntity().setFoodLevel(20);
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (event.getInventory().getType() == InventoryType.FURNACE ||
                    event.getInventory().getType() == InventoryType.WORKBENCH ||
                    event.getInventory().getType() == InventoryType.BREWING ||
                    event.getInventory().getType() == InventoryType.ENCHANTING ||
                    event.getInventory().getType() == InventoryType.ANVIL ||
                    event.getInventory().getType() == InventoryType.ENDER_CHEST ||
                    event.getInventory().getType() == InventoryType.HOPPER ||
                    event.getInventory().getType() == InventoryType.DISPENSER ||
                    event.getInventory().getType() == InventoryType.DROPPER ||
                    event.getInventory().getType() == InventoryType.BARREL ||
                    event.getInventory().getType() == InventoryType.LOOM ||
                    event.getInventory().getType() == InventoryType.STONECUTTER ||
                    event.getInventory().getType() == InventoryType.CARTOGRAPHY ||
                    event.getInventory().getType() == InventoryType.SMITHING ||
                    event.getInventory().getType() == InventoryType.GRINDSTONE ||
                    event.getInventory().getType() == InventoryType.SMOKER ||
                    event.getInventory().getType() == InventoryType.BEACON ||
                    event.getInventory().getType() == InventoryType.SHULKER_BOX ||
                    event.getInventory().getType() == InventoryType.BLAST_FURNACE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.CHEST ||
                e.getClickedBlock().getType() == Material.TRAPPED_CHEST ||
                e.getClickedBlock().getType() == Material.CAVE_VINES ||
                e.getClickedBlock().getType() == Material.CAVE_VINES_PLANT ||
                e.getClickedBlock().getType() == Material.ENDER_CHEST ||
                e.getClickedBlock().getType() == Material.LEVER ||
                e.getClickedBlock().getType() == Material.DRAGON_EGG ||
                e.getClickedBlock().getType() == Material.NOTE_BLOCK ||
                e.getClickedBlock().getType().toString().contains("_BUTTON") ||
                e.getClickedBlock().getType().toString().contains("DOOR") ||
                e.getClickedBlock().getType().toString().contains("_GATE"))) {
            e.setCancelled(true);
        }

        if (e.getAction() == Action.LEFT_CLICK_BLOCK && (e.getClickedBlock().getType() == Material.DRAGON_EGG)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCropTrample(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerPortalTeleport(PlayerPortalEvent event) {
        event.setCancelled(true);
    }
}
