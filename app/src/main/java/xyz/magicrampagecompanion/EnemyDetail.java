package xyz.magicrampagecompanion;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EnemyDetail extends AppCompatActivity {
    public static final String EXTRA_ENEMY = "xyz.magicrampagecompanion.EXTRA_ENEMY";

    private ImageView enemyImage;
    private TextView enemyName;
    private TextView enemyDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_detail);

        enemyImage       = findViewById(R.id.enemyImage);
        enemyName        = findViewById(R.id.enemyName);
        enemyDescription = findViewById(R.id.enemyDescription);

        Parcelable p = getIntent().getParcelableExtra(EXTRA_ENEMY);
        if (p instanceof Enemy) {
            Enemy e = (Enemy) p;
            enemyImage.setImageResource(e.getImageResId());
            enemyName.setText(e.getName());
            enemyDescription.setText(buildDesc(e));
        }
    }

    private String buildDesc(Enemy e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Health: ").append(e.getHealth()).append("\n")
                .append("Damage: ").append(e.getDamage()).append("\n")
                .append("Touch Damage: ").append(e.getDamageOnTouch()).append("\n")
                .append("Armor: ").append(e.getArmor()).append("\n")
                .append("Speed: ").append(e.getSpeed()).append("\n")
                .append("Jump: ").append(e.getJump()).append("\n")
                .append("Patrol Behaviour: ").append(e.getPatrolBehavour()).append("\n")
                .append("Attack Behaviour: ").append(e.getAttackBehaviour()).append("\n");
        return sb.toString();
    }
}
