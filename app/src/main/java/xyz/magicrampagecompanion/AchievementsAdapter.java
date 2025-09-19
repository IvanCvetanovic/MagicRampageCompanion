package xyz.magicrampagecompanion;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder> {

    private final List<Achievement> achievements;

    public AchievementsAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.achievementNumberTextView.setText(String.valueOf(position + 1));
        holder.achievementNameTextView.setText(achievement.getName());
        holder.achievementDescriptionTextView.setText(achievement.getDescription());

        // --- Background per category (uses the shadowed/beveled drawable) ---
        switch (achievement.getCategory()) {
            case RAMPAGE:
                holder.cardBg.setBackgroundResource(R.drawable.ach_card_red);
                break;
            case NOT_RELEASED:
                holder.cardBg.setBackgroundResource(R.drawable.ach_card_olive); // ensure this exists
                break;
            case NORMAL:
            default:
                holder.cardBg.setBackgroundResource(R.drawable.ach_card_grey);
                break;
        }

        // --- Text color per category (yellow on Rampage, default on others) ---
        int textColorRes = (achievement.getCategory() == Achievement.AchievementCategory.RAMPAGE)
                ? R.color.yellow
                : R.color.card_text; // your default white
        int textColor = ContextCompat.getColor(holder.itemView.getContext(), textColorRes);
        holder.achievementNumberTextView.setTextColor(textColor);
        holder.achievementNameTextView.setTextColor(textColor);
        holder.achievementDescriptionTextView.setTextColor(textColor);
        holder.rewardsTextView.setTextColor(textColor);

        // --- Rewards with emoji, aligned & slightly larger (130%) ---
        List<String> rewards = achievement.getRewards();
        if (!rewards.isEmpty()) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            int size = (int) (holder.rewardsTextView.getLineHeight() * 1.3f);

            for (int i = 0; i < rewards.size(); i++) {
                String reward = rewards.get(i);
                if (reward == null || reward.isEmpty()) continue;

                int emojiRes = getEmojiResourceIdForReward(reward, i);
                Drawable d = ResourcesCompat.getDrawable(holder.itemView.getResources(), emojiRes, null);
                if (d != null) {
                    d.setBounds(0, 0, size, size);
                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                    SpannableString token = new SpannableString("  "); // 1 span char + a space
                    token.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    sb.append(token);
                    sb.append(reward).append("  ");
                }
            }
            holder.rewardsTextView.setIncludeFontPadding(false);
            holder.rewardsTextView.setText(sb);
        } else {
            holder.rewardsTextView.setText("");
        }
    }

    // Map rewards to emoji resources (by position)
    private int getEmojiResourceIdForReward(String reward, int position) {
        switch (position) {
            case 0: return R.drawable.coin;
            case 1: return R.drawable.token;
            case 2: return R.drawable.red_chest;
            case 3: return R.drawable.skill_point;
            default: return R.drawable.coin;
        }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View cardBg; // background layer we tint/swap
        TextView achievementNumberTextView;
        TextView achievementNameTextView;
        TextView achievementDescriptionTextView;
        TextView rewardsTextView;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.achievementCard);
            cardBg = itemView.findViewById(R.id.cardBg);
            achievementNumberTextView = itemView.findViewById(R.id.achievementNumberTextView);
            achievementNameTextView = itemView.findViewById(R.id.achievementNameTextView);
            achievementDescriptionTextView = itemView.findViewById(R.id.achievementDescriptionTextView);
            rewardsTextView = itemView.findViewById(R.id.rewardsTextView);
        }
    }
}
