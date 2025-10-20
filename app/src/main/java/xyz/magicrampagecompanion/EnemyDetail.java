package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EnemyDetail extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private ImageView enemyImage;
    private TextView enemyName;
    private TextView enemyDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enemy_detail);

        // Edge-to-edge: push content below status/cutout and above nav bar,
        // without accumulating padding across re-applies.
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

        enemyImage       = findViewById(R.id.enemyImage);
        enemyName        = findViewById(R.id.enemyName);
        enemyDescription = findViewById(R.id.enemyDescription);

        Parcelable p = getIntent().getParcelableExtra(EXTRA_ENEMY);
        if (p instanceof Enemy) {
            Enemy e = (Enemy) p;
            enemyImage.setImageResource(e.getImageResId());
            enemyName.setText(e.getName());
            enemyDescription.setText(buildDesc(e, this));
        }
    }

    private String buildDesc(Enemy e, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(R.string.enemy_stat_health)).append(e.getHealth()).append("\n")
                .append(context.getString(R.string.enemy_stat_damage)).append(e.getDamage()).append("\n")
                .append(context.getString(R.string.enemy_stat_touch_damage)).append(e.getDamageOnTouch()).append("\n")
                .append(context.getString(R.string.enemy_stat_armor)).append(e.getArmor()).append("\n")
                .append(context.getString(R.string.enemy_stat_speed)).append(e.getSpeed()).append("\n")
                .append(context.getString(R.string.enemy_stat_jump)).append(e.getJump()).append("\n")
                .append(context.getString(R.string.enemy_stat_patrol_behaviour)).append(e.getPatrolBehavour()).append("\n")
                .append(context.getString(R.string.enemy_stat_attack_behaviour)).append(e.getAttackBehaviour()).append("\n");
        return sb.toString();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
