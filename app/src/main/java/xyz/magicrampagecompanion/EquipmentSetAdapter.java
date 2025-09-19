package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface Listener {
        void onPickArmor(int position);
        void onPickRing(int position);
        void onPickWeapon(int position);
        void onPickClass(int position);
        void onPickSkills(int position);
        void onRemoveSet(int position);

        // Element pickers per slot
        void onPickArmorElement(int position);
        void onPickWeaponElement(int position);
        void onPickRingElement(int position);

        default void onAddSetTapped(boolean atLimit) {}
    }

    private static final int TYPE_SET = 0;
    private static final int TYPE_ADD = 1;
    public static final int MAX_SETS = 10;

    private final List<EquipmentSet> data = new ArrayList<>();
    private final Listener listener;

    public EquipmentSetAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(false);
    }

    private boolean isFooterVisible() { return data.size() < MAX_SETS; }
    private boolean isFooterPosition(int position) { return isFooterVisible() && position == data.size(); }

    @Override public long getItemId(int position) { return position; }
    @Override public int getItemViewType(int position) { return isFooterPosition(position) ? TYPE_ADD : TYPE_SET; }
    @Override public int getItemCount() { return data.size() + (isFooterVisible() ? 1 : 0); }

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<EquipmentSet> items) {
        data.clear();
        if (items != null) data.addAll(items.size() > MAX_SETS ? items.subList(0, MAX_SETS) : items);
        notifyDataSetChanged();
    }

    public void addEmpty() {
        if (data.size() >= MAX_SETS) {
            if (listener != null) listener.onAddSetTapped(true);
            return;
        }
        int insertPos = data.size();
        data.add(new EquipmentSet());
        notifyItemInserted(insertPos);
        if (data.size() == MAX_SETS) notifyItemRemoved(MAX_SETS); // footer disappears
        if (listener != null) listener.onAddSetTapped(false);
    }

    public void removeAt(int position) {
        if (position < 0 || position >= data.size()) return;
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size() - position);
        if (data.size() == MAX_SETS - 1) notifyItemInserted(data.size()); // footer reappears
    }

    public EquipmentSet getItem(int position) { return data.get(position); }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_ADD) {
            View v = inf.inflate(R.layout.item_set_add, parent, false);
            return new AddVH(v);
        } else {
            View v = inf.inflate(R.layout.item_set, parent, false);
            return new SetVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        if (getItemViewType(pos) == TYPE_ADD) {
            ((AddVH) holder).addButton.setOnClickListener(v -> addEmpty());
            return;
        }

        SetVH h = (SetVH) holder;
        EquipmentSet set = data.get(pos);

        // Main images
        h.armor.setImageResource(set.armor != null ? set.armor.getImageResId() : R.drawable.dummy_armor);
        h.ring.setImageResource(set.ring != null ? set.ring.getImageResId() : R.drawable.dummy_ring);
        h.weapon.setImageResource(set.weapon != null ? set.weapon.getImageResId() : R.drawable.dummy_sword);

        // Class & skills
        h.classBtn.setImageResource(set.characterClass != null ? set.characterClass.getImageResId() : R.drawable.select_class_button_grey);
        h.skillBtn.setImageResource(anySkillPicked(set.skills) ? R.drawable.select_skill_tree_button : R.drawable.select_skill_tree_button_grey);

        // Compute real stats once
        SetStats stats = StatsCalculator.compute(set);

        // Armor value (base + bonus)
        if (set.armor != null) {
            int baseArmor = set.armor.getMaxArmor();
            int bonusArmor = Math.max(0, stats.armor - baseArmor);
            String armorText = baseArmor + " <font color='#8cfdfc'>+" + bonusArmor + "</font>";
            h.armorValue.setText(Html.fromHtml(armorText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            h.armorValue.setText(h.itemView.getContext().getString(R.string.armor));
        }

        // Weapon value (base + bonus)
        if (set.weapon != null) {
            int baseDmg = set.weapon.getMaxDamage();
            int bonusDmg = Math.max(0, stats.damage - baseDmg);
            String weaponText = baseDmg + " <font color='#8cfdfc'>+" + bonusDmg + "</font>";
            h.weaponValue.setText(Html.fromHtml(weaponText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            h.weaponValue.setText(h.itemView.getContext().getString(R.string.damage));
        }

        // Fire resistance side icon
        if (stats.fireRes) {
            h.armorSideIcon.setVisibility(View.VISIBLE);
            h.armorSideIcon.setImageResource(R.drawable.fire_protection); // your chosen icon
        } else {
            h.armorSideIcon.setVisibility(View.GONE);
        }

        // Element button icons: lock if item intrinsic element is non-neutral; edit if neutral; chosen element icon if user picked one
        bindElementIcon(h.elementArmorBtn,  set.armor,  set.armorElement);
        bindElementIcon(h.elementWeaponBtn, set.weapon, set.weaponElement);
        bindElementIcon(h.elementRingBtn,   set.ring,   set.ringElement);

        // Click listeners (main)
        h.armor.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickArmor(p); });
        h.ring.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickRing(p); });
        h.weapon.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickWeapon(p); });
        h.classBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickClass(p); });
        h.skillBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickSkills(p); });
        h.removeBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onRemoveSet(p); });

        // Click listeners (elements)
        h.elementArmorBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickArmorElement(p); });
        h.elementWeaponBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickWeaponElement(p); });
        h.elementRingBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickRingElement(p); });
    }

    private static boolean anySkillPicked(boolean[] skills) {
        if (skills == null) return false;
        for (boolean b : skills) if (b) return true;
        return false;
    }

    /** Decides which icon to show on the element buttons. */
    /** Decides which icon to show on the element buttons. */
    /** Decides which icon to show on the element buttons. */
    /** Decides which icon to show on the element buttons. */
    private void bindElementIcon(ImageButton target, Object itemObj, Elements chosenElement) {
        if (itemObj == null) {
            target.setImageResource(R.drawable.icon_lock);
            return;
        }

        Elements intrinsic;
        if (itemObj instanceof Armor) intrinsic = ((Armor) itemObj).getElement();
        else if (itemObj instanceof Weapon) intrinsic = ((Weapon) itemObj).getElement();
        else intrinsic = ((Ring) itemObj).getElement();

        if (intrinsic != null && intrinsic != Elements.NEUTRAL) {
            // Show actual element icon if item has built-in element
            target.setImageResource(elementDrawableFor(intrinsic));
            return;
        }

        // Intrinsic neutral → use chosen element or edit icon
        if (chosenElement != null && chosenElement != Elements.NEUTRAL) {
            target.setImageResource(elementDrawableFor(chosenElement));
        } else {
            target.setImageResource(R.drawable.edit_icon);
        }
    }

    private int elementDrawableFor(Elements e) {
        switch (e) {
            case FIRE:     return R.drawable.element_fire;
            case WATER:    return R.drawable.element_water;
            case DARKNESS: return R.drawable.element_darkness;
            case LIGHT:    return R.drawable.element_light;
            case EARTH:    return R.drawable.element_earth;
            case AIR:      return R.drawable.element_air;
            default:       return R.drawable.edit_icon; // fallback
        }
    }

    // ---------- view holders ----------
    static class SetVH extends RecyclerView.ViewHolder {
        ImageButton armor, ring, weapon, classBtn, skillBtn, removeBtn;
        ImageButton elementArmorBtn, elementWeaponBtn, elementRingBtn;
        ImageView armorSideIcon;
        TextView armorValue, weaponValue;

        SetVH(@NonNull View v) {
            super(v);
            armor           = v.findViewById(R.id.ArmorButton);
            ring            = v.findViewById(R.id.RingButton);
            weapon          = v.findViewById(R.id.WeaponButton);
            classBtn        = v.findViewById(R.id.ClassButton);
            skillBtn        = v.findViewById(R.id.SkillTreeButton);
            removeBtn       = v.findViewById(R.id.RemoveSetButton);
            armorValue      = v.findViewById(R.id.armorValueText);
            weaponValue     = v.findViewById(R.id.weaponValueText);
            armorSideIcon   = v.findViewById(R.id.ArmorSideIcon);

            elementArmorBtn = v.findViewById(R.id.ElementArmorButton);
            elementWeaponBtn= v.findViewById(R.id.ElementWeaponButton);
            elementRingBtn  = v.findViewById(R.id.ElementRingButton);
        }
    }

    static class AddVH extends RecyclerView.ViewHolder {
        ImageButton addButton;
        AddVH(@NonNull View v) {
            super(v);
            addButton = v.findViewById(R.id.AddSetFooterButton);
        }
    }
}
