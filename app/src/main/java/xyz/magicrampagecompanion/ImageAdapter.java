package xyz.magicrampagecompanion;

import android.annotation.SuppressLint;
import android.util.Log;
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
            holder.imageView.setImageBitmap(armor.getPicture());
            holder.itemName.setText(armor.getName());
        } else if (item instanceof Ring) {
            Ring ring = (Ring) item;
            holder.imageView.setImageBitmap(ring.getPicture());
            holder.itemName.setText(ring.getName());
        } else if (item instanceof Weapon) {
            Weapon weapon = (Weapon) item;
            holder.imageView.setImageBitmap(weapon.getPicture());
            holder.itemName.setText(weapon.getName());
        } else if (item instanceof CharacterClass) {
            CharacterClass characterClass = (CharacterClass) item;
            holder.imageView.setImageBitmap(characterClass.getPicture());
            holder.itemName.setText(characterClass.getName(holder.itemView.getContext()));
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

