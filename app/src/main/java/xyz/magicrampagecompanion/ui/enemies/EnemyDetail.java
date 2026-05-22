package xyz.magicrampagecompanion.ui.enemies;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.models.Enemy;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class EnemyDetail extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private static final int COLOR_HEALTH  = 0xFF4CAF50;
    private static final int COLOR_ARMOR   = 0xFF42A5F5;
    private static final int COLOR_DMG     = 0xFFEF6C00;
    private static final int COLOR_DMG_ALT = 0xFFFF8A65;
    private static final int COLOR_SPEED   = 0xFF26C6DA;
    private static final int COLOR_JUMP    = 0xFF26C6DA;

    private ImageView enemyImage;
    private TextView enemyName;
    private LinearLayout statsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enemy_detail);

        final View root = findViewById(R.id.enemy_detail_root);
        final int baseL = root.getPaddingLeft();
        final int baseT = root.getPaddingTop();
        final int baseR = root.getPaddingRight();
        final int baseB = root.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            root.setPadding(baseL, baseT + bars.top, baseR, baseB + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);

        enemyImage     = findViewById(R.id.enemyImage);
        enemyName      = findViewById(R.id.enemyName);
        statsContainer = findViewById(R.id.statsContainer);

        Parcelable p = getIntent().getParcelableExtra(EXTRA_ENEMY);
        if (p instanceof Enemy) {
            Enemy e = (Enemy) p;
            enemyImage.setImageResource(e.getImageResId());
            enemyName.setText(e.getName());
            populateEnemyStats(e);
        }
    }

    private void populateEnemyStats(Enemy e) {
        LinearLayout statsCard = addSection(getString(R.string.section_stats));
        addPairRow(statsCard,
                getString(R.string.enemy_stat_health), String.valueOf(e.getHealth()), COLOR_HEALTH,
                getString(R.string.enemy_stat_armor),  String.valueOf(e.getArmor()),  COLOR_ARMOR);
        addPairRow(statsCard,
                getString(R.string.enemy_stat_damage),       String.valueOf(e.getDamage()),        COLOR_DMG,
                getString(R.string.enemy_stat_touch_damage), String.valueOf(e.getDamageOnTouch()), COLOR_DMG_ALT);
        addPairRow(statsCard,
                getString(R.string.enemy_stat_speed), String.valueOf(e.getSpeed()), COLOR_SPEED,
                getString(R.string.enemy_stat_jump),  String.valueOf(e.getJump()),  COLOR_JUMP);

        LinearLayout behaviourCard = addSection(getString(R.string.section_behaviour));
        addRow(behaviourCard,
                getString(R.string.enemy_stat_patrol_behaviour), e.getPatrolBehavour());
        addRow(behaviourCard,
                getString(R.string.enemy_stat_attack_behaviour), e.getAttackBehaviour());
    }

    // ── View helpers ──────────────────────────────────────────────────────────────

    private LinearLayout addSection(String title) {
        // Section container: takes an equal share of statsContainer's height
        LinearLayout section = new LinearLayout(this);
        section.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams sectionParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        sectionParams.weight = 1;
        section.setLayoutParams(sectionParams);
        statsContainer.addView(section);

        TextView header = (TextView) getLayoutInflater().inflate(
                R.layout.item_detail_section_header, section, false);
        header.setText(title);
        section.addView(header);

        // Card fills the rest of the section after the header
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.table_card_bg);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        cardParams.weight = 1;
        card.setLayoutParams(cardParams);
        section.addView(card);

        return card;
    }

    private void addRow(LinearLayout card, String label, CharSequence value) {
        if (card.getChildCount() > 0) addDivider(card);
        View row = getLayoutInflater().inflate(R.layout.item_detail_stat_row, card, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        row.setLayoutParams(lp);
        // Stack label above value, pinned to top-left of the cell
        ((LinearLayout) row).setOrientation(LinearLayout.VERTICAL);
        ((LinearLayout) row).setGravity(Gravity.TOP | Gravity.START);
        TextView statLabel = row.findViewById(R.id.statLabel);
        TextView statValue = row.findViewById(R.id.statValue);
        statLabel.setTextSize(11f);
        statValue.setTextSize(20f);
        statLabel.setText(label);
        statValue.setText(value);
        card.addView(row);
    }

    private void addPairRow(LinearLayout card,
                            String label1, CharSequence val1, int color1,
                            String label2, CharSequence val2, int color2) {
        if (card.getChildCount() > 0) addDivider(card);
        View row = getLayoutInflater().inflate(R.layout.item_detail_stat_pair, card, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        row.setLayoutParams(lp);
        TextView labelLeft  = row.findViewById(R.id.labelLeft);
        TextView valueLeft  = row.findViewById(R.id.valueLeft);
        TextView labelRight = row.findViewById(R.id.labelRight);
        TextView valueRight = row.findViewById(R.id.valueRight);
        labelLeft.setTextSize(14f);
        valueLeft.setTextSize(20f);
        labelRight.setTextSize(14f);
        valueRight.setTextSize(20f);
        labelLeft.setText(label1);
        valueLeft.setText(val1);
        valueLeft.setTextColor(color1);
        labelRight.setText(label2);
        valueRight.setText(val2);
        valueRight.setTextColor(color2);
        card.addView(row);
    }

    private void addDivider(LinearLayout card) {
        View divider = new View(this);
        divider.setBackgroundColor(0x20FFFFFF);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        p.setMarginStart(dp(14));
        p.setMarginEnd(dp(14));
        divider.setLayoutParams(p);
        card.addView(divider);
    }

    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
