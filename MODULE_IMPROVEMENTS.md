# Module Improvement Plan (non–Level-Editor)

> Scan date: **2026-06-18**. Source-of-truth list of grounded improvement opportunities outside the
> Level Editor (which is feature-complete — see `LEVEL_EDITOR_PLAN.md`).
> Baseline: the app is well-built — modern SDK 37 / Java 17, 10 localized languages, consistent
> edge-to-edge insets, search in Items/Enemies/Achievements/ItemSelection, no `TODO`/`FIXME` markers.
> Items below are concrete and code-grounded; **[obs]** = directly observed, **[inf]** = reasonable inference to confirm.

## High value
1. **Enemy elemental weaknesses / resistances** (Enemies). **[obs]** `data/models/Enemy.java` has
   health/armor/damage/speed/jump/patrol/attack but **no element data**, yet the game is built on
   elemental combat and the app already ships an `Elements` enum (used in EquipmentTester). Add a
   per-enemy weakness/resistance and surface it in `EnemyDetail` (colored element chips like the
   existing stat cards). *The single biggest content gap for a build-planning companion.*
   Effort: **M–L** (needs sourcing the per-enemy weakness data + model field + detail UI). Risk: low (additive).

2. **Skins: search / class-jump** (`ui/items/Skins.java`). **[obs]** 13 class sections, each a
   horizontal scroller of many skins, with **no way to jump or filter** — finding one skin means
   scrolling. Add a class-chip jump row (or a name filter), matching the search affordance every
   other list screen has. Effort: **M**. Risk: low.

## Medium value
3. **Achievements progress tracking** (`ui/achievements/AchievementsPage.java`). **[obs]** ~120
   achievements, searchable + categorized, but read-only. Let users tap to mark one done (persist
   in SharedPreferences) and show an "X / N completed" header. Effort: **M**. Risk: low.

4. **Localize the Level Editor UI** to the 10 shipped languages. **[obs]** All editor strings
   (save/export dialogs, palette, validation, marquee, browser…) are English-only; the rest of the
   app is fully localized. Also removes a latent release-`lintVital` `MissingTranslation` risk.
   Effort: **M** (translate ~35 strings × 9 locales). Risk: low.

5. **About: app version + "What's new" + Rate/Share** (`ui/main/About.java`). **[obs]** Currently
   social links only. Add `BuildConfig.versionName`, a short changelog/credits, and a Rate/Share
   action. Effort: **S**. Risk: low.

## Code quality / release hygiene
6. **Strip or guard debug logging.** **[obs]** 138 `Log.*` calls; many are verbose diagnostics that
   ship in release — `StatsCalculator` (18), `ItemSyncer` (13), `LiveStatsSyncer` (18),
   `EquipmentTester` (`STATS_UI`/`SKILLS`). Guard with `BuildConfig.DEBUG` or remove. Effort: **S**. Risk: low.

7. **EquipmentTester: remove the empty `calculateStats()` stub.** **[obs]** It's a no-op
   ("recompute … if you have one") invoked on every `requestRecalc()`; plus leftover
   `Log.d("STATS_UI"/"SKILLS")`. Effort: **trivial**. Risk: low.

## Lower priority / larger
8. **EnemyDetail "found in"** — which dungeons each enemy appears in (cross-reference enemies ↔ level
   data). Effort: **L**. **[inf]**
9. **Equipment build sharing** — export/import a set as a shareable code/QR; `SaveLoadManager` already
   serializes sets locally. Effort: **M–L**. **[inf]**
10. **News pull-to-refresh + offline cache** (`ui/main/News.java`). Effort: **S–M**. **[inf]**

## Suggested order
Quick wins first (7 → 6 → 5), then the high-value content/UX (1 Enemy elements, 2 Skins search),
then 3/4. Each is independently shippable and low-risk; none touch the Level Editor or the save path.
