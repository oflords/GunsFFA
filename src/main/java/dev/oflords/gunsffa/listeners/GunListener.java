package dev.oflords.gunsffa.listeners;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.commands.KitCommand;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.utils.NBTEditor;
import dev.oflords.lordutils.player.AttackerUtil;
import dev.oflords.lordutils.player.PlayerStateUtil;
import org.bukkit.*;
import org.bukkit.block.data.type.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class GunListener implements Listener {

    @EventHandler
    public void onSign(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().contains("_SIGN")) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getClickedBlock().getState();

                if (sign.getLine(0).contains("[Guns FFA]")) {
                    KitCommand.execute(event.getPlayer(), sign.getLine(1));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker = AttackerUtil.getAttacker(event);

        if (attacker != null && event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (damaged == attacker) return;

            if (event.getDamager() instanceof Snowball) {
                Snowball s = (Snowball) event.getDamager();
                if (!s.getMetadata("gunsFFA").isEmpty()) {
                    double damage = s.getMetadata("gunsFFA").get(0).asInt();

                    attacker.playSound(attacker, Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
                    damaged.playSound(damaged.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
                    damaged.getWorld().playEffect(damaged.getLocation().clone().add(0, 0.5, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
                    if (damaged.getHealth()-damage < 0) {
                        damaged.setHealth(0);
                    } else {
                        damaged.setHealth(damaged.getHealth() - damage);
                    }
                }
            } else if (event.getDamager() instanceof Player) {
                if (attacker.getInventory().getItemInMainHand().getType() == Material.WOODEN_AXE) {
                    event.setCancelled(false);
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onGunShoot(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getAction().name().contains("RIGHT")) {
            ItemStack itemStack = event.getItem();
            Player player = event.getPlayer();

            GunsFFA.get().getGunsTask().getRightClickingPlayers().put(player.getUniqueId(), System.currentTimeMillis());

            if (itemStack.equals(player.getInventory().getItemInOffHand())) {
                return;
            }

            Gun gun = Gun.canShoot(itemStack);
            if (gun != null) {
                gun.shoot(event.getPlayer());
                itemStack = NBTEditor.set(itemStack, System.currentTimeMillis() + (gun.getShootingCooldown() * 50L), "gunsFFA-cooldown");
                event.getPlayer().getInventory().setItem(event.getPlayer().getInventory().getHeldItemSlot(), itemStack);
            }
        }
    }
}
