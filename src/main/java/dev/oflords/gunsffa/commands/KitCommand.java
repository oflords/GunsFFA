package dev.oflords.gunsffa.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AMultiLiteralArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.oflords.gunsffa.guns.Gun;
import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.lordutils.chat.CC;
import dev.oflords.lordutils.item.ItemBuilder;
import dev.oflords.lordutils.player.PlayerStateUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command("kit")
public class KitCommand {

    @Default
    public static void execute(Player player, @AStringArgument String kit) {
        Gun gun = Gun.getByIdentifier(kit);

        if (gun == null) {
            player.sendMessage(CC.RED + "Kit does not exist.");
            return;
        }

        if (gun.getName().equals("Minigun")) {
            player.sendMessage(CC.RED + "YOU THOUGHT LOL!!!");
            return;
        }

        GunPlayer gunPlayer = GunPlayer.getByUUID(player.getUniqueId());
        if (gunPlayer.isHasKit()) {
            player.sendMessage(CC.RED + "You already have a kit...");
            return;
        }

        gunPlayer.setKillstreak(0);
        gunPlayer.setHasKit(true);
        PlayerStateUtil.reset(player);

        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().addItem(gun.makeItem());
        player.getInventory().addItem(Gun.getByName("Pistol").makeItem());
        player.getInventory().addItem(new ItemBuilder(Material.WOODEN_SWORD).unbreakable(true).name(CC.WHITE + "Knife").build());
        player.teleport(new Location(player.getWorld(), 0, 100, 0));
    }
}
