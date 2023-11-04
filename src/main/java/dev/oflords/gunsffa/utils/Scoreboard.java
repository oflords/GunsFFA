package dev.oflords.gunsffa.utils;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.lordutils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {

    public static List<String> getLines(GunPlayer gunPlayer) {
        List<String> toReturn = new ArrayList<>();

        for (String line : GunsFFA.get().getScoreboardManager().getLines()) {
            line = line.replaceAll("(%playing%)", String.valueOf(Bukkit.getOnlinePlayers().size()));
            line = line.replaceAll("(%kills%)", String.valueOf(gunPlayer.getKills()));
            line = line.replaceAll("(%deaths%)", String.valueOf(gunPlayer.getDeaths()));
            line = line.replaceAll("(%killstreak%)", String.valueOf(gunPlayer.getKillstreak()));
            line = ChatColor.translateAlternateColorCodes('&', line);
            toReturn.add(line);
        }

        return toReturn;
    }
}
