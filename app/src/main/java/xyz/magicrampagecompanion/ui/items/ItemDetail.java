package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.enums.Elements;
import xyz.magicrampagecompanion.enums.WeaponTypes;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class ItemDetail extends BaseActivity {
    public static final String EXTRA_ITEM = "xyz.magicrampagecompanion.EXTRA_ITEM";

    private ImageView itemImage;
    private TextView itemName;
    private LinearLayout statsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_detail);

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

        itemImage      = findViewById(R.id.itemImage);
        itemName       = findViewById(R.id.itemName);
        statsContainer = findViewById(R.id.statsContainer);

        Parcelable parcel = getIntent().getParcelableExtra(EXTRA_ITEM);
        if (parcel instanceof Weapon) {
            Weapon w = (Weapon) parcel;
            itemImage.setImageResource(w.getImageResId());
            itemName.setText(w.getName());
            populateWeaponStats(w);
        } else if (parcel instanceof Armor) {
            Armor a = (Armor) parcel;
            itemImage.setImageResource(a.getImageResId());
            itemName.setText(a.getName());
            populateArmorStats(a);
        } else if (parcel instanceof Ring) {
            Ring r = (Ring) parcel;
            itemImage.setImageResource(r.getImageResId());
            itemName.setText(r.getName());
            populateRingStats(r);
        }
    }

    // ── Section builders ─────────────────────────────────────────────────────────

    private void populateWeaponStats(Weapon w) {
        // STATS
        LinearLayout statsCard = addSection(getString(R.string.section_stats));
        addPairRow(statsCard,
                getString(R.string.type),                  getLocalizedWeaponType(w.getType()),
                getString(R.string.element),               buildElementSpan(w.getElement()));
        addPairRow(statsCard,
                getString(R.string.damage_in_calculation), w.getMinDamage() + " – " + w.getMaxDamage(),
                getString(R.string.upgrades),              String.valueOf(w.getUpgrades()));

        // ATTACK
        LinearLayout attackCard = addSection(getString(R.string.section_attack));
        addPairRow(attackCard,
                getString(R.string.attack_speed),          buildStarsSpan(w.getAttackCooldown()),
                getString(R.string.pierce_count),          String.valueOf(w.getPierceCount()));
        addPairRow(attackCard,
                getString(R.string.pierce_area_damage),    buildBooleanSpan(w.isEnablePierceAreaDamage()),
                getString(R.string.projectile_persistence),buildBooleanSpan(w.isPersistAgainstProjectile()));
        addPairRow(attackCard,
                getString(R.string.poisonous_attack),      buildBooleanSpan(w.isPoisonous()),
                getString(R.string.frost_attack),          buildBooleanSpan(w.isFrost()));

        // BONUSES (always shown)
        LinearLayout bonusCard = addSection(getString(R.string.section_bonuses));
        addPairRow(bonusCard,
                getString(R.string.jump_impulse_in_calculation),  sign(w.getJump()) + "%",
                getString(R.string.speed_in_calculation),         sign(w.getSpeed()) + "%");
        addRow(bonusCard,
                getString(R.string.armor_bonus),                  sign((int) w.getArmorBonus()) + "%");

        // ECONOMY
        addEconomySection(
                w.getFreemiumGoldPrice(), w.getPremiumGoldPrice(),
                w.getFreemiumCoinPrice(), w.getPremiumCoinPrice(),
                w.getBaseFreemiumSellPrice(), w.getBasePremiumSellPrice(),
                w.getObtainability());
    }

    private void populateArmorStats(Armor a) {
        // STATS
        LinearLayout statsCard = addSection(getString(R.string.section_stats));
        addPairRow(statsCard,
                getString(R.string.element),              buildElementSpan(a.getElement()),
                getString(R.string.frost_resistance),     buildBooleanSpan(a.isFrostImmune()));
        addPairRow(statsCard,
                getString(R.string.armor_in_calculation), a.getMinArmor() + " – " + a.getMaxArmor(),
                getString(R.string.upgrades),             String.valueOf(a.getUpgrades()));

        // BONUSES (always shown)
        LinearLayout bonusCard = addSection(getString(R.string.section_bonuses));
        addPairRow(bonusCard,
                getString(R.string.speed_in_calculation),        sign(a.getSpeed()) + "%",
                getString(R.string.jump_impulse_in_calculation), sign(a.getJump()) + "%");
        addPairRow(bonusCard,
                getString(R.string.magic_bonus),   sign((int) a.getMagic()) + "%",
                getString(R.string.sword_bonus),   sign((int) a.getSword()) + "%");
        addPairRow(bonusCard,
                getString(R.string.staff_bonus),   sign((int) a.getStaff()) + "%",
                getString(R.string.dagger_bonus),  sign((int) a.getDagger()) + "%");
        addPairRow(bonusCard,
                getString(R.string.axe_bonus),     sign((int) a.getAxe()) + "%",
                getString(R.string.hammer_bonus),  sign((int) a.getHammer()) + "%");
        addRow(bonusCard,
                getString(R.string.spear_bonus),   sign((int) a.getSpear()) + "%");

        // ECONOMY
        addEconomySection(
                a.getFreemiumGoldPrice(), a.getPremiumGoldPrice(),
                a.getFreemiumCoinPrice(), a.getPremiumCoinPrice(),
                a.getBaseFreemiumSellPrice(), a.getBasePremiumSellPrice(),
                a.getObtainability());
    }

    private void populateRingStats(Ring r) {
        // STATS
        LinearLayout statsCard = addSection(getString(R.string.section_stats));
        addRow(statsCard, getString(R.string.element), buildElementSpan(r.getElement()));

        // BONUSES (always shown)
        LinearLayout bonusCard = addSection(getString(R.string.section_bonuses));
        addPairRow(bonusCard,
                getString(R.string.armor_in_calculation), sign(r.getArmor()),
                getString(R.string.armor_bonus),          sign((int) r.getArmorBonus()) + "%");
        addPairRow(bonusCard,
                getString(R.string.speed_in_calculation),        sign(r.getSpeed()) + "%",
                getString(R.string.jump_impulse_in_calculation), sign(r.getJump()) + "%");
        addPairRow(bonusCard,
                getString(R.string.magic_bonus),   sign((int) r.getMagic()) + "%",
                getString(R.string.sword_bonus),   sign((int) r.getSword()) + "%");
        addPairRow(bonusCard,
                getString(R.string.staff_bonus),   sign((int) r.getStaff()) + "%",
                getString(R.string.dagger_bonus),  sign((int) r.getDagger()) + "%");
        addPairRow(bonusCard,
                getString(R.string.axe_bonus),     sign((int) r.getAxe()) + "%",
                getString(R.string.hammer_bonus),  sign((int) r.getHammer()) + "%");
        addRow(bonusCard,
                getString(R.string.spear_bonus),   sign((int) r.getSpear()) + "%");

        // ECONOMY
        addEconomySection(
                r.getFreemiumGoldPrice(), r.getPremiumGoldPrice(),
                r.getFreemiumCoinPrice(), r.getPremiumCoinPrice(),
                r.getBaseFreemiumSellPrice(), r.getBasePremiumSellPrice(),
                r.getObtainability());
    }

    private void addEconomySection(int fg, int pg, int fc, int pc, int sf, int sp,
                                   List<String> obtainability) {
        boolean hasGold  = fg > 0 || pg > 0;
        boolean hasCoins = fc > 0 || pc > 0;
        boolean hasSell  = sf > 0 || sp > 0;
        boolean hasObtainability = obtainability != null && !obtainability.isEmpty();
        if (!hasGold && !hasCoins && !hasSell && !hasObtainability) return;

        LinearLayout card = addSection(getString(R.string.section_economy));
        if (hasGold)  addPairRow(card,
                getString(R.string.price_freemium_gold),  String.valueOf(fg),
                getString(R.string.price_premium_gold),   String.valueOf(pg));
        if (hasCoins) addPairRow(card,
                getString(R.string.price_freemium_token), String.valueOf(fc),
                getString(R.string.price_premium_token),  String.valueOf(pc));
        if (hasSell)  addPairRow(card,
                getString(R.string.price_freemium_sell),  String.valueOf(sf),
                getString(R.string.price_premium_sell),   String.valueOf(sp));

        if (hasObtainability) {
            addRow(card, getString(R.string.obtainability), "");
            for (String entry : obtainability) addTextRow(card, "• " + entry);
        }
    }

    // ── View helpers ──────────────────────────────────────────────────────────────

    private LinearLayout addSection(String title) {
        TextView header = (TextView) getLayoutInflater().inflate(
                R.layout.item_detail_section_header, statsContainer, false);
        header.setText(title);
        statsContainer.addView(header);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.table_card_bg);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        statsContainer.addView(card);
        return card;
    }

    private void addRow(LinearLayout card, String label, CharSequence value) {
        if (card.getChildCount() > 0) addDivider(card);
        View row = getLayoutInflater().inflate(R.layout.item_detail_stat_row, card, false);
        ((TextView) row.findViewById(R.id.statLabel)).setText(label);
        ((TextView) row.findViewById(R.id.statValue)).setText(value);
        card.addView(row);
    }

    private void addPairRow(LinearLayout card,
                            String label1, CharSequence val1,
                            String label2, CharSequence val2) {
        if (card.getChildCount() > 0) addDivider(card);
        View row = getLayoutInflater().inflate(R.layout.item_detail_stat_pair, card, false);
        ((TextView) row.findViewById(R.id.labelLeft)).setText(label1);
        ((TextView) row.findViewById(R.id.valueLeft)).setText(val1);
        ((TextView) row.findViewById(R.id.labelRight)).setText(label2);
        ((TextView) row.findViewById(R.id.valueRight)).setText(val2);
        card.addView(row);
    }

    private void addTextRow(LinearLayout card, String text) {
        TextView tv = new TextView(this);
        tv.setTextColor(ContextCompat.getColor(this, R.color.color_text_primary));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv.setPadding(dp(14), dp(8), dp(14), dp(8));
        tv.setText(text);
        card.addView(tv);
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

    // ── Span builders ─────────────────────────────────────────────────────────────

    private CharSequence buildElementSpan(Elements element) {
        String name = getLocalizedElementName(element);
        if (element == Elements.NEUTRAL) return name;
        SpannableStringBuilder ssb = new SpannableStringBuilder(name);
        ssb.setSpan(new ForegroundColorSpan(getElementColor(element)),
                0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private SpannableStringBuilder buildStarsSpan(int cooldownMs) {
        int starCount;
        if (cooldownMs <= 300)      starCount = 5;
        else if (cooldownMs <= 450) starCount = 4;
        else if (cooldownMs <= 650) starCount = 3;
        else if (cooldownMs <= 750) starCount = 2;
        else                        starCount = 1;

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Drawable star = ContextCompat.getDrawable(this, R.drawable.star);
        if (star != null) {
            int size = dp(18);
            star.setBounds(0, 0, size, size);
            for (int i = 0; i < starCount; i++) {
                int start = ssb.length();
                ssb.append(" ");
                ssb.setSpan(new CenteredImageSpan(star),
                        start, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            for (int i = 0; i < starCount; i++) ssb.append("★");
        }
        return ssb;
    }

    private SpannableStringBuilder buildBooleanSpan(boolean value) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Drawable icon = ContextCompat.getDrawable(this,
                value ? R.drawable.icon_check : R.drawable.icon_uncheck);
        if (icon != null) {
            int size = dp(18);
            icon.setBounds(0, 0, size, size);
            ssb.append(" ");
            ssb.setSpan(new CenteredImageSpan(icon),
                    0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(value ? "✓" : "✗");
        }
        return ssb;
    }

    // ── Misc helpers ──────────────────────────────────────────────────────────────

    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }

    private String sign(int n) {
        return n > 0 ? "+" + n : String.valueOf(n);
    }

    private int getElementColor(Elements element) {
        switch (element) {
            case WATER:    return ContextCompat.getColor(this, R.color.water_element_color);
            case EARTH:    return ContextCompat.getColor(this, R.color.earth_element_color);
            case FIRE:     return ContextCompat.getColor(this, R.color.fire_element_color);
            case DARKNESS: return ContextCompat.getColor(this, R.color.darkness_element_color);
            case LIGHT:    return ContextCompat.getColor(this, R.color.light_element_color);
            case AIR:      return ContextCompat.getColor(this, R.color.air_element_color);
            default:       return ContextCompat.getColor(this, R.color.white);
        }
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


    private static final class CenteredImageSpan extends ImageSpan {
        CenteredImageSpan(Drawable d) { super(d); }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            if (fm != null) {
                Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
                int center = (pfm.ascent + pfm.descent) / 2;
                int half = d.getBounds().height() / 2;
                fm.ascent = center - half;
                fm.descent = center + half;
                fm.top = fm.ascent;
                fm.bottom = fm.descent;
            }
            return d.getBounds().right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            canvas.save();
            Drawable d = getDrawable();
            canvas.translate(x, (top + bottom - d.getBounds().height()) / 2f);
            d.draw(canvas);
            canvas.restore();
        }
    }
}
