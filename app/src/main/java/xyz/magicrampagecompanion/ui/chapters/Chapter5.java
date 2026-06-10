package xyz.magicrampagecompanion.ui.chapters;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Chapter5 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter5);

        bindDungeonButton(R.id.Dungeon41Button,      R.layout.activity_dungeon41);
        bindDungeonButton(R.id.Dungeon42Button,      R.layout.activity_dungeon42);
        bindDungeonButton(R.id.Dungeon42BonusButton, R.layout.activity_dungeon42_bonus);
        bindDungeonButton(R.id.Dungeon43Button,      R.layout.activity_dungeon43);
        bindDungeonButton(R.id.Dungeon43BonusButton, R.layout.activity_dungeon43_bonus);
        bindDungeonButton(R.id.Dungeon44Button,      R.layout.activity_dungeon44);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }
}
