package xyz.magicrampagecompanion.ui.items;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xyz.magicrampagecompanion.R;
import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.data.models.SkinItem;
import xyz.magicrampagecompanion.ui.common.BaseActivity;

public class Skins extends BaseActivity {

    private RecyclerView rvSections;

    // ---- Tunables ----
    private static final int IMAGE_DP              = 160;
    private static final int CAPTION_MAX_LINES     = 3;
    private static final int CAPTION_TEXT_MAX_SP   = 16;
    private static final float CAPTION_LINE_HEIGHT_MULT = 1.12f;
    private static final int CAPTION_PAD_TOP_DP    = 8;
    private static final int CAPTION_PAD_BOTTOM_DP = 6;
    private static final int CARD_BOTTOM_MARGIN_DP = 2;
    private static final int CARD_BOTTOM_PADDING_DP = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_skins);

        rvSections = findViewById(R.id.rvSections);
        rvSections.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rvSections.setHasFixedSize(true);

        List<Section> sections = buildSectionsFromItemData();
        SectionAdapter adapter = new SectionAdapter(sections);
        rvSections.setAdapter(adapter);
        rvSections.addItemDecoration(new VerticalSpaceDecoration(dp(16)));

        applySystemInsets(findViewById(R.id.skinsRoot), rvSections);
    }

    private List<Section> buildSectionsFromItemData() {
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
            List<ImageItem> items = new ArrayList<>();
            for (SkinItem si : lists.get(i)) {
                items.add(new ImageItem(si.getImageResId(), si.getName()));
            }
            sections.add(new Section(titles.get(i), items));
        }
        return sections;
    }

    // -------- Models --------
    static class ImageItem {
        @DrawableRes final int imageResId;
        final String caption;

        ImageItem(@DrawableRes int imageResId, String caption) {
            this.imageResId = imageResId;
            this.caption    = caption;
        }
    }

    static class Section {
        final String title;
        final List<ImageItem> items;

        Section(String title, List<ImageItem> items) {
            this.title = title;
            this.items = items;
        }
    }

    // -------- Section adapter --------
    class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SecVH> {
        private final List<Section> data;

        SectionAdapter(List<Section> data) { this.data = data; }

        @NonNull
        @Override
        public SecVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout container = new LinearLayout(parent.getContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            container.setPadding(dp(0), dp(8), dp(0), dp(8));

            TextView title = new TextView(parent.getContext());
            title.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            title.setTextSize(22);
            title.setTypeface(title.getTypeface(), Typeface.BOLD);
            title.setPadding(dp(4), dp(4), dp(4), dp(8));
            title.setAllCaps(false);
            title.setGravity(Gravity.START);

            LinearLayout scrollerCard = new LinearLayout(parent.getContext());
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardLp.topMargin    = dp(0);
            cardLp.bottomMargin = dp(CARD_BOTTOM_MARGIN_DP);
            scrollerCard.setLayoutParams(cardLp);
            scrollerCard.setOrientation(LinearLayout.VERTICAL);
            scrollerCard.setBackgroundResource(R.drawable.skins_scroller_bg);
            scrollerCard.setPadding(dp(8), dp(8), dp(8), dp(CARD_BOTTOM_PADDING_DP));
            scrollerCard.setClipToPadding(false);

            RecyclerView horizontal = new RecyclerView(parent.getContext());
            horizontal.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, computeRowHeightPx(parent)));
            horizontal.setClipToPadding(false);
            horizontal.setOverScrollMode(View.OVER_SCROLL_NEVER);
            horizontal.setLayoutManager(
                    new LinearLayoutManager(parent.getContext(), RecyclerView.HORIZONTAL, false));
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
            // Show skin count alongside class name
            holder.title.setText(section.title + "  (" + section.items.size() + ")");
            holder.horizontal.setAdapter(new ImageAdapter(section.items, Skins.this::playClick));
        }

        @Override
        public int getItemCount() { return data.size(); }

        class SecVH extends RecyclerView.ViewHolder {
            final TextView title;
            final RecyclerView horizontal;

            SecVH(@NonNull View itemView, TextView title, RecyclerView horizontal) {
                super(itemView);
                this.title      = title;
                this.horizontal = horizontal;
            }
        }
    }

    // -------- Image adapter --------
    static class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImgVH> {
        private final List<ImageItem> data;
        private final Runnable onTap;

        // Badge colors: letter-variant skins get an accent tint to signal they're alternates
        private static final int BADGE_COLOR_NORMAL  = 0xCC000000;
        private static final int BADGE_COLOR_VARIANT = 0xCC00796B; // teal

        ImageAdapter(List<ImageItem> data, Runnable onTap) {
            this.data  = data;
            this.onTap = onTap;
        }

        @NonNull
        @Override
        public ImgVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context ctx = parent.getContext();

            LinearLayout card = new LinearLayout(ctx);
            card.setLayoutParams(new RecyclerView.LayoutParams(
                    dpStatic(parent, 160), ViewGroup.LayoutParams.MATCH_PARENT));
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.TOP);

            // FrameLayout wraps image + badge overlay
            FrameLayout imgContainer = new FrameLayout(ctx);
            imgContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, dpStatic(parent, IMAGE_DP)));

            ImageView iv = new ImageView(ctx);
            iv.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setAdjustViewBounds(true);

            // Number badge — top-right corner of the image
            TextView badge = new TextView(ctx);
            badge.setTextColor(Color.WHITE);
            badge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            badge.setTypeface(badge.getTypeface(), Typeface.BOLD);
            badge.setPadding(dpStatic(parent, 4), dpStatic(parent, 2),
                             dpStatic(parent, 4), dpStatic(parent, 2));
            FrameLayout.LayoutParams badgeLp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP | Gravity.END);
            badgeLp.setMargins(0, dpStatic(parent, 4), dpStatic(parent, 4), 0);
            badge.setLayoutParams(badgeLp);
            GradientDrawable badgeBg = new GradientDrawable();
            badgeBg.setCornerRadius(dpStatic(parent, 6));
            badgeBg.setColor(BADGE_COLOR_NORMAL);
            badge.setBackground(badgeBg);

            // Caption
            TextView tv = new TextView(ctx);
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(dpStatic(parent, 8), dpStatic(parent, CAPTION_PAD_TOP_DP),
                          dpStatic(parent, 8), dpStatic(parent, CAPTION_PAD_BOTTOM_DP));
            tv.setSingleLine(false);
            tv.setMaxLines(CAPTION_MAX_LINES);
            tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
            tv.setIncludeFontPadding(false);
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    tv, 12, CAPTION_TEXT_MAX_SP, 1, TypedValue.COMPLEX_UNIT_SP);

            imgContainer.addView(iv);
            imgContainer.addView(badge);
            card.addView(imgContainer);
            card.addView(tv);

            return new ImgVH(card, iv, tv, badge);
        }

        @Override
        public void onBindViewHolder(@NonNull ImgVH holder, int position) {
            ImageItem item = data.get(position);
            Context ctx = holder.image.getContext();

            holder.image.setImageResource(item.imageResId);
            holder.caption.setText(item.caption);
            ViewCompat.setTooltipText(holder.caption, item.caption);

            String badgeText = extractBadge(ctx, item.imageResId);
            if (badgeText.isEmpty()) {
                holder.badge.setVisibility(View.GONE);
            } else {
                holder.badge.setVisibility(View.VISIBLE);
                holder.badge.setText(badgeText);
                // Teal badge for letter-variant skins (e.g. 14B, 17A, 12A)
                boolean isVariant = badgeText.matches(".*[A-Z]$");
                ((GradientDrawable) holder.badge.getBackground()).setColor(
                        isVariant ? BADGE_COLOR_VARIANT : BADGE_COLOR_NORMAL);
            }

            holder.itemView.setOnClickListener(v -> {
                if (onTap != null) onTap.run();
                showFullscreen(ctx, item);
            });
        }

        @Override
        public int getItemCount() { return data.size(); }

        // Derives the badge label from the drawable resource name.
        // "skin_mage17a"     → "17A"
        // "skin_mage14_b"    → "14B"
        // "skin_ranger1"     → "1"
        private static String extractBadge(Context ctx, int resId) {
            String name = ctx.getResources().getResourceEntryName(resId);
            Matcher m;
            // number immediately followed by a letter: 17a, 12a
            m = Pattern.compile("(\\d+)([a-z])$").matcher(name);
            if (m.find()) return m.group(1) + m.group(2).toUpperCase();
            // number + underscore + letter: 14_b
            m = Pattern.compile("(\\d+)_([a-z])$").matcher(name);
            if (m.find()) return m.group(1) + m.group(2).toUpperCase();
            // plain number
            m = Pattern.compile("(\\d+)$").matcher(name);
            if (m.find()) return m.group(1);
            return "";
        }

        private static void showFullscreen(Context ctx, ImageItem item) {
            Dialog dialog = new Dialog(ctx);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window win = dialog.getWindow();
            if (win != null) {
                win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                win.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                              ViewGroup.LayoutParams.MATCH_PARENT);
            }
            dialog.setCanceledOnTouchOutside(true);

            // Full-screen dim layer — tap anywhere outside the card to close
            FrameLayout root = new FrameLayout(ctx);
            root.setBackgroundColor(0xCC000000);
            root.setOnClickListener(v -> dialog.dismiss());

            // Centered card
            LinearLayout card = new LinearLayout(ctx);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER_HORIZONTAL);
            card.setClickable(true);
            card.setFocusable(true);
            card.setOnClickListener(v -> dialog.dismiss());
            int pad = dpCtx(ctx, 24);
            card.setPadding(pad, pad, pad, pad);
            card.setBackgroundResource(R.drawable.skins_scroller_bg);
            FrameLayout.LayoutParams cardLp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            int margin = dpCtx(ctx, 40);
            cardLp.setMargins(margin, margin, margin, margin);
            card.setLayoutParams(cardLp);

            DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
            int imgSize = (int) (Math.min(dm.widthPixels, dm.heightPixels) * 0.65f);

            ImageView iv = new ImageView(ctx);
            iv.setLayoutParams(new LinearLayout.LayoutParams(imgSize, imgSize));
            iv.setImageResource(item.imageResId);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setAdjustViewBounds(true);

            TextView tv = new TextView(ctx);
            tv.setText(item.caption);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tvLp.topMargin = dpCtx(ctx, 12);
            tv.setLayoutParams(tvLp);

            card.addView(iv);
            card.addView(tv);
            root.addView(card);

            dialog.setContentView(root);
            dialog.show();
        }

        private static int dpCtx(Context ctx, int dp) {
            return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
        }

        static class ImgVH extends RecyclerView.ViewHolder {
            final ImageView image;
            final TextView caption;
            final TextView badge;

            ImgVH(@NonNull View itemView, ImageView image, TextView caption, TextView badge) {
                super(itemView);
                this.image   = image;
                this.caption = caption;
                this.badge   = badge;
            }
        }
    }

    // -------- Spacing decorators --------
    static class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int spacePx;

        HorizontalSpaceDecoration(int spacePx) { this.spacePx = spacePx; }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);
            outRect.set(pos == 0 ? 0 : spacePx, 0, 0, 0);
        }
    }

    static class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int spacePx;

        VerticalSpaceDecoration(int spacePx) { this.spacePx = spacePx; }

        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);
            outRect.set(0, pos == 0 ? 0 : spacePx, 0, 0);
        }
    }

    // -------- Edge-to-edge insets --------
    private void applySystemInsets(View root, View recycler) {
        final int rl = root.getPaddingLeft(),    rt = root.getPaddingTop();
        final int rr = root.getPaddingRight(),   rb = root.getPaddingBottom();
        final int vl = recycler.getPaddingLeft(), vt = recycler.getPaddingTop();
        final int vr = recycler.getPaddingRight(), vb = recycler.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            root.setPadding(rl, rt + bars.top, rr, rb);
            recycler.setPadding(vl, vt, vr, vb + bars.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(root);
    }

    // -------- Height calculator --------
    private int computeRowHeightPx(ViewGroup parent) {
        final float density = parent.getResources().getDisplayMetrics().density;
        final float scaled  = parent.getResources().getDisplayMetrics().scaledDensity;
        int imagePx      = Math.round(IMAGE_DP * density);
        int captionPadPx = Math.round((CAPTION_PAD_TOP_DP + CAPTION_PAD_BOTTOM_DP) * density);
        float lineHeightPx = CAPTION_TEXT_MAX_SP * scaled * CAPTION_LINE_HEIGHT_MULT;
        return imagePx + captionPadPx + Math.round(lineHeightPx * CAPTION_MAX_LINES);
    }

    // -------- dp helpers --------
    private int dp(int dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static int dpStatic(ViewGroup parent, int dp) {
        return Math.round(dp * parent.getResources().getDisplayMetrics().density);
    }
}
