package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ItemDetail extends AppCompatActivity {
    public static final String EXTRA_ITEM = "xyz.magicrampagecompanion.EXTRA_ITEM";

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);

        itemImage       = findViewById(R.id.itemImage);
        itemName        = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);

        Parcelable parcel = getIntent().getParcelableExtra(EXTRA_ITEM);
        if (parcel instanceof Weapon) {
            Weapon w = (Weapon) parcel;
            itemImage.setImageResource(w.getImageResId());
            itemName.setText(w.getName());
            itemDescription.setText(buildWeaponDesc(w));

        } else if (parcel instanceof Armor) {
            Armor a = (Armor) parcel;
            itemImage.setImageResource(a.getImageResId());
            itemName.setText(a.getName());
            itemDescription.setText(buildArmorDesc(a));

        } else if (parcel instanceof Ring) {
            Ring r = (Ring) parcel;
            itemImage.setImageResource(r.getImageResId());
            itemName.setText(r.getName());
            itemDescription.setText(buildRingDesc(r));
        }
    }

    private int getElementColor(Elements element) {
        Context context = this;
        switch (element) {
            case WATER:
                return ContextCompat.getColor(context, R.color.water_element_color);
            case EARTH:
                return ContextCompat.getColor(context, R.color.earth_element_color);
            case FIRE:
                return ContextCompat.getColor(context, R.color.fire_element_color);
            case DARKNESS:
                return ContextCompat.getColor(context, R.color.darkness_element_color);
            case LIGHT:
                return ContextCompat.getColor(context, R.color.light_element_color);
            case AIR:
                return ContextCompat.getColor(context, R.color.air_element_color);
            case NEUTRAL:
            default:
                return ContextCompat.getColor(context, R.color.white);
        }
    }

    private void appendBooleanStatus(SpannableStringBuilder ssb, String label, boolean isChecked) {
        ssb.append(label).append(" ");

        int iconResId = isChecked ? R.drawable.icon_check : R.drawable.icon_uncheck;
        Drawable iconDrawable = ContextCompat.getDrawable(this, iconResId);

        if (iconDrawable != null) {
            int iconSize = 80;
            iconDrawable.setBounds(0, 0, iconSize, iconSize);

            int start = ssb.length();
            ssb.append(" ");
            int end = ssb.length();
            ImageSpan span = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BASELINE);
            ssb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(isChecked ? "(✓)" : "(✗)");
        }

        ssb.append("\n");
    }

    private CharSequence buildWeaponDesc(Weapon w) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = w.getElement();
        String elementName = element.name();

        ssb.append(getString(R.string.type)).append(w.getType().name()).append("\n");

        ssb.append(getString(R.string.element));
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(elementName, new ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(elementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.damage_in_calculation))
                .append(String.valueOf(w.getMinDamage())).append("-").append(String.valueOf(w.getMaxDamage())).append("\n")
                .append(getString(R.string.upgrades)).append(String.valueOf(w.getUpgrades())).append("\n")
                .append(getString(R.string.armor_bonus)).append(String.valueOf((int) w.getArmorBonus())).append("%\n")
                .append(getString(R.string.speed)).append(String.valueOf(w.getSpeed())).append("%\n")
                .append(getString(R.string.jump_impulse)).append(String.valueOf(w.getJump())).append("%\n");

        ssb.append(getString(R.string.attack_speed));
        double currentAttackSpeed = w.getAttackCooldown();
        int starCount;
        if (currentAttackSpeed <= 300) { starCount = 5; }
        else if (currentAttackSpeed <= 450) { starCount = 4; }
        else if (currentAttackSpeed <= 650) { starCount = 3; }
        else if (currentAttackSpeed <= 750) { starCount = 2; }
        else { starCount = 1; }

        Drawable starDrawable = ContextCompat.getDrawable(this, R.drawable.star);
        if (starDrawable != null) {
            int desiredWidth = 80;
            int desiredHeight = 80;
            starDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
            for (int i = 0; i < starCount; i++) {
                int start = ssb.length();
                ssb.append(" ");
                int end = ssb.length();
                ImageSpan span = new ImageSpan(starDrawable, ImageSpan.ALIGN_BASELINE);
                ssb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            ssb.append("(").append(String.valueOf(starCount)).append(" stars)");
        }
        ssb.append("\n");

        ssb.append(getString(R.string.pierce_count)).append(String.valueOf(w.getPierceCount())).append("\n");

        appendBooleanStatus(ssb, getString(R.string.pierce_area_damage), w.isEnablePierceAreaDamage());
        appendBooleanStatus(ssb, getString(R.string.projectile_persistence), w.isPersistAgainstProjectile());
        appendBooleanStatus(ssb, getString(R.string.poisonous_attack), w.isPoisonous());
        appendBooleanStatus(ssb, getString(R.string.frost_attack), w.isFrost());

        return ssb;
    }

    private CharSequence buildArmorDesc(Armor a) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = a.getElement();
        String elementName = element.name();

        ssb.append(getString(R.string.element));
        int start = ssb.length();
        ssb.append(elementName);
        int end = ssb.length();
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.armor_in_calculation))
                .append(String.valueOf(a.getMinArmor())).append("-").append(String.valueOf(a.getMaxArmor())).append("\n")
                .append(getString(R.string.upgrades)).append(String.valueOf(a.getUpgrades())).append("\n")
                .append(getString(R.string.speed)).append(String.valueOf(a.getSpeed())).append("%\n")
                .append(getString(R.string.jump_impulse)).append(String.valueOf(a.getJump())).append("%\n")
                .append(getString(R.string.magic_bonus)).append(String.valueOf((int) a.getMagic())).append("%\n")
                .append(getString(R.string.sword_bonus)).append(String.valueOf((int) a.getSword())).append("%\n")
                .append(getString(R.string.staff_bonus)).append(String.valueOf((int) a.getStaff())).append("%\n")
                .append(getString(R.string.dagger_bonus)).append(String.valueOf((int) a.getDagger())).append("%\n")
                .append(getString(R.string.axe_bonus)).append(String.valueOf((int) a.getAxe())).append("%\n")
                .append(getString(R.string.hammer_bonus)).append(String.valueOf((int) a.getHammer())).append("%\n")
                .append(getString(R.string.spear_bonus)).append(String.valueOf((int) a.getSpear())).append("%\n");

        appendBooleanStatus(ssb, getString(R.string.frost_resistance), a.isFrostImmune());

        return ssb;
    }

    private CharSequence buildRingDesc(Ring r) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = r.getElement();
        String elementName = element.name();

        ssb.append(getString(R.string.element));
        int start = ssb.length();
        ssb.append(elementName);
        int end = ssb.length();
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.armor_in_calculation)).append(String.valueOf(r.getArmor())).append("\n")
                .append(getString(R.string.armor_bonus)).append(String.valueOf((int) r.getArmorBonus())).append("%\n")
                .append(getString(R.string.speed)).append(String.valueOf(r.getSpeed())).append("%\n")
                .append(getString(R.string.jump_impulse)).append(String.valueOf(r.getJump())).append("%\n")
                .append(getString(R.string.magic_bonus)).append(String.valueOf((int) r.getMagic())).append("%\n")
                .append(getString(R.string.sword_bonus)).append(String.valueOf((int) r.getSword())).append("%\n")
                .append(getString(R.string.staff_bonus)).append(String.valueOf((int) r.getStaff())).append("%\n")
                .append(getString(R.string.dagger_bonus)).append(String.valueOf((int) r.getDagger())).append("%\n")
                .append(getString(R.string.axe_bonus)).append(String.valueOf((int) r.getAxe())).append("%\n")
                .append(getString(R.string.hammer_bonus)).append(String.valueOf((int) r.getHammer())).append("%\n")
                .append(getString(R.string.spear_bonus)).append(String.valueOf((int) r.getSpear())).append("%\n");

        return ssb;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
