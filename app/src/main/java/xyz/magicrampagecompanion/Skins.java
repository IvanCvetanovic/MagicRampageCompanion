package xyz.magicrampagecompanion;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Skins screen:
 * - Vertical RecyclerView of "sections" (one per class)
 * - Each section has a title and a horizontal RecyclerView of image+caption items
 * - Data comes from ItemData.*Skins lists
 * - Edge-to-edge with insets handling
 */
public class Skins extends AppCompatActivity {

    private RecyclerView rvSections;

    // ---- Tunables (easy to tweak in one place) ----
    private static final int IMAGE_DP = 160;                 // image height in each card
    private static final int CAPTION_MAX_LINES = 3;          // lines before truncation
    private static final int CAPTION_TEXT_MAX_SP = 16;       // pairs with auto-size (12..16sp)
    private static final float CAPTION_LINE_HEIGHT_MULT = 1.12f; // slight line-height
    private static final int CAPTION_PAD_TOP_DP = 8;
    private static final int CAPTION_PAD_BOTTOM_DP = 6;      // tightened from 10
    private static final int CARD_BOTTOM_MARGIN_DP = 2;      // space between sections
    private static final int CARD_BOTTOM_PADDING_DP = 6;     // inside the rounded card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_skins);

        rvSections = findViewById(R.id.rvSections);
        rvSections.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvSections.setHasFixedSize(true);

        // Build sections from ItemData
        List<Section> sections = buildSectionsFromItemData();

        SectionAdapter adapter = new SectionAdapter(sections);
        rvSections.setAdapter(adapter);
        rvSections.addItemDecoration(new VerticalSpaceDecoration(dp(16)));

