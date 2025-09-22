package xyz.magicrampagecompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedSetsAdapter extends RecyclerView.Adapter<SavedSetsAdapter.VH> {
    interface OnRowClick { void onClick(String name); }
    interface OnDeleteClick { void onDelete(String name); }

    private final List<String> names;
    private final OnRowClick onRowClick;
    private final OnDeleteClick onDeleteClick;

    SavedSetsAdapter(List<String> names, OnRowClick onRowClick, OnDeleteClick onDeleteClick) {
        this.names = names;
        this.onRowClick = onRowClick;
        this.onDeleteClick = onDeleteClick;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        ImageButton trash;
        VH(View v) {
            super(v);
            tv = v.findViewById(R.id.tvName);
            trash = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_saved_set, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String name = names.get(position);
        h.tv.setText(name);
        h.itemView.setOnClickListener(v -> onRowClick.onClick(name));
        h.trash.setOnClickListener(v -> onDeleteClick.onDelete(name));
    }

    @Override
    public int getItemCount() { return names.size(); }

    void removeName(String name) {
        int idx = names.indexOf(name);
        if (idx >= 0) {
            names.remove(idx);
            notifyItemRemoved(idx);
        }
    }
}
