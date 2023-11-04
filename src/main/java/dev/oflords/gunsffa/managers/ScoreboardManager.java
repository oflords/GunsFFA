package dev.oflords.gunsffa.managers;

import dev.oflords.gunsffa.GunsFFA;
import lombok.Getter;

import java.util.List;

public class ScoreboardManager {

    @Getter private String title;
    @Getter private List<String> lines;

    public ScoreboardManager() {
        this.title = GunsFFA.get().getScoreboardConfig().getString("Title");
        this.lines = GunsFFA.get().getScoreboardConfig().getStringList("Lines");
    }
}
