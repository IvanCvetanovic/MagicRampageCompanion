package xyz.magicrampagecompanion.level;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.ui.levelviewer.LevelEntity;
import xyz.magicrampagecompanion.ui.levelviewer.SceneProperties;

public class Level {

    public String name;

    // Scene-wide properties (lighting, parallax, etc.)
    public SceneProperties sceneProperties = new SceneProperties();

    // All entities placed in the level
    public List<LevelEntity> entities = new ArrayList<>();
}
