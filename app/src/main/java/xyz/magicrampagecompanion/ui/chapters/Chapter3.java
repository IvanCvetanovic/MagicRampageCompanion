package xyz.magicrampagecompanion.ui.chapters;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Chapter3 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter3);

        bindDungeonButton(R.id.Dungeon21Button,      R.layout.activity_dungeon21);
        bindDungeonButton(R.id.Dungeon22Button,      R.layout.activity_dungeon22);
        bindDungeonButton(R.id.Dungeon23Button,      R.layout.activity_dungeon23);
        bindDungeonButton(R.id.Dungeon23BonusButton, R.layout.activity_dungeon23_bonus);
        bindDungeonButton(R.id.Dungeon24Button,      R.layout.activity_dungeon24);
        bindDungeonButton(R.id.Dungeon25Button,      R.layout.activity_dungeon25);
        bindDungeonButton(R.id.Dungeon26Button,      R.layout.activity_dungeon26);
        bindDungeonButton(R.id.Dungeon27Button,      R.layout.activity_dungeon27);
        bindDungeonButton(R.id.Dungeon27BonusButton, R.layout.activity_dungeon27_bonus);
        bindDungeonButton(R.id.Dungeon28Button,      R.layout.activity_dungeon28);
        bindDungeonButton(R.id.Dungeon29Button,      R.layout.activity_dungeon29);
        bindDungeonButton(R.id.Dungeon29BonusButton, R.layout.activity_dungeon29_bonus);
        bindDungeonButton(R.id.Dungeon30Button,      R.layout.activity_dungeon30);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }
}
