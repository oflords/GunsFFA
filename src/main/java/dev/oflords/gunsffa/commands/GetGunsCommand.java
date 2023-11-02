package dev.oflords.gunsffa.commands;

import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.oflords.gunsffa.guns.Gun;
import org.bukkit.entity.Player;

@Command("getguns")
@Permission("gunsffa.admin")
public class GetGunsCommand {

    @Default
    public static void execute(Player player) {
        for (Gun gun : Gun.getGuns()) {
            player.getInventory().addItem(gun.makeItem());
        }
    }
}
