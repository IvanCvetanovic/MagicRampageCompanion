package xyz.magicrampagecompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AchievementsPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AchievementsAdapter adapter;
    private List<Achievement> achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_page);
        recyclerView = findViewById(R.id.AchievementsRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize achievements list
        achievements = new ArrayList<>();

        // Populate achievements list
        populateAchievements();

        // Initialize adapter
        adapter = new AchievementsAdapter(achievements);
        recyclerView.setAdapter(adapter);
    }

    private void populateAchievements() {

        achievements.add(new Achievement("Armed and Dangerous", "Get your first weapon.", Arrays.asList()));
        achievements.add(new Achievement("Zombie Killer", "Kill a zombie.", Arrays.asList()));
        achievements.add(new Achievement("Head shot!", "Hit an enemy with a head shot.", Arrays.asList("16000")));
        achievements.add(new Achievement("There and Back again", "Revive after death.", Arrays.asList()));
        achievements.add(new Achievement("Ecstasy of Gold", "Buy more gold for your character.", Arrays.asList("", "", "Throat Cutter")));
        achievements.add(new Achievement("Save that Dude!", "Keep That Dude from being attacked in Chapter 1 Dungeon level 2.", Arrays.asList("32000", "", "", "+1")));
        achievements.add(new Achievement("Mr. MacGyver", "Access the secret area in Chapter 1 Dungeon Bonus level 3.", Arrays.asList()));
        achievements.add(new Achievement("The Bone Collector", "Defeat a skeleton.", Arrays.asList("16000")));
        achievements.add(new Achievement("Class consciousness", "Learn and perform the special skill of your class in Chapter 1 Dungeon level 6.", Arrays.asList("32000")));
        achievements.add(new Achievement("Spider killer", "Kill a spider.", Arrays.asList("16000")));
        achievements.add(new Achievement("Keep That Dude alive!", "Help That Dude in Chapter 1 Dungeo level 8 and keep him alive.", Arrays.asList("48000", "", "", "+1")));
        achievements.add(new Achievement("That Dude's story", "Listen to what That Dude has to say in CHapter 1 Dungeon level 8 (if he survived Chapter1 Dungeon level 2).", Arrays.asList("32000", "", "", "+1")));
        achievements.add(new Achievement("Flawless victory!", "Get a star be replaying a level without taking any damage.", Arrays.asList("40000")));
        achievements.add(new Achievement("Demon slayer", "Beat the Boss in Chapter 1.", Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement("Pimp my character", "Use the Character Editor to change your avatar's head.", Arrays.asList("", "", "High Cleric Tunic")));
        achievements.add(new Achievement("More storage", "Buy the inventory expansion chest in the Shop.", Arrays.asList("", "", "Dagger of Sand")));
        achievements.add(new Achievement("Even more storage!", "Buy the second expansion chest in the shop.", Arrays.asList("", "", "Dark Dagger+")));
        achievements.add(new Achievement("In your honor, have a toast!", "Buy a drink for another player in the Tavern.", Arrays.asList("", "750")));
        achievements.add(new Achievement("Dirty-faced Rookie", "Finish a Solo Dungeon in the Competitive Mode.", Arrays.asList("20000")));
        achievements.add(new Achievement("Bring it on!", "Start a match against another player in the Competitive Mode.", Arrays.asList("32000")));
        achievements.add(new Achievement("I came, I saw, I conquered", "Win a match against another player in the Competitive Mode.", Arrays.asList("80000")));
        achievements.add(new Achievement("It's the taking part that counts...", "Don't ever try to lose a match against another player, but if you do, this achievement will be unlocked.", Arrays.asList()));
        achievements.add(new Achievement("Challenge Accepted", "Finish a Challenge from the Weekly Challenge Portal.", Arrays.asList("200000")));
        achievements.add(new Achievement("It's Amazing!", "Beat the Amazing Score Goal in the weekly Dungeons. You need to beat Ordinary and Super goals first.", Arrays.asList("", "1500")));
        achievements.add(new Achievement("Friends will be friends", "Add a friend in the Competitive Mode.", Arrays.asList("120000")));
        achievements.add(new Achievement("Friendly fire", "Challenge a friend in the Competitive Mode.", Arrays.asList("200000")));
        achievements.add(new Achievement("R for Revenge", "Win a revenge match in the Competitive Mode.", Arrays.asList("", "", "Obsidian Spear")));
        achievements.add(new Achievement("50 kills", "Kill 50 enemies in Survival mode.", Arrays.asList("64000")));
        achievements.add(new Achievement("100 kills", "Kill 100 enemies in Survival Mode.", Arrays.asList("160000")));
        achievements.add(new Achievement("Unlock all survival levels", "Unlock every survival level by reaching all minimum scores.", Arrays.asList("120000", "", "", "+1")));
        achievements.add(new Achievement("Get in the Ring", "Access the secret area in Chapter 2 Dungeon Bonus level 1.", Arrays.asList()));
        achievements.add(new Achievement("The lost Flip-Flop", "Find the lost Flip-Flop.", Arrays.asList()));
        achievements.add(new Achievement("The Untold Story", "Let that Dude tell you The Untold Story in Chapter 2 Dungeon level 4.", Arrays.asList("40000", "", "", "+1")));
        achievements.add(new Achievement("That Dude must live!", "Keep That Dude alive until the end of the level.", Arrays.asList("64000", "", "", "+1")));
        achievements.add(new Achievement("I could beat Ghouls 'N Ghosts", "Get a star on all first 15 levels.", Arrays.asList("", "", "Improved Jumpman Suit")));
        achievements.add(new Achievement("Changing in the air", "Get to the top of the highest point in Chapter 2 Dungeon level 6.", Arrays.asList()));
        achievements.add(new Achievement("Runeforge", "Upgrade an item.", Arrays.asList("32000")));
        achievements.add(new Achievement("Ride the Wind", "Reach the last chest in Chapter 2 Dungeon level 7.", Arrays.asList("56000")));
        achievements.add(new Achievement("Ghostbusters", "Defeat a ghost in Chapter 2 Dungeon level 7.", Arrays.asList("40000")));
        achievements.add(new Achievement("Why so curious?", "Access the locked area in Chapter 2 Dungeon bonus level 7.", Arrays.asList()));
        achievements.add(new Achievement("Loot from the future", "Find the least one of the two hidden chests in Chapter 1 Dungeon level 3.", Arrays.asList()));
        achievements.add(new Achievement("The Untold Story 2", "Hear what The Captain has to say about Portals and giant winged creatures in Chapter 2 Dungeon level 9.", Arrays.asList("40000")));
        achievements.add(new Achievement("Evil thing in spider form", "Defeat the giant spider that lives in the swamp.", Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement("Bank for your buck", "Deposit an item in the bank.", Arrays.asList("Banker's Coat")));
        achievements.add(new Achievement("I did it intentionally, to...", "Find The Lost Hammer.", Arrays.asList()));
        achievements.add(new Achievement("That Dude still lives", "Meet That Dude in Chapter 3 Dungeon level 5 (if he survived Chapter 2 Dungeon level 4.", Arrays.asList()));
        achievements.add(new Achievement("Breaker of Chains", "Release The Wizard of the Oracle in Chapter 3 Dungeon level 9.", Arrays.asList()));
        achievements.add(new Achievement("I saw a giant", "Kill a giant.", Arrays.asList("40000")));
        achievements.add(new Achievement("Witch-hunt", "Defeat The Warlock Boss.", Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement("Dragonslayer", "Defeat the Great Red Dragon.", Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement("Skilled but dirty-faced", "Learn three skills from the Skill Tree.", Arrays.asList("", "750")));
        achievements.add(new Achievement("The super skill", "Learn one of the Super Skills from the Skill Tree..", Arrays.asList("", "1050")));
        achievements.add(new Achievement("Did the whole thing!", "Complete 101% of the game. Find all secret areas and diamonds.", Arrays.asList("", "", "Spellmaster's Ring", "+1")));
        achievements.add(new Achievement("200 kills", "Kill 200 enemies in Survival Mode.", Arrays.asList("", "", "Dragon Orb Staff")));
        achievements.add(new Achievement("300 kills", "Kil 300 enemies in Survival Mode.", Arrays.asList("", "", "Warlock's Ring", "+1")));
        achievements.add(new Achievement("100 kills? WTF!", "Kill 100 enemies in Survival Mode.", Arrays.asList("", "", "Sword of 100 Lies")));
        achievements.add(new Achievement("Over 100% complete!", "Complete 101% of the game. Find all secret areas of the game, and reply Chapters 1, 2 and 3 without taking any damage.", Arrays.asList("", "", "Hippie Dragon Staff", "+1")));
        achievements.add(new Achievement("The Truth Is Out There", "Travel through the Portal and find out what is beyond.", Arrays.asList()));
        achievements.add(new Achievement("Chapter 4 is too difficult!", "Complete 102% of the game. Complete all Dungeons, find all secret areas and replay all dungeons up to the end of Chapter 4 without taking any damage.", Arrays.asList("", "", "Special Dragon Vest", "+1")));
        achievements.add(new Achievement("May the Magic be with you", "Find the lost saber.", Arrays.asList()));
        achievements.add(new Achievement("Breaker of Chains 2", "Release the Moon-Leeper in Chapter 4 Dungeon level 9.", Arrays.asList()));
        achievements.add(new Achievement("Bound together", "Join the King after defeating the Warlock.", Arrays.asList("", "", "", "+1")));
        achievements.add(new Achievement("The Champion Slayer", "Beat any champion in the Hall of Champions.", Arrays.asList("", "", "Ring of Light")));
        achievements.add(new Achievement("Bronze Beast", "Raise your Rank up to Bronze in the Competitive Mode.", Arrays.asList("", "450")));
        achievements.add(new Achievement("Silver Soldier", "Raise your Rank up to Gold in the Competituve Mode.", Arrays.asList("", "750", "", "+1")));
        achievements.add(new Achievement("Going Gold!", "Raise your Rank up to Gold in the Competitive Mode.", Arrays.asList("", "900", "", "+1")));
        achievements.add(new Achievement("Pure Platinum!", "Raise your Rank up to Platinum in the Competitive Mode.", Arrays.asList("", "1800", "", "+1")));
        achievements.add(new Achievement("Crazy Diamond!", "Raise your Rank up to Diamond in the Competitive Mode. Shine on!", Arrays.asList("", "3000", "", "+1")));
        achievements.add(new Achievement("Sharp Dressed Minion", "Change your minion's look using a custom skin. You can purchase new skins in the Prize Room.", Arrays.asList("", "", "Necromorphicon")));
        achievements.add(new Achievement("Impossibly Huge", "Buy the third expansion chest.", Arrays.asList("", "3000")));
        achievements.add(new Achievement("It's time!", "Collect the 420 coins that float in the air at 3-6. Do it during a specific time of day.", Arrays.asList("", "", "Hippie Dragon Staff+")));
        achievements.add(new Achievement("Bullseye Wizard", "Score 990,000 or higher in the Darts game inside the Tavern.", Arrays.asList("", "", "Poisoned Dart")));
        achievements.add(new Achievement("Settle a score", "Score 35,000 or higher in the Salesman's Smackdown game inside the Tavern.", Arrays.asList("", "750")));
        achievements.add(new Achievement("Veteran Rampager", "Be in top 500 list of best ranked players. You can see your current position in the ranking section of your inventory.", Arrays.asList("", "1800")));
        achievements.add(new Achievement("True hero", "Be in top 100 list of best ranked players. You can see your current position in the ranking section of your inventory.", Arrays.asList("", "2400")));
        achievements.add(new Achievement("We are the champions!", "Join the top 15 players featured in the Hall of Champions.", Arrays.asList("", "3600")));
        achievements.add(new Achievement("The number one!", "Get to the top of the Ranking leaderboard.", Arrays.asList("", "6000")));
        achievements.add(new Achievement("The End of the World", "Get to the surface for the first time in Dungeon -1.", Arrays.asList("", "", "Toxic Axe")));
        achievements.add(new Achievement("A story to be told", "Listen to what the Dude has to say at 5-3 prologue about the turbulent portal crossing.", Arrays.asList("", "21000")));
        achievements.add(new Achievement("The Untold Story 3", "Listen to what the King ha to say in his lab at 5-3 prologue.", Arrays.asList("", "", "Moonlit Razor")));
        achievements.add(new Achievement("The ninth circle", "Get to the frozen underground chambers of the planet in Dungeon 5-4.", Arrays.asList("", "", "Winter WItch Armor")));
        achievements.add(new Achievement("The darkest side of the Moon", "Watch the bonus scene after finishing Dungeon 5-4 with all three diamonds.", Arrays.asList("", "", "Path of Exile", "+1")));
    }
}