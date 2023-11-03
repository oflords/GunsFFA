package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.lordutils.chat.CC;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GunPlayer {

    @Getter
    private static HashMap<UUID, GunPlayer> profiles = new HashMap<>();

    private UUID uuid;
    @Getter @Setter private boolean hasKit = false;
    @Getter @Setter private FastBoard scoreBoard = null;

    @Getter @Setter private int killstreak = 0;
    @Getter @Setter private int kills = 0;
    @Getter @Setter private int deaths = 0;
    // @Getter @Setter private int assists = 0;
    // @Getter @Setter private double points = 0;

    public GunPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public static void createProfile(UUID uuid) {
        if (profiles.containsKey(uuid)) return;
        GunPlayer data = new GunPlayer(uuid);
        profiles.put(uuid, data);
    }

    public static void removeProfile(UUID uuid) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(GunsFFA.get(), () -> {
            profiles.remove(uuid);
        });
    }

    public static GunPlayer getByUUID(UUID uuid) {
        if (!profiles.containsKey(uuid)) {
            createProfile(uuid);
        }
        return profiles.get(uuid);
    }

    public void reset() {
        this.setHasKit(false);
        this.setKillstreak(0);
    }

    public void incrementKillstreak() {
        this.killstreak++;
    }
    public void incrementKills() {
        this.kills++;
    }
    public void incrementDeaths() {
        this.deaths++;
    }

    public void enableScoreboard() {
        setScoreBoard(new FastBoard(getPlayer()));
        getScoreBoard().updateTitle(CC.DARK_PURPLE + CC.BOLD + " Mythic" + CC.GRAY + CC.ITALIC + " [GunsFFA] ");
    }

    public void disableScoreboard() {
        getScoreBoard().delete();
        setScoreBoard(null);
    }
}
