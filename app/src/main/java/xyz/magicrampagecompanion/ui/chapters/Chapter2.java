package xyz.magicrampagecompanion.ui.chapters;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Chapter2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter2);

        bindDungeonButton(R.id.Dungeon11Button,      R.layout.activity_dungeon11);
        bindDungeonButton(R.id.Dungeon11BonusButton, R.layout.activity_dungeon11_bonus);
        bindDungeonButton(R.id.Dungeon12Button,      R.layout.activity_dungeon12);
        bindDungeonButton(R.id.Dungeon13Button,      R.layout.activity_dungeon13);
        bindDungeonButton(R.id.Dungeon13BonusButton, R.layout.activity_dungeon13_bonus);
        bindDungeonButton(R.id.Dungeon14Button,      R.layout.activity_dungeon14);
        bindDungeonButton(R.id.Dungeon15Button,      R.layout.activity_dungeon15);
        bindDungeonButton(R.id.Dungeon16Button,      R.layout.activity_dungeon16);
        bindDungeonButton(R.id.Dungeon17Button,      R.layout.activity_dungeon17);
        bindDungeonButton(R.id.Dungeon17BonusButton, R.layout.activity_dungeon17_bonus);
        bindDungeonButton(R.id.Dungeon18Button,      R.layout.activity_dungeon18);
        bindDungeonButton(R.id.Dungeon19Button,      R.layout.activity_dungeon19);
        bindDungeonButton(R.id.Dungeon19BonusButton, R.layout.activity_dungeon19_bonus);
        bindDungeonButton(R.id.Dungeon20Button,      R.layout.activity_dungeon20);
    }

    @Override
    protected int clickSoundRes() {
        return R.raw.button;
    }
}
