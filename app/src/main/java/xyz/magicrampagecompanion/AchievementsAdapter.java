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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder> {
    private List<Achievement> achievements;

    public AchievementsAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.achievementNumberTextView.setText(String.valueOf(position + 1));
        holder.achievementNameTextView.setText(achievement.getName());
        holder.achievementDescriptionTextView.setText(achievement.getDescription());

        List<String> rewards = achievement.getRewards();
        if (!rewards.isEmpty()) {
            // Create a SpannableStringBuilder to append rewards with emoji
            SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

            for (int i = 0; i < rewards.size(); i++) {
                String reward = rewards.get(i);
                // Skip processing if reward is empty
                if (!reward.isEmpty()) {
                    // Get the emoji resource ID for the reward
                    int emojiResourceId = getEmojiResourceIdForReward(reward, i);

                    // Create a SpannableString with the reward text and emoji
                    SpannableString spannable = new SpannableString(" ");
                    Drawable customEmojiDrawable = ResourcesCompat.getDrawable(holder.itemView.getResources(), emojiResourceId, null);
                    if (customEmojiDrawable != null) {
                        int desiredWidth = 100;
                        int desiredHeight = 100;
                        customEmojiDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
                        ImageSpan imageSpan = new ImageSpan(customEmojiDrawable, ImageSpan.ALIGN_BOTTOM);
                        // Set the emoji before the reward string
                        spannable.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    // Append the SpannableString to the SpannableStringBuilder
                    spannableBuilder.append(spannable);
                    // Add the reward text
                    spannableBuilder.append(reward + " ");
                }
            }

            // Set the SpannableStringBuilder to the rewardsTextView
            holder.rewardsTextView.setText(spannableBuilder);
        } else {
            holder.rewardsTextView.setText(""); // Clear the rewardsTextView if there are no rewards
        }
    }


    // Implement this method to map rewards to emoji resources
    private int getEmojiResourceIdForReward(String reward, int position) {
        // Check the position of the reward within the list
        switch (position) {
            case 0:
                // Return the coin emoji for the first reward
                return R.drawable.coin;
            case 1:
                // Return the second emoji for the second reward
                return R.drawable.token;
            case 2:
                // Return the third emoji for the third reward
                return R.drawable.red_chest;
            case 3:
                // Return the fourth emoji for the fourth reward
                return R.drawable.skill_point;
            default:
                // For any other position, return a default emoji
                return R.drawable.coin;
        }
    }


    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView achievementNumberTextView;
        TextView achievementNameTextView;
        TextView achievementDescriptionTextView;
        TextView rewardsTextView;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementNumberTextView = itemView.findViewById(R.id.achievementNumberTextView);
            achievementNameTextView = itemView.findViewById(R.id.achievementNameTextView);
            achievementDescriptionTextView = itemView.findViewById(R.id.achievementDescriptionTextView);
            rewardsTextView = itemView.findViewById(R.id.rewardsTextView);
        }
    }
}
