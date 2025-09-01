package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter<T> extends RecyclerView.Adapter<ImageAdapter<T>.ImageViewHolder> {
    private List<T> itemList;
    private OnItemClickListener<T> itemClickListener;

    public ImageAdapter(List<T> itemList, OnItemClickListener<T> itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ImageAdapter<T>.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter<T>.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        T item = itemList.get(position);

        if (item instanceof Armor) {
            Armor armor = (Armor) item;
            holder.imageView.setImageResource(armor.getImageResId());
            holder.itemName.setText(armor.getName());
        } else if (item instanceof Ring) {
            Ring ring = (Ring) item;
            holder.imageView.setImageResource(ring.getImageResId());
            holder.itemName.setText(ring.getName());
        } else if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            holder.imageView.setImageResource(weapon.getImageResId());
            holder.itemName.setText(weapon.getName());
        } else if (item instanceof CharacterClass) {
            CharacterClass characterClass = (CharacterClass) item;
            holder.imageView.setImageResource(characterClass.getImageResId());
            holder.itemName.setText(characterClass.getName(holder.itemView.getContext()));
        } else if (item instanceof Enemy) {
            Enemy enemy = (Enemy) item;
            holder.imageView.setImageResource(enemy.getImageResId());
            holder.itemName.setText(enemy.getName());

        } else {
            // nothing matched, clear out
            holder.imageView.setImageDrawable(null);
            holder.itemName.setText("");
        }

        // Set the click listener for the ImageView
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemName = itemView.findViewById(R.id.itemName);
        }
    }

    // Interface for item click events
    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> newData) {
        itemList.clear(); // Clear the existing data
        itemList.addAll(newData); // Add the new data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }
}