        applySystemInsets(findViewById(R.id.skinsRoot), rvSections);
    }

    // -------- Build data from ItemData --------
    private List<Section> buildSectionsFromItemData() {
        // Titles from strings.xml (localizable)
        List<String> titles = Arrays.asList(
                getString(R.string.class_ranger),
                getString(R.string.class_priest),
                getString(R.string.class_warlock),
                getString(R.string.class_black_mage),
                getString(R.string.class_rogue),
                getString(R.string.class_thief),
                getString(R.string.class_warrior),
                getString(R.string.class_mage),
                getString(R.string.class_druid),
                getString(R.string.class_paladin),
                getString(R.string.class_high_mage),
                getString(R.string.class_elite_warrior),
                getString(R.string.class_witchdoctor)
        );

        // Lists in the same order as titles
        List<List<SkinItem>> lists = Arrays.asList(
                ItemData.rangerSkins,
                ItemData.priestSkins,
                ItemData.warlockSkins,
                ItemData.blackMageSkins,
                ItemData.rogueSkins,
                ItemData.thiefSkins,
                ItemData.warriorSkins,
                ItemData.mageSkins,
                ItemData.druidSkins,
                ItemData.paladinSkins,
                ItemData.highMageSkins,
                ItemData.eliteWarriorSkins,
                ItemData.witchdoctorSkins
        );

        List<Section> sections = new ArrayList<>(titles.size());
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            List<SkinItem> src = lists.get(i);

            List<ImageItem> items = new ArrayList<>(src.size());
            for (SkinItem si : src) {
                items.add(new ImageItem(si.getImageResId(), si.getName()));
            }
            sections.add(new Section(title, items));
        }
        return sections;
    }

    // -------- Models --------
    static class ImageItem {
        @DrawableRes int imageResId;
        String caption;

        ImageItem(@DrawableRes int imageResId, String caption) {
            this.imageResId = imageResId;
            this.caption = caption;
        }
    }

    static class Section {
        String title;
        List<ImageItem> items;

        Section(String title, List<ImageItem> items) {
            this.title = title;
            this.items = items;
        }
    }

    // -------- Adapters --------
    /** Vertical adapter of sections: Title + horizontal RecyclerView */
    class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SecVH> {
        private final List<Section> data;

        SectionAdapter(List<Section> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public SecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Root vertical container
            LinearLayout container = new LinearLayout(parent.getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            container.setPadding(dp(0), dp(8), dp(0), dp(8));

            // Title
            TextView title = new TextView(parent.getContext());
            LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            title.setLayoutParams(titleLp);
            title.setTextSize(22);
            title.setTypeface(title.getTypeface(), android.graphics.Typeface.BOLD);
            title.setPadding(dp(4), dp(4), dp(4), dp(8));
            title.setAllCaps(false);
            title.setGravity(Gravity.START);

            // Card-style background for the horizontal list
            LinearLayout scrollerCard = new LinearLayout(parent.getContext());
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardLp.topMargin = dp(0);
            cardLp.bottomMargin = dp(CARD_BOTTOM_MARGIN_DP);
            scrollerCard.setLayoutParams(cardLp);
            scrollerCard.setOrientation(LinearLayout.VERTICAL);
            scrollerCard.setBackgroundResource(R.drawable.skins_scroller_bg);
            scrollerCard.setPadding(dp(8), dp(8), dp(8), dp(CARD_BOTTOM_PADDING_DP));
            scrollerCard.setClipToPadding(false);

            // Horizontal RecyclerView — height computed to fit image + 3 lines + padding
            RecyclerView horizontal = new RecyclerView(parent.getContext());
            LinearLayout.LayoutParams hrLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    computeRowHeightPx(parent) // <- dynamic height
            );
            horizontal.setLayoutParams(hrLp);
            horizontal.setClipToPadding(false);
            horizontal.setPadding(0, 0, 0, 0);
            horizontal.setOverScrollMode(View.OVER_SCROLL_NEVER);
            horizontal.setLayoutManager(new LinearLayoutManager(parent.getContext(), RecyclerView.HORIZONTAL, false));
            horizontal.addItemDecoration(new HorizontalSpaceDecoration(dp(12)));
            horizontal.setHasFixedSize(true);

            scrollerCard.addView(horizontal);
            container.addView(title);
            container.addView(scrollerCard);

            return new SecVH(container, title, horizontal);
        }

        @Override
        public void onBindViewHolder(@NonNull SecVH holder, int position) {
            Section section = data.get(position);
            holder.title.setText(section.title);
            holder.horizontal.setAdapter(new ImageAdapter(section.items));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class SecVH extends RecyclerView.ViewHolder {
            TextView title;
            RecyclerView horizontal;

            SecVH(@NonNull View itemView, TextView title, RecyclerView horizontal) {
                super(itemView);
                this.title = title;
                this.horizontal = horizontal;
            }
        }
    }

    /** Horizontal adapter of image+caption items */
    static class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImgVH> {
        private final List<ImageItem> data;

        ImageAdapter(List<ImageItem> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ImgVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Card column — fills the computed row height
            LinearLayout card = new LinearLayout(parent.getContext());
            RecyclerView.LayoutParams cardLp =
                    new RecyclerView.LayoutParams(dpStatic(parent, 160), ViewGroup.LayoutParams.MATCH_PARENT);
            card.setLayoutParams(cardLp);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.TOP);

            // Image (fixed height)
            ImageView iv = new ImageView(parent.getContext());
            LinearLayout.LayoutParams ivLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpStatic(parent, IMAGE_DP));
            iv.setLayoutParams(ivLp);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setAdjustViewBounds(true);

            // Caption (up to 3 lines + auto-shrink)
            TextView tv = new TextView(parent.getContext());
            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(tvLp);
            tv.setPadding(dpStatic(parent, 8), dpStatic(parent, CAPTION_PAD_TOP_DP),
                    dpStatic(parent, 8), dpStatic(parent, CAPTION_PAD_BOTTOM_DP));

            tv.setSingleLine(false);
            tv.setMaxLines(CAPTION_MAX_LINES);
            tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
            tv.setIncludeFontPadding(false); // removes built-in extra top/bottom font padding

            // Auto-size text for long names (12..16sp)
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    tv, 12, CAPTION_TEXT_MAX_SP, 1, TypedValue.COMPLEX_UNIT_SP
            );

            card.addView(iv);
            card.addView(tv);

            return new ImgVH(card, iv, tv);
        }

        @Override
        public void onBindViewHolder(@NonNull ImgVH holder, int position) {
            ImageItem item = data.get(position);
            holder.image.setImageResource(item.imageResId);
            holder.caption.setText(item.caption);
            ViewCompat.setTooltipText(holder.caption, item.caption); // full text on long-press/hover
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        static class ImgVH extends RecyclerView.ViewHolder {
            ImageView image;
            TextView caption;

            ImgVH(@NonNull View itemView, ImageView image, TextView caption) {
                super(itemView);
                this.image = image;
                this.caption = caption;
            }
        }
    }

    // -------- Spacing decorators --------
    static class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int spacePx;

        HorizontalSpaceDecoration(int spacePx) {
            this.spacePx = spacePx;
        }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            outRect.top = 0;
            outRect.bottom = 0;
            outRect.left = (position == 0) ? 0 : spacePx;
            outRect.right = 0;
        }
    }

    static class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int spacePx;

        VerticalSpaceDecoration(int spacePx) {
            this.spacePx = spacePx;
        }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = (position == 0) ? 0 : spacePx;
            outRect.bottom = 0;
        }
    }

    // -------- Edge-to-edge insets helper --------
    private void applySystemInsets(View root, View recycler) {
        final int baseRootLeft = root.getPaddingLeft();
        final int baseRootTop = root.getPaddingTop();
        final int baseRootRight = root.getPaddingRight();
        final int baseRootBottom = root.getPaddingBottom();

        final int baseRvLeft = recycler.getPaddingLeft();
        final int baseRvTop = recycler.getPaddingTop();
        final int baseRvRight = recycler.getPaddingRight();
        final int baseRvBottom = recycler.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            root.setPadding(baseRootLeft, baseRootTop + bars.top, baseRootRight, baseRootBottom);
            recycler.setPadding(baseRvLeft, baseRvTop, baseRvRight, baseRvBottom + bars.bottom);
            return insets;
        });

        ViewCompat.requestApplyInsets(root);
    }

    // -------- Height calculator --------
    private int computeRowHeightPx(ViewGroup parent) {
        final float density = parent.getResources().getDisplayMetrics().density;
        final float scaled = parent.getResources().getDisplayMetrics().scaledDensity;

        int imagePx = Math.round(IMAGE_DP * density);
        int captionPadPx = Math.round((CAPTION_PAD_TOP_DP + CAPTION_PAD_BOTTOM_DP) * density);

        // approximate line height from max text size
        float lineHeightPx = CAPTION_TEXT_MAX_SP * scaled * CAPTION_LINE_HEIGHT_MULT;

        return imagePx + captionPadPx + Math.round(lineHeightPx * CAPTION_MAX_LINES);
    }

    // -------- dp helpers --------
    private int dp(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().density));
    }

    private static int dpStatic(ViewGroup parent, int dp) {
        float density = parent.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }
}
