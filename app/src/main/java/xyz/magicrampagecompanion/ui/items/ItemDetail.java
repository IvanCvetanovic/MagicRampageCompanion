package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.WeaponTypes;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

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

        // --- Edge-to-edge padding fix ---
        final View root = findViewById(R.id.detail_root);
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

        // --- UI refs ---
        itemImage       = findViewById(R.id.itemImage);
        itemName        = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);

        // --- Fill with data ---
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
            case WATER:    return ContextCompat.getColor(context, R.color.water_element_color);
            case EARTH:    return ContextCompat.getColor(context, R.color.earth_element_color);
            case FIRE:     return ContextCompat.getColor(context, R.color.fire_element_color);
            case DARKNESS: return ContextCompat.getColor(context, R.color.darkness_element_color);
            case LIGHT:    return ContextCompat.getColor(context, R.color.light_element_color);
            case AIR:      return ContextCompat.getColor(context, R.color.air_element_color);
            case NEUTRAL:
            default:       return ContextCompat.getColor(context, R.color.white);
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

    private void appendObtainability(SpannableStringBuilder ssb, List<String> list) {
        if (list == null || list.isEmpty()) return;

        ssb.append(getString(R.string.obtainability)).append("\n");

        for (String entry : list) {
            ssb.append("• ").append(entry).append("\n");
        }
    }


    private void appendIfNonZeroInt(SpannableStringBuilder ssb, int labelResId, int value) {
        if (value != 0) {
            ssb.append(getString(labelResId)).append(String.valueOf(value)).append("\n");
        }
    }

    private void appendIfNonZeroPercent(SpannableStringBuilder ssb, int labelResId, int percentValue) {
        if (percentValue != 0) {
            ssb.append(getString(labelResId)).append(String.valueOf(percentValue)).append("%\n");
        }
    }

    private void appendPrices(SpannableStringBuilder ssb,
                              int fg, int pg, int fc, int pc, int sf, int sp) {
        appendIfNonZeroInt(ssb, R.string.price_freemium_gold, fg);
        appendIfNonZeroInt(ssb, R.string.price_premium_gold,  pg);
        appendIfNonZeroInt(ssb, R.string.price_freemium_token, fc);
        appendIfNonZeroInt(ssb, R.string.price_premium_token,  pc);
        appendIfNonZeroInt(ssb, R.string.price_freemium_sell,  sf);
        appendIfNonZeroInt(ssb, R.string.price_premium_sell,   sp);
    }

    private CharSequence buildWeaponDesc(Weapon w) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = w.getElement();

        ssb.append(getString(R.string.type))
                .append(getLocalizedWeaponType(w.getType()))
                .append("\n");

        ssb.append(getString(R.string.element));
        int color = getElementColor(element);
        String localizedElementName = getLocalizedElementName(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(localizedElementName, new ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(localizedElementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.damage_in_calculation))
                .append(String.valueOf(w.getMinDamage())).append("-").append(String.valueOf(w.getMaxDamage())).append("\n")
                .append(getString(R.string.upgrades)).append(String.valueOf(w.getUpgrades())).append("\n");

        appendIfNonZeroPercent(ssb, R.string.armor_bonus, (int) w.getArmorBonus());
        appendIfNonZeroPercent(ssb, R.string.speed,       w.getSpeed());
        appendIfNonZeroPercent(ssb, R.string.jump_impulse, w.getJump());

        ssb.append(getString(R.string.attack_speed));
        double currentAttackSpeed = w.getAttackCooldown();
        int starCount;
        if (currentAttackSpeed <= 300)      starCount = 5;
        else if (currentAttackSpeed <= 450) starCount = 4;
        else if (currentAttackSpeed <= 650) starCount = 3;
        else if (currentAttackSpeed <= 750) starCount = 2;
        else                                starCount = 1;

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

        appendIfNonZeroInt(ssb, R.string.pierce_count, w.getPierceCount());

        appendBooleanStatus(ssb, getString(R.string.pierce_area_damage), w.isEnablePierceAreaDamage());
        appendBooleanStatus(ssb, getString(R.string.projectile_persistence), w.isPersistAgainstProjectile());
        appendBooleanStatus(ssb, getString(R.string.poisonous_attack), w.isPoisonous());
        appendBooleanStatus(ssb, getString(R.string.frost_attack), w.isFrost());

        appendPrices(
                ssb,
                w.getFreemiumGoldPrice(),
                w.getPremiumGoldPrice(),
                w.getFreemiumCoinPrice(),
                w.getPremiumCoinPrice(),
                w.getBaseFreemiumSellPrice(),
                w.getBasePremiumSellPrice()
        );

        appendObtainability(ssb, w.getObtainability());

        return ssb;
    }

    private CharSequence buildArmorDesc(Armor a) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = a.getElement();

        ssb.append(getString(R.string.element));
        int color = getElementColor(element);
        String localizedElementName = getLocalizedElementName(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(localizedElementName, new ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(localizedElementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.armor_in_calculation))
                .append(String.valueOf(a.getMinArmor())).append("-").append(String.valueOf(a.getMaxArmor())).append("\n")
                .append(getString(R.string.upgrades)).append(String.valueOf(a.getUpgrades())).append("\n");

        appendIfNonZeroPercent(ssb, R.string.speed,        a.getSpeed());
        appendIfNonZeroPercent(ssb, R.string.jump_impulse, a.getJump());
        appendIfNonZeroPercent(ssb, R.string.magic_bonus,  (int) a.getMagic());
        appendIfNonZeroPercent(ssb, R.string.sword_bonus,  (int) a.getSword());
        appendIfNonZeroPercent(ssb, R.string.staff_bonus,  (int) a.getStaff());
        appendIfNonZeroPercent(ssb, R.string.dagger_bonus, (int) a.getDagger());
        appendIfNonZeroPercent(ssb, R.string.axe_bonus,    (int) a.getAxe());
        appendIfNonZeroPercent(ssb, R.string.hammer_bonus, (int) a.getHammer());
        appendIfNonZeroPercent(ssb, R.string.spear_bonus,  (int) a.getSpear());

        appendBooleanStatus(ssb, getString(R.string.frost_resistance), a.isFrostImmune());

        appendPrices(
                ssb,
                a.getFreemiumGoldPrice(),
                a.getPremiumGoldPrice(),
                a.getFreemiumCoinPrice(),
                a.getPremiumCoinPrice(),
                a.getBaseFreemiumSellPrice(),
                a.getBasePremiumSellPrice()
        );

        appendObtainability(ssb, a.getObtainability());

        return ssb;
    }

    private CharSequence buildRingDesc(Ring r) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = r.getElement();

        ssb.append(getString(R.string.element));
        int color = getElementColor(element);
        String localizedElementName = getLocalizedElementName(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(localizedElementName, new ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(localizedElementName);
        }
        ssb.append("\n");

        ssb.append(getString(R.string.armor_in_calculation)).append(String.valueOf(r.getArmor())).append("\n");

        appendIfNonZeroPercent(ssb, R.string.armor_bonus, (int) r.getArmorBonus());
        appendIfNonZeroPercent(ssb, R.string.speed,       r.getSpeed());
        appendIfNonZeroPercent(ssb, R.string.jump_impulse, r.getJump());
        appendIfNonZeroPercent(ssb, R.string.magic_bonus,  (int) r.getMagic());
        appendIfNonZeroPercent(ssb, R.string.sword_bonus,  (int) r.getSword());
        appendIfNonZeroPercent(ssb, R.string.staff_bonus,  (int) r.getStaff());
        appendIfNonZeroPercent(ssb, R.string.dagger_bonus, (int) r.getDagger());
        appendIfNonZeroPercent(ssb, R.string.axe_bonus,    (int) r.getAxe());
        appendIfNonZeroPercent(ssb, R.string.hammer_bonus, (int) r.getHammer());
        appendIfNonZeroPercent(ssb, R.string.spear_bonus,  (int) r.getSpear());

        appendPrices(
                ssb,
                r.getFreemiumGoldPrice(),
                r.getPremiumGoldPrice(),
                r.getFreemiumCoinPrice(),
                r.getPremiumCoinPrice(),
                r.getBaseFreemiumSellPrice(),
                r.getBasePremiumSellPrice()
        );

        appendObtainability(ssb, r.getObtainability());

        return ssb;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    private String getLocalizedElementName(Elements element) {
        switch (element) {
            case FIRE:     return getString(R.string.element_fire);
            case WATER:    return getString(R.string.element_water);
            case DARKNESS: return getString(R.string.element_darkness);
            case LIGHT:    return getString(R.string.element_light);
            case EARTH:    return getString(R.string.element_earth);
            case AIR:      return getString(R.string.element_air);
            default:       return getString(R.string.element_neutral);
        }
    }

    private String getLocalizedWeaponType(WeaponTypes type) {
        switch (type) {
            case SWORD:  return getString(R.string.weapon_type_sword);
            case STAFF:  return getString(R.string.weapon_type_staff);
            case DAGGER: return getString(R.string.weapon_type_dagger);
            case AXE:    return getString(R.string.weapon_type_axe);
            case HAMMER: return getString(R.string.weapon_type_hammer);
            case SPEAR:  return getString(R.string.weapon_type_spear);
            default:     return "";
        }
    }
}