package dev.oflords.gunsffa.utils;

import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.lordutils.chat.CC;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {

    public static List<String> getLines(GunPlayer gunPlayer) {
        List<String> toReturn = new ArrayList<>();

        toReturn.add("");
        toReturn.add(CC.translate("&d● &5&oNetwork"));
        toReturn.add(CC.translate("  &fOnline: &6") + Bukkit.getOnlinePlayers().size());
        toReturn.add("");
        toReturn.add(CC.translate("&d● &5&oGunsFFA"));
        toReturn.add(CC.translate("  &fKills: &d") + gunPlayer.getKills());
        // toReturn.add(CC.translate("  &fAssists: &d") + gunPlayer.getAssists());
        toReturn.add(CC.translate("  &fDeaths: &c") + gunPlayer.getDeaths());
        toReturn.add(CC.translate(""));
        toReturn.add(CC.translate("  &fKillstreak: &a") + gunPlayer.getKillstreak());
        toReturn.add(CC.translate(""));
        toReturn.add(CC.translate("&7&omythic.gg"));

        return toReturn;
    }
}
