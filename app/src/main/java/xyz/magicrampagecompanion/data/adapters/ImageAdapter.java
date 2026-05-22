package xyz.magicrampagecompanion.data.adapters;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.CharacterClass;
import xyz.magicrampagecompanion.data.models.Elixir;
import xyz.magicrampagecompanion.data.models.Enemy;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.enums.Elements;

public class ImageAdapter<T> extends RecyclerView.Adapter<ImageAdapter<T>.ImageViewHolder> {
    private final List<T> itemList;
    private final OnItemClickListener<T> itemClickListener;
    private final OnItemLongClickListener<T> longClickListener;

    public ImageAdapter(List<T> itemList, OnItemClickListener<T> itemClickListener) {
        this(itemList, itemClickListener, null);
    }

    public ImageAdapter(List<T> itemList, OnItemClickListener<T> itemClickListener,
                        OnItemLongClickListener<T> longClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
        this.longClickListener = longClickListener;
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ImageAdapter<T>.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);

        // Pre-attach an oval GradientDrawable to the dot so onBindViewHolder can
        // just call setColor() without allocating a new drawable each bind.
        View dot = view.findViewById(R.id.elementDot);
        GradientDrawable dotBg = new GradientDrawable();
        dotBg.setShape(GradientDrawable.OVAL);
        dot.setBackground(dotBg);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter<T>.ImageViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        T item = itemList.get(position);

        // Reset dot for every bind — only weapons show it
        holder.elementDot.setVisibility(View.GONE);

        if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            holder.imageView.setImageResource(weapon.getImageResId());
            holder.itemName.setText(weapon.getName());
            int dotColor = elementColor(weapon.getElement());
            if (dotColor != 0) {
                ((GradientDrawable) holder.elementDot.getBackground()).setColor(dotColor);
                holder.elementDot.setVisibility(View.VISIBLE);
            }

        } else if (item instanceof Armor) {
            Armor armor = (Armor) item;
            holder.imageView.setImageResource(armor.getImageResId());
            holder.itemName.setText(armor.getName());
            int armorDot = elementColor(armor.getElement());
            if (armorDot != 0) {
                ((GradientDrawable) holder.elementDot.getBackground()).setColor(armorDot);
                holder.elementDot.setVisibility(View.VISIBLE);
            }

        } else if (item instanceof Ring) {
            Ring ring = (Ring) item;
            holder.imageView.setImageResource(ring.getImageResId());
            holder.itemName.setText(ring.getName());
            int ringDot = elementColor(ring.getElement());
            if (ringDot != 0) {
                ((GradientDrawable) holder.elementDot.getBackground()).setColor(ringDot);
                holder.elementDot.setVisibility(View.VISIBLE);
            }

        } else if (item instanceof CharacterClass) {
            CharacterClass characterClass = (CharacterClass) item;
            holder.imageView.setImageResource(characterClass.getImageResId());
            holder.itemName.setText(characterClass.getName(holder.itemView.getContext()));

        } else if (item instanceof Enemy) {
            Enemy enemy = (Enemy) item;
            holder.imageView.setImageResource(enemy.getImageResId());
            holder.itemName.setText(enemy.getName());

        } else if (item instanceof Elixir) {
            Elixir elixir = (Elixir) item;
            holder.imageView.setImageResource(elixir.getImageResId());
            holder.itemName.setText(elixir.getName());

        } else {
            holder.imageView.setImageDrawable(null);
            holder.itemName.setText("");
        }

        // Whole card is the click target, not just the image
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, position));
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(v, position);
                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    // Returns the display color for a weapon element, or 0 for NEUTRAL (no dot).
    private static int elementColor(Elements element) {
        switch (element) {
            case FIRE:     return 0xFFEF6C00;
            case WATER:    return 0xFF42A5F5;
            case EARTH:    return 0xFF66BB6A;
            case AIR:      return 0xFF80DEEA;
            case LIGHT:    return 0xFFFFD54F;
            case DARKNESS: return 0xFFAB47BC;
            default:       return 0;
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;
        View elementDot;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView  = itemView.findViewById(R.id.imageView);
            itemName   = itemView.findViewById(R.id.itemName);
            elementDot = itemView.findViewById(R.id.elementDot);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> newData) {
        itemList.clear();
        itemList.addAll(newData);
        notifyDataSetChanged();
    }
}
