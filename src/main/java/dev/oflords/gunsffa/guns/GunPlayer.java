package dev.oflords.gunsffa.guns;

import dev.oflords.gunsffa.GunsFFA;
import dev.oflords.lordutils.chat.CC;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
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
        if (!new File(GunsFFA.get().getPlayerDataFolder(), uuid + ".yml").exists()) {
            try {
                if (new File(GunsFFA.get().getPlayerDataFolder(), uuid + ".yml").createNewFile()) {
                    data.save();
                } else {
                    return;
                }
                return;
            } catch (Exception e) {
                System.out.println("Create Error: " + e.getMessage());
                return;
            }
        }

        data.load();

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

    public void save() {
        File file = new File(GunsFFA.get().getPlayerDataFolder(), uuid + ".yml");
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(file);
            var stats = configuration.createSection("Stats");
            stats.set("username", this.getPlayer().getName());
            stats.set("kills", this.getKills());
            stats.set("deaths", this.getDeaths());
            configuration.save(file);
        } catch (Exception e) {
            System.out.println("Save Error: " + e.getMessage());
        }
    }

    public void load() {
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(new File(GunsFFA.get().getPlayerDataFolder(), uuid + ".yml"));
            var stats = configuration.getConfigurationSection("Stats");
            this.setKills(stats.getInt("kills"));
            this.setDeaths(stats.getInt("deaths"));
        } catch (Exception e) {
            System.out.println("Load Error: " + e.getMessage());
        }
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
        getScoreBoard().updateTitle(ChatColor.translateAlternateColorCodes('&', GunsFFA.get().getScoreboardManager().getTitle()));
    }

    public void disableScoreboard() {
        getScoreBoard().delete();
        setScoreBoard(null);
    }
}
