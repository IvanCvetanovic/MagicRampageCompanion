package xyz.magicrampagecompanion.ui.chapters;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Chapter4 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter4);

        bindDungeonButton(R.id.Dungeon31Button,      R.layout.activity_dungeon31);
        bindDungeonButton(R.id.Dungeon31BonusButton, R.layout.activity_dungeon31_bonus);
        bindDungeonButton(R.id.Dungeon32Button,      R.layout.activity_dungeon32);
        bindDungeonButton(R.id.Dungeon33Button,      R.layout.activity_dungeon33);
        bindDungeonButton(R.id.Dungeon34Button,      R.layout.activity_dungeon34);
        bindDungeonButton(R.id.Dungeon35Button,      R.layout.activity_dungeon35);
        bindDungeonButton(R.id.Dungeon36Button,      R.layout.activity_dungeon36);
        bindDungeonButton(R.id.Dungeon37Button,      R.layout.activity_dungeon37);
        bindDungeonButton(R.id.Dungeon38Button,      R.layout.activity_dungeon38);
        // Note: layout file name has a stray 's' (activity_dungeons39).
        bindDungeonButton(R.id.Dungeon39Button,      R.layout.activity_dungeons39);
        bindDungeonButton(R.id.Dungeon40Button,      R.layout.activity_dungeon40);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }
}
