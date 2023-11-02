package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.utils.NBTEditor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GunTask extends BukkitRunnable {

    // UUID of player, last active right click
    @Getter private Map<UUID, Long> rightClickingPlayers = new HashMap<>();

    @Override
    public void run() {
        // Shoot every tick for people if possible!
        List<UUID> toRemove = new ArrayList<>();
        long current = System.currentTimeMillis();

        for (UUID uuid : rightClickingPlayers.keySet()) {
            // Step 1: Check the person still online
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                toRemove.add(uuid);
                continue;
            }

            // Step 2: Has right click held down recently
            long lastRightClick = rightClickingPlayers.get(uuid);

            if (lastRightClick+200L < current) {
                toRemove.add(uuid);
                continue;
            }

            // Step 3: Has gun in hand
            ItemStack itemStack = player.getItemInHand();
            if (itemStack.getType() != Material.AIR) {
                Gun gun = Gun.isGun(itemStack);
                if (gun != null) {
                    // Step 4: If its been long enough, shoot!
                    if (Gun.canShoot(itemStack) != null) {
                        gun.shoot(player);
                        itemStack = NBTEditor.set(itemStack, System.currentTimeMillis() + (gun.getShootingCooldown() * 50L), "gunsFFA-cooldown");
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemStack);
                    }
                }
            }
        }

        for (UUID uuid : toRemove) {
            rightClickingPlayers.remove(uuid);
        }
    }
}
