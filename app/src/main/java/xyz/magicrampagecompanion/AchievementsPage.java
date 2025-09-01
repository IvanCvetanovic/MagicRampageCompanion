package xyz.magicrampagecompanion;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementsPage extends AppCompatActivity {

    private List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
EdgeToEdge.enable(this);
        setContentView(R.layout.activity_achievements_page);
        RecyclerView recyclerView = findViewById(R.id.achievementsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize achievements list
        achievements = new ArrayList<>();

        // Populate achievements list
        populateAchievements();

        // Initialize adapter
        AchievementsAdapter adapter = new AchievementsAdapter(achievements);
        recyclerView.setAdapter(adapter);
    }


    private void populateAchievements() {

        achievements.add(new Achievement(getString(R.string.vase_breaker), getString(R.string.vase_breaker_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.save_that_dude), getString(R.string.save_that_dude_desc), Arrays.asList("8000/32000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.keep_that_dude_alive), getString(R.string.keep_that_dude_alive_desc), Arrays.asList("12000/48000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.armed_and_dangerous), getString(R.string.armed_and_dangerous_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.head_shot), getString(R.string.head_shot_desc), Arrays.asList("4000/16000")));
        achievements.add(new Achievement(getString(R.string.zombie_killer), getString(R.string.zombie_killer_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.spider_killer), getString(R.string.spider_killer_desc), Arrays.asList("4000/16000")));
        achievements.add(new Achievement(getString(R.string.bone_collector), getString(R.string.bone_collector_desc), Arrays.asList("4000/16000")));
        achievements.add(new Achievement(getString(R.string.mr_macgyver), getString(R.string.mr_macgyver_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.there_and_back), getString(R.string.there_and_back_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.know_your_enemy), getString(R.string.know_your_enemy_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.complete_101), getString(R.string.complete_101_desc), Arrays.asList("", "", getString(R.string.hippie_dragon_staff), "+1")));
        achievements.add(new Achievement(getString(R.string.complete_100), getString(R.string.complete_100_desc), Arrays.asList("", "", getString(R.string.spellmasters_ring), "+1")));
        achievements.add(new Achievement(getString(R.string.flawless_victory), getString(R.string.flawless_victory_desc), Arrays.asList("2500/10000")));
        achievements.add(new Achievement(getString(R.string.ecstasy_of_gold), getString(R.string.ecstasy_of_gold_desc), Arrays.asList("", "", getString(R.string.throat_cutter))));
        achievements.add(new Achievement(getString(R.string.demon_slayer), getString(R.string.demon_slayer_desc), Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.get_in_the_ring), getString(R.string.get_in_the_ring_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.lost_flipflop), getString(R.string.lost_flipflop_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.the_untold_story), getString(R.string.the_untold_story_desc), Arrays.asList("10000/40000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.that_dude_must_live), getString(R.string.that_dude_must_live_desc), Arrays.asList("16000/64000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.could_beat_gng), getString(R.string.could_beat_gng_desc), Arrays.asList("", "", getString(R.string.improved_jumpman_suit))));
        achievements.add(new Achievement(getString(R.string.changing_in_the_air), getString(R.string.changing_in_the_air_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.runeforge), getString(R.string.runeforge_desc), Arrays.asList("8000/32000")));
        achievements.add(new Achievement(getString(R.string.ride_the_wind), getString(R.string.ride_the_wind_desc), Arrays.asList("14000/56000")));
        achievements.add(new Achievement(getString(R.string.ghostbusters), getString(R.string.ghostbusters_desc), Arrays.asList("10000/40000")));
        achievements.add(new Achievement(getString(R.string.why_so_curious), getString(R.string.why_so_curious_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.loot_from_the_future), getString(R.string.loot_from_the_future_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.the_untold_story_2), getString(R.string.the_untold_story_2_desc), Arrays.asList("10000/40000")));
        achievements.add(new Achievement(getString(R.string.kill_the_spider), getString(R.string.kill_the_spider_desc), Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.lost_hammer), getString(R.string.lost_hammer_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.dude_still_lives), getString(R.string.dude_still_lives_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.dudes_story), getString(R.string.dudes_story_desc), Arrays.asList("8000/32000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.breaker_of_chains), getString(R.string.breaker_of_chains_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.saw_a_giant), getString(R.string.saw_a_giant_desc), Arrays.asList("10000/40000")));
        achievements.add(new Achievement(getString(R.string.witch_hunt), getString(R.string.witch_hunt_desc), Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.post_credits), getString(R.string.post_credits_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.survival_50), getString(R.string.survival_50_desc), Arrays.asList("16000/64000")));
        achievements.add(new Achievement(getString(R.string.survival_100), getString(R.string.survival_100_desc), Arrays.asList("40000/160000")));
        achievements.add(new Achievement(getString(R.string.survival_200), getString(R.string.survival_200_desc), Arrays.asList("", "", getString(R.string.dragon_orb_staff))));
        achievements.add(new Achievement(getString(R.string.survival_300), getString(R.string.survival_300_desc), Arrays.asList("", "", getString(R.string.warlocks_ring), "+1")));
        achievements.add(new Achievement(getString(R.string.survival_1000), getString(R.string.survival_1000_desc), Arrays.asList("", "", getString(R.string.sword_of_1000_lies))));
        achievements.add(new Achievement(getString(R.string.pimp_my_char), getString(R.string.pimp_my_char_desc), Arrays.asList("", "", getString(R.string.high_cleric_tunic))));
        achievements.add(new Achievement(getString(R.string.bag_expanded), getString(R.string.bag_expanded_desc), Arrays.asList("", "", getString(R.string.dagger_of_sand))));
        achievements.add(new Achievement(getString(R.string.may_the_magic), getString(R.string.may_the_magic_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.truth_out_there), getString(R.string.truth_out_there_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.complete_102), getString(R.string.complete_102_desc), Arrays.asList("", "", getString(R.string.special_dragon_vest), "+1")));
        achievements.add(new Achievement(getString(R.string.class_consciousness), getString(R.string.class_consciousness_desc), Arrays.asList("8000/32000")));
        achievements.add(new Achievement(getString(R.string.unlock_all_survival), getString(R.string.unlock_all_survival_desc), Arrays.asList("30000/120000", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.dragonslayer), getString(R.string.dragonslayer_desc), Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.breaker_of_chains_2), getString(R.string.breaker_of_chains_2_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.bound_together), getString(R.string.bound_together_desc), Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement(getString(R.string.bank_for_your_buck), getString(R.string.bank_for_your_buck_desc), Arrays.asList(getString(R.string.bankers_coat))));
        achievements.add(new Achievement(getString(R.string.dirty_faced_rookie), getString(R.string.dirty_faced_rookie_desc), Arrays.asList("5000/20000")));
        achievements.add(new Achievement(getString(R.string.bring_it_on), getString(R.string.bring_it_on_desc), Arrays.asList("8000/32000")));
        achievements.add(new Achievement(getString(R.string.veni_vidi_vici), getString(R.string.veni_vidi_vici_desc), Arrays.asList("20000/80000")));
        achievements.add(new Achievement(getString(R.string.taking_part_counts), getString(R.string.taking_part_counts_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.add_friend), getString(R.string.add_friend_desc), Arrays.asList("30000/120000")));
        achievements.add(new Achievement(getString(R.string.challenge_friend), getString(R.string.challenge_friend_desc), Arrays.asList("50000/200000")));
        achievements.add(new Achievement(getString(R.string.r_for_revenge), getString(R.string.r_for_revenge_desc), Arrays.asList("", "", getString(R.string.obsidian_spear))));
        achievements.add(new Achievement(getString(R.string.bronze_rank_achievement), getString(R.string.bronze_rank_achievement_desc), Arrays.asList("", "150/450")));
        achievements.add(new Achievement(getString(R.string.silver_rank_achievement), getString(R.string.silver_rank_achievement_desc), Arrays.asList("", "250/750", "", "+1")));
        achievements.add(new Achievement(getString(R.string.gold_rank_achievement), getString(R.string.gold_rank_achievement_desc), Arrays.asList("", "300/900", "", "+1")));
        achievements.add(new Achievement(getString(R.string.platinum_rank_achievement), getString(R.string.platinum_rank_achievement_desc), Arrays.asList("", "600/1800", "", "+1")));
        achievements.add(new Achievement(getString(R.string.diamond_rank_achievement), getString(R.string.diamond_rank_achievement_desc), Arrays.asList("", "1000/3000", "", "+1")));
        achievements.add(new Achievement(getString(R.string.champion_slayer), getString(R.string.champion_slayer_desc), Arrays.asList("", "", getString(R.string.ring_of_light))));
        achievements.add(new Achievement(getString(R.string.ranked_top_1), getString(R.string.ranked_top_1_desc), Arrays.asList("", "2000/6000")));
        achievements.add(new Achievement(getString(R.string.ranked_top_15), getString(R.string.ranked_top_15_desc), Arrays.asList("", "1200/3600")));
        achievements.add(new Achievement(getString(R.string.its_time), getString(R.string.its_time_desc), Arrays.asList("", "", getString(R.string.hippie_dragon_staff_plus))));
        achievements.add(new Achievement(getString(R.string.bag_expanded_2), getString(R.string.bag_expanded_2_desc), Arrays.asList("", "", getString(R.string.dark_dagger_plus))));
        achievements.add(new Achievement(getString(R.string.ranked_top_100), getString(R.string.ranked_top_100_desc), Arrays.asList("", "800/2400")));
        achievements.add(new Achievement(getString(R.string.ranked_top_500), getString(R.string.ranked_top_500_desc), Arrays.asList("", "600/1800")));
        achievements.add(new Achievement(getString(R.string.challenge_accepted), getString(R.string.challenge_accepted_desc), Arrays.asList("12500/50000")));
        achievements.add(new Achievement(getString(R.string.salesman_payback), getString(R.string.salesman_payback_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.time_patrollers_time), getString(R.string.time_patrollers_time_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.kingslayer_time), getString(R.string.kingslayer_time_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.pixelated_arsenal), getString(R.string.pixelated_arsenal_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.the_end_of_the_world), getString(R.string.the_end_of_the_world_desc), Arrays.asList("", "", getString(R.string.toxic_axe))));
        achievements.add(new Achievement(getString(R.string.bag_expanded_3), getString(R.string.bag_expanded_3_desc), Arrays.asList("", "1000/3000")));
        achievements.add(new Achievement(getString(R.string.its_amazing), getString(R.string.its_amazing_desc), Arrays.asList("", "500/1500")));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_completed_15), getString(R.string.weekly_dungeons_completed_15_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_completed_30), getString(R.string.weekly_dungeons_completed_30_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_completed_50), getString(R.string.weekly_dungeons_completed_50_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_star_30), getString(R.string.weekly_dungeons_star_30_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_star_50), getString(R.string.weekly_dungeons_star_50_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_time_30), getString(R.string.weekly_dungeons_time_30_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.weekly_dungeons_time_50), getString(R.string.weekly_dungeons_time_50_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.story_to_be_told), getString(R.string.story_to_be_told_desc), Arrays.asList("", "500/1500")));
        achievements.add(new Achievement(getString(R.string.the_untold_story_3), getString(R.string.the_untold_story_3_desc), Arrays.asList("", "", getString(R.string.moonlit_razor))));
        achievements.add(new Achievement(getString(R.string.skilled_dirty_faced), getString(R.string.skilled_dirty_faced_desc), Arrays.asList("", "250/750")));
        achievements.add(new Achievement(getString(R.string.super_skill), getString(R.string.super_skill_desc), Arrays.asList("", "350/1050")));
        achievements.add(new Achievement(getString(R.string.complete_103), getString(R.string.complete_103_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.ninth_circle), getString(R.string.ninth_circle_desc), Arrays.asList("", "", getString(R.string.winter_witch_armor))));
        achievements.add(new Achievement(getString(R.string.the_darkest_side), getString(R.string.the_darkest_side_desc), Arrays.asList("", "", getString(R.string.path_of_exile), "+1")));
        achievements.add(new Achievement(getString(R.string.sharp_dressed_minion), getString(R.string.sharp_dressed_minion_desc), Arrays.asList("", "", getString(R.string.necromorphicon))));
        achievements.add(new Achievement(getString(R.string.dirty_faced_souls), getString(R.string.dirty_faced_souls_desc), Arrays.asList("", getString(R.string.iron_dagger_plus), "", "+1")));
        achievements.add(new Achievement(getString(R.string.time_patroller), getString(R.string.time_patroller_desc), Arrays.asList("", "", getString(R.string.time_patroller_suit), "+1")));
        achievements.add(new Achievement(getString(R.string.the_king), getString(R.string.the_king_desc), Arrays.asList("", "", getString(R.string.kings_spectre_tunic), "+1")));
        achievements.add(new Achievement(getString(R.string.bound_together_plus), getString(R.string.bound_together_plus_desc), Arrays.asList("", "500/1500", "", "+1")));
        achievements.add(new Achievement(getString(R.string.witch_hunt_plus), getString(R.string.witch_hunt_plus_desc), Arrays.asList("", "500/1500", "", "+1")));
        achievements.add(new Achievement(getString(R.string.sinister_riddle), getString(R.string.sinister_riddle_desc), Arrays.asList()));
        achievements.add(new Achievement(getString(R.string.have_a_toast), getString(R.string.have_a_toast_desc), Arrays.asList("", "250/750")));
        achievements.add(new Achievement(getString(R.string.bullseye_wizard), getString(R.string.bullseye_wizard_desc), Arrays.asList("", "", getString(R.string.poisoned_dart))));
        achievements.add(new Achievement(getString(R.string.settle_a_score), getString(R.string.settle_a_score_desc), Arrays.asList("", "250/750")));
        achievements.add(new Achievement(getString(R.string.meet_me_outside), getString(R.string.meet_me_outside_desc), Arrays.asList("", "300/900")));
        achievements.add(new Achievement(getString(R.string.duels_of_destiny), getString(R.string.duels_of_destiny_desc), Arrays.asList("", "400/1200")));
        achievements.add(new Achievement(getString(R.string.taste_of_victory), getString(R.string.taste_of_victory_desc), Arrays.asList("", "400/1200")));
        achievements.add(new Achievement(getString(R.string.bronze_duel_rank_achievement), getString(R.string.bronze_duel_rank_achievement_desc), Arrays.asList("", "200/600", "", "+1")));
        achievements.add(new Achievement(getString(R.string.silver_duel_rank_achievement), getString(R.string.silver_duel_rank_achievement_desc), Arrays.asList("", "400/1200", "", "+1")));
        achievements.add(new Achievement(getString(R.string.gold_duel_rank_achievement), getString(R.string.gold_duel_rank_achievement_desc), Arrays.asList("", "800/2400", "", "+1")));
        achievements.add(new Achievement(getString(R.string.platinum_duel_rank_achievement), getString(R.string.platinum_duel_rank_achievement_desc), Arrays.asList("", "1600/4800", "", "+1")));
        achievements.add(new Achievement(getString(R.string.diamond_duel_rank_achievement), getString(R.string.diamond_duel_rank_achievement_desc), Arrays.asList("", "1600/4800", "", "+1")));
        achievements.add(new Achievement(getString(R.string.first_steps_into_destiny), getString(R.string.first_steps_into_destiny_desc), Arrays.asList("", "", getString(R.string.gatekeepers_bionic_arm))));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

}