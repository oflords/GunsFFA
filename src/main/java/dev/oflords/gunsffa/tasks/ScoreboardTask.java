package dev.oflords.gunsffa.tasks;

import dev.oflords.gunsffa.guns.GunPlayer;
import dev.oflords.gunsffa.utils.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GunPlayer gunPlayer = GunPlayer.getByUUID(player.getUniqueId());

            if (gunPlayer.getScoreBoard() != null) {
                gunPlayer.getScoreBoard().updateLines(Scoreboard.getLines(gunPlayer));
            }
        }
    }
}
