package xyz.magicrampagecompanion;

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
        Context context = this; // 'this' works because ItemDetail extends AppCompatActivity (which is a Context)
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
                // Return the default color (white in your case, or fetch your default text color)
                // Using the resource is better than hardcoding Color.WHITE
                return ContextCompat.getColor(context, R.color.white);
        }
    }

    private void appendBooleanStatus(SpannableStringBuilder ssb, String label, boolean isChecked) {
        // Append the text label part
        ssb.append(label).append(": ");

        // Determine which icon to use
        int iconResId = isChecked ? R.drawable.icon_check : R.drawable.icon_uncheck;
        Drawable iconDrawable = ContextCompat.getDrawable(this, iconResId);

        if (iconDrawable != null) {
            // Adjust icon size - make it similar to text line height
            int iconSize = (int) (itemDescription.getTextSize() * 1.2); // Example: slightly larger than text
            // Or set a fixed size like 35x35 or 40x40
            iconSize = 80;
            iconDrawable.setBounds(0, 0, iconSize, iconSize);

            // Append placeholder and set the ImageSpan
            int start = ssb.length();
            ssb.append(" "); // Placeholder for the icon
            int end = ssb.length();
            ImageSpan span = new ImageSpan(iconDrawable, ImageSpan.ALIGN_BASELINE);
            ssb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else {
            // Fallback if icons are missing
            ssb.append(isChecked ? "(✓)" : "(✗)"); // Or "(true)" / "(false)"
        }

        // Append the newline character AFTER the label and icon/fallback
        ssb.append("\n");
    }

    private CharSequence buildWeaponDesc(Weapon w) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = w.getElement();
        String elementName = element.name();

        ssb.append("Type: ").append(w.getType().name()).append("\n");

        ssb.append("Element: ");
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.append(elementName, new ForegroundColorSpan(color), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssb.append(elementName);
        }
        ssb.append("\n");

        ssb.append("Damage: ").append(String.valueOf(w.getMinDamage())).append("‑").append(String.valueOf(w.getMaxDamage())).append("\n")
                .append("Upgrades: ").append(String.valueOf(w.getUpgrades())).append("\n")
                .append("Armor Bonus: ").append(String.valueOf((int) w.getArmorBonus())).append("%\n")
                .append("Speed: ").append(String.valueOf(w.getSpeed())).append("%\n")
                .append("Jump: ").append(String.valueOf(w.getJump())).append("%\n");

        // --- Attack Speed Stars Logic ---
        ssb.append("Attack Speed: ");
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
        } else { ssb.append("(").append(String.valueOf(starCount)).append(" stars)"); }
        ssb.append("\n");
        // --- End Attack Speed Stars Logic ---

        ssb.append("Pierce Count: ").append(String.valueOf(w.getPierceCount())).append("\n");

        // --- Use helper function for boolean statuses ---
        appendBooleanStatus(ssb, "Pierce Area Damage", w.isEnablePierceAreaDamage());
        appendBooleanStatus(ssb, "Projectile Persistence", w.isPersistAgainstProjectile());
        appendBooleanStatus(ssb, "Poisonous Attack", w.isPoisonous());
        appendBooleanStatus(ssb, "Frost Attack", w.isFrost());
        // --- End boolean statuses ---

        // Remove final newline if handled by the last call to appendBooleanStatus
        // Ensure the last status line adds the final newline correctly.

        return ssb;
    }

    private CharSequence buildArmorDesc(Armor a) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = a.getElement();
        String elementName = element.name();

        ssb.append("Element: ");
        int start = ssb.length();
        ssb.append(elementName);
        int end = ssb.length();
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ssb.append("\n");

        ssb.append("Armor: ").append(String.valueOf(a.getMinArmor())).append("‑").append(String.valueOf(a.getMaxArmor())).append("\n")
                .append("Upgrades: ").append(String.valueOf(a.getUpgrades())).append("\n")
                .append("Speed: ").append(String.valueOf(a.getSpeed())).append("%\n")
                .append("Jump: ").append(String.valueOf(a.getJump())).append("%\n")
                .append("Magic: ").append(String.valueOf((int) a.getMagic())).append("%\n")
                .append("Sword Bonus: ").append(String.valueOf((int) a.getSword())).append("%\n")
                .append("Staff Bonus: ").append(String.valueOf((int) a.getStaff())).append("%\n")
                .append("Dagger Bonus: ").append(String.valueOf((int) a.getDagger())).append("%\n")
                .append("Axe Bonus: ").append(String.valueOf((int) a.getAxe())).append("%\n")
                .append("Hammer Bonus: ").append(String.valueOf((int) a.getHammer())).append("%\n")
                .append("Spear Bonus: ").append(String.valueOf((int) a.getSpear())).append("%\n"); // Keep newline here

        // --- Use helper function for boolean status ---
        appendBooleanStatus(ssb, "Frost Resistance", a.isFrostImmune());
        // --- End boolean status ---

        // Remove final newline if handled by the last call to appendBooleanStatus
        // Ensure the last status line adds the final newline correctly.

        return ssb;
    }

    private CharSequence buildRingDesc(Ring r) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        Elements element = r.getElement();
        String elementName = element.name();

        // --- Element Coloring Logic ---
        ssb.append("Element: ");
        int start = ssb.length();
        ssb.append(elementName);
        int end = ssb.length();
        int color = getElementColor(element);
        if (element != Elements.NEUTRAL) {
            ssb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ssb.append("\n");
        // --- End Element Coloring ---

        ssb.append("Armor: ").append(String.valueOf(r.getArmor())).append("\n")
                .append("Armor Bonus: ").append(String.valueOf((int) r.getArmorBonus())).append("%\n")
                .append("Speed: ").append(String.valueOf(r.getSpeed())).append("%\n")
                .append("Jump: ").append(String.valueOf(r.getJump())).append("%\n")
                .append("Magic: ").append(String.valueOf((int) r.getMagic())).append("%\n")
                .append("Sword Bonus: ").append(String.valueOf((int) r.getSword())).append("%\n")
                .append("Staff Bonus: ").append(String.valueOf((int) r.getStaff())).append("%\n")
                .append("Dagger Bonus: ").append(String.valueOf((int) r.getDagger())).append("%\n")
                .append("Axe Bonus: ").append(String.valueOf((int) r.getAxe())).append("%\n")
                .append("Hammer Bonus: ").append(String.valueOf((int) r.getHammer())).append("%\n")
                .append("Spear Bonus: ").append(String.valueOf((int) r.getSpear())).append("%\n");

        return ssb;
    }
}