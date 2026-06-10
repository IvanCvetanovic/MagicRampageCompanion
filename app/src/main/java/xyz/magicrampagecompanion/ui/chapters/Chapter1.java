package xyz.magicrampagecompanion.ui.chapters;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Chapter1 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter1);

        bindDungeonButton(R.id.Dungeon1Button,      R.layout.activity_dungeon1);
        bindDungeonButton(R.id.Dungeon2Button,      R.layout.activity_dungeon2);
        bindDungeonButton(R.id.Dungeon3Button,      R.layout.activity_dungeon3);
        bindDungeonButton(R.id.Dungeon3BonusButton, R.layout.activity_dungeon3_bonus);
        bindDungeonButton(R.id.Dungeon4Button,      R.layout.activity_dungeon4);
        bindDungeonButton(R.id.Dungeon5Button,      R.layout.activity_dungeon5);
        bindDungeonButton(R.id.Dungeon5BonusButton, R.layout.activity_dungeon5_bonus);
        bindDungeonButton(R.id.Dungeon6Button,      R.layout.activity_dungeon6);
        bindDungeonButton(R.id.Dungeon7Button,      R.layout.activity_dungeon7);
        bindDungeonButton(R.id.Dungeon7BonusButton, R.layout.activity_dungeon7_bonus);
        bindDungeonButton(R.id.Dungeon8Button,      R.layout.activity_dungeon8);
        bindDungeonButton(R.id.Dungeon9Button,      R.layout.activity_dungeon9);
        bindDungeonButton(R.id.Dungeon10Button,     R.layout.activity_dungeon10);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }
}
