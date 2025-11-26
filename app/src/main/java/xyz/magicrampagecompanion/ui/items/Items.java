package xyz.magicrampagecompanion.ui.items;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.adapters.ImageAdapter;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.core.utils.LocaleHelper;

public class Items extends AppCompatActivity {
    public static final String EXTRA_ITEM = "xyz.magicrampagecompanion.EXTRA_ITEM";

    private RecyclerView recyclerView;
    private HorizontalScrollView tabs;
    private EditText searchEdit;

    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8;
    private List<?> currentFullList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items);

        ItemData.init(this);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setClipToPadding(false);

        tabs = findViewById(R.id.buttonsScrollView);
        searchEdit = findViewById(R.id.searchEdit);

        // Tabs
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);

        applySystemInsets(findViewById(R.id.main), tabs, searchEdit, recyclerView);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { applyFilter(s.toString()); }
        });

        List<Weapon> allWeapons = new ArrayList<>();
        allWeapons.addAll(ItemData.swordList);
        allWeapons.addAll(ItemData.staffList);
        allWeapons.addAll(ItemData.daggerList);
        allWeapons.addAll(ItemData.axeList);
        allWeapons.addAll(ItemData.spearList);
        allWeapons.addAll(ItemData.hammerList);

        // Tab click logic
        button0.setOnClickListener(v -> setActiveTab(button0, allWeapons));
        button1.setOnClickListener(v -> setActiveTab(button1, ItemData.armorList));
        button2.setOnClickListener(v -> setActiveTab(button2, ItemData.ringList));
        button3.setOnClickListener(v -> setActiveTab(button3, ItemData.swordList));
        button4.setOnClickListener(v -> setActiveTab(button4, ItemData.staffList));
        button5.setOnClickListener(v -> setActiveTab(button5, ItemData.daggerList));
        button6.setOnClickListener(v -> setActiveTab(button6, ItemData.axeList));
        button7.setOnClickListener(v -> setActiveTab(button7, ItemData.spearList));
        button8.setOnClickListener(v -> setActiveTab(button8, ItemData.hammerList));

        // Default: ALL
        setActiveTab(button0, allWeapons);
    }

    /** Highlights the clicked tab and loads its content */
    private void setActiveTab(Button active, List<?> list) {
        Button[] all = {button0, button1, button2, button3, button4, button5, button6, button7, button8};
        for (Button b : all) {
            if (b != null) b.setSelected(b == active);
        }
        showList(list);
    }

    /** Sets list & reapplies filter */
    private void showList(List<?> list) {
        currentFullList = list != null ? list : new ArrayList<>();
        applyFilter(searchEdit.getText() != null ? searchEdit.getText().toString() : "");
    }

    /** Filters by text */
    private void applyFilter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase();
        List<Object> filtered = new ArrayList<>();
        for (Object o : currentFullList) {
            String text = itemText(o).toLowerCase();
            if (q.isEmpty() || text.contains(q)) filtered.add(o);
        }

        ImageAdapter adapter = new ImageAdapter(filtered, (view, pos) -> {
            Object selected = filtered.get(pos);
            Intent intent = new Intent(Items.this, ItemDetail.class);
            intent.putExtra(EXTRA_ITEM, (Parcelable) selected);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    /** Gets item name */
    private String itemText(Object o) {
        if (o instanceof Weapon) return ((Weapon) o).getName();
        if (o instanceof Armor)  return ((Armor) o).getName();
        if (o instanceof Ring)   return ((Ring) o).getName();
        return String.valueOf(o);
    }

    private void applySystemInsets(View root, HorizontalScrollView tabs, EditText search, RecyclerView rv) {
        // Capture base paddings ONCE
        final int baseTabsL = tabs.getPaddingLeft();
        final int baseTabsT = tabs.getPaddingTop();
        final int baseTabsR = tabs.getPaddingRight();
        final int baseTabsB = tabs.getPaddingBottom();

        final int baseSearchL = search.getPaddingLeft();
        final int baseSearchT = search.getPaddingTop();
        final int baseSearchR = search.getPaddingRight();
        final int baseSearchB = search.getPaddingBottom();

        final int baseRvL = rv.getPaddingLeft();
        final int baseRvT = rv.getPaddingTop();
        final int baseRvR = rv.getPaddingRight();
        final int baseRvB = rv.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Give status-bar inset to tabs (fixed from base), nav-bar inset to the grid (fixed from base)
            tabs.setPadding(baseTabsL, baseTabsT + bars.top, baseTabsR, baseTabsB);
            search.setPadding(baseSearchL, baseSearchT, baseSearchR, baseSearchB);
            rv.setPadding(baseRvL, baseRvT, baseRvR, baseRvB + bars.bottom);
            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
