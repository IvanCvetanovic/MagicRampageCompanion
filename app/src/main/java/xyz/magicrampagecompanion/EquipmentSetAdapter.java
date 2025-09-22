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
        void onSaveSet(int position);
        void onShowStats(int position);

        // Element pickers per slot
        void onPickArmorElement(int position);
        void onPickWeaponElement(int position);
        void onPickRingElement(int position);

        // Elixir picker
        void onPickDrink(int position);

        // Footer actions
        default void onFooterLoadTapped() {}
        default void onFooterAddTapped(boolean atLimit) {}
        default void onFooterRecommendTapped() {}
    }

    private static final int TYPE_SET = 0;
    private static final int TYPE_FOOTER = 1;
    public static final int MAX_SETS = 10;

    private final List<EquipmentSet> data = new ArrayList<>();
    private final Listener listener;

    public EquipmentSetAdapter(Listener listener) {
        this.listener = listener;
        setHasStableIds(false);
    }

    private boolean isFooterPosition(int position) { return position == data.size(); }

    @Override public long getItemId(int position) { return position; }
    @Override public int getItemViewType(int position) { return isFooterPosition(position) ? TYPE_FOOTER : TYPE_SET; }
    @Override public int getItemCount() { return data.size() + 1; } // footer always visible

    @SuppressLint("NotifyDataSetChanged")
    public void setItems(List<EquipmentSet> items) {
        data.clear();
        if (items != null) data.addAll(items.size() > MAX_SETS ? items.subList(0, MAX_SETS) : items);
        notifyDataSetChanged();
    }

    public List<EquipmentSet> getItems() { return data; }

    public void addEmpty() {
        if (data.size() >= MAX_SETS) {
            if (listener != null) listener.onFooterAddTapped(true);
            return;
        }
        int insertPos = data.size();
        data.add(new EquipmentSet());
        notifyItemInserted(insertPos);
        if (listener != null) listener.onFooterAddTapped(false);
    }

    public void removeAt(int position) {
        if (position < 0 || position >= data.size()) return;
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size() - position + 1); // + footer
    }

    public EquipmentSet getItem(int position) { return data.get(position); }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FOOTER) {
            View v = inf.inflate(R.layout.item_set_add, parent, false);
            return new FooterVH(v);
        } else {
            View v = inf.inflate(R.layout.item_set, parent, false);
            return new SetVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        if (getItemViewType(pos) == TYPE_FOOTER) {
            FooterVH f = (FooterVH) holder;
            f.btnLoad.setOnClickListener(v -> { if (listener != null) listener.onFooterLoadTapped(); });
            f.btnAdd.setOnClickListener(v -> addEmpty());
            f.btnRecommend.setOnClickListener(v -> { if (listener != null) listener.onFooterRecommendTapped(); });
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

        ImageButton statsBtn = holder.itemView.findViewById(R.id.StatsButton);
        statsBtn.setOnClickListener(v -> listener.onShowStats(holder.getAdapterPosition()));

        if (h.saveBtn != null) {
            h.saveBtn.setOnClickListener(v -> {
                int p = h.getAdapterPosition();
                if (p != RecyclerView.NO_POSITION && listener != null) {
                    listener.onSaveSet(p);
                }
            });
        }

        // Elixir image (drink button)
        if (h.drinkBtn != null) {
            if (set.elixir != null) {
                h.drinkBtn.setImageResource(set.elixir.getImageResId());
            } else {
                h.drinkBtn.setImageResource(R.drawable.drink);
            }
        }

        // Compute stats once
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
            h.armorSideIcon.setImageResource(R.drawable.fire_protection);
        } else {
            h.armorSideIcon.setVisibility(View.GONE);
        }

        // Element badges
        bindElementIcon(h.elementArmorBtn,  set.armor,  set.armorElement);
        bindElementIcon(h.elementWeaponBtn, set.weapon, set.weaponElement);
        bindElementIcon(h.elementRingBtn,   set.ring,   set.ringElement);

        // Clicks
        h.armor.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickArmor(p); });
        h.ring.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickRing(p); });
        h.weapon.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickWeapon(p); });
        h.classBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickClass(p); });
        h.skillBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickSkills(p); });
        h.removeBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onRemoveSet(p); });

        // Elixir click
        if (h.drinkBtn != null) {
            h.drinkBtn.setOnClickListener(v -> {
                int p = h.getAdapterPosition();
                if (p != RecyclerView.NO_POSITION) listener.onPickDrink(p);
            });
        }

        // Element picker clicks
        h.elementArmorBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickArmorElement(p); });
        h.elementWeaponBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickWeaponElement(p); });
        h.elementRingBtn.setOnClickListener(v -> { int p = h.getAdapterPosition(); if (p != RecyclerView.NO_POSITION) listener.onPickRingElement(p); });
    }

    private static boolean anySkillPicked(boolean[] skills) {
        if (skills == null) return false;
        for (boolean b : skills) if (b) return true;
        return false;
    }

    private void bindElementIcon(ImageButton target, Object itemObj, Elements chosenElement) {
        if (target == null) return;

        if (itemObj == null) {
            target.setImageResource(R.drawable.icon_lock);
            return;
        }

        Elements intrinsic;
        if (itemObj instanceof Armor) intrinsic = ((Armor) itemObj).getElement();
        else if (itemObj instanceof Weapon) intrinsic = ((Weapon) itemObj).getElement();
        else intrinsic = ((Ring) itemObj).getElement();

        if (intrinsic != null && intrinsic != Elements.NEUTRAL) {
            target.setImageResource(elementDrawableFor(intrinsic));
            return;
        }

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
            default:       return R.drawable.edit_icon;
        }
    }

    // ---------- view holders ----------
    static class SetVH extends RecyclerView.ViewHolder {
        ImageButton armor, ring, weapon, classBtn, skillBtn, removeBtn, saveBtn, statsBtn;
        ImageButton elementArmorBtn, elementWeaponBtn, elementRingBtn;
        ImageButton drinkBtn;
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
            saveBtn = v.findViewById(R.id.SaveSetButton);

            elementArmorBtn = v.findViewById(R.id.ElementArmorButton);
            elementWeaponBtn= v.findViewById(R.id.ElementWeaponButton);
            elementRingBtn  = v.findViewById(R.id.ElementRingButton);

            drinkBtn        = v.findViewById(R.id.DrinkButton); // in item_set.xml
        }
    }

    static class FooterVH extends RecyclerView.ViewHolder {
        ImageButton btnLoad, btnAdd, btnRecommend;
        FooterVH(@NonNull View v) {
            super(v);
            btnLoad      = v.findViewById(R.id.TopPlaceholderButton);       // Load
            btnAdd       = v.findViewById(R.id.AddSetFooterButton);         // New
            btnRecommend = v.findViewById(R.id.BottomPlaceholderButton);    // Recommend
        }
    }
}
