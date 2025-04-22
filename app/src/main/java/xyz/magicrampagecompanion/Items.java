package xyz.magicrampagecompanion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Items extends AppCompatActivity {
    public static final String EXTRA_ITEM = "xyz.magicrampagecompanion.EXTRA_ITEM";

    private RecyclerView recyclerView;
    private Button button1, button2, button3, button4, button5, button6, button7, button8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);

        button1.setOnClickListener(v -> loadItems(ItemData.swordList));
        button2.setOnClickListener(v -> loadItems(ItemData.staffList));
        button3.setOnClickListener(v -> loadItems(ItemData.daggerList));
        button4.setOnClickListener(v -> loadItems(ItemData.axeList));
        button5.setOnClickListener(v -> loadItems(ItemData.spearList));
        button6.setOnClickListener(v -> loadItems(ItemData.hammerList));
        button7.setOnClickListener(v -> loadItems(ItemData.armorList));
        button8.setOnClickListener(v -> loadItems(ItemData.ringList));

        // default
        loadItems(ItemData.swordList);
    }

    private <T extends Parcelable> void loadItems(List<T> items) {
        ImageAdapter<T> adapter = new ImageAdapter<>(items, (view, position) -> {
            T selectedItem = items.get(position);
            Intent intent = new Intent(Items.this, ItemDetail.class);
            // **THIS** is the fix: use the Parcelable overload
            intent.putExtra(EXTRA_ITEM, selectedItem);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}
