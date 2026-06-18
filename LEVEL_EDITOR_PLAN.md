# Level Editor — Build Plan & Progress

> Upgrading the in-app **Level Viewer** into a **Level Editor**.
> This file is the source of truth across sessions. Tick the boxes as work lands:
> `- [ ]` = to be completed, `- [x]` = completed.

**Status:** Phases 0–8 COMPLETE + pre-save **validation warnings** + **palette thumbnails** + **on-canvas rotate/scale handles** shipped. Full editor verified on emulator: select / inspect / move(+snap) / rotate+scale on canvas / add(+sprite-preview palette) / duplicate / delete / undo-redo, Save→"My Levels"→reopen, Export (SAF) to Downloads, CustomData value editing, and validation. Saves are surgical (no-op = 0 diffs; each edit = exactly its change). Remaining Phase 8 extra (spawner-template palette) deferred as optional.
**Next action:** Spawner-template palette (the last optional Phase 8 extra), or a new direction — the editor is functionally complete and game-faithful.
**Last updated:** 2026-06-18 (on-canvas rotate/scale handles added)

---

## Resume guide — build, run, verify (read this first when continuing)
**Git / commits:** repo git config is already Ivan Cvetanovic <icvetanovic99@gmail.com>; commit with **NO Claude/Anthropic attribution**. Always use a **scoped pathspec** — `git add <paths> && git commit -m "…" -- <paths>` — because the IDE auto-stages `.idea/*` and it must not leak into commits. Committing/pushing without asking is authorized for this repo.

**Build:** `./gradlew :app:assembleDebug` (set Bash timeout ~600000). APK at `app/build/outputs/apk/debug/app-debug.apk` (~213 MB). Package `xyz.magicrampagecompanion`. SDK at `C:/Users/PrOfSeS/AppData/Local/Android/Sdk`.

**Emulator** (AVD `Pixel_10_Pro_XL`, screen 1344×2992): launch it as a background task's OWN process — `emulator -avd Pixel_10_Pro_XL -no-snapshot-load -no-boot-anim` via `run_in_background: true`. **Do NOT background it with `&`** — that orphans/kills it. Wait for boot: `adb wait-for-device && adb shell 'while [[ -z $(getprop sys.boot_completed|tr -d "\r") ]]; do sleep 2; done'`.

**Git Bash gotcha:** prefix any `adb shell`/`adb pull` taking a `/sdcard/...` path with `MSYS_NO_PATHCONV=1`, and pull to a `C:/...` local path.

**Drive the UI:** `adb shell uiautomator dump /sdcard/u.xml` (MSYS_NO_PATHCONV=1) → pull → grep `text=`/`content-desc=` + `bounds=` for coordinates → `adb shell input tap X Y`. The soft keyboard can cover dialog buttons — `adb shell input keyevent 4` to dismiss before tapping SAVE.

**Read an app-private saved level:** `adb exec-out run-as xyz.magicrampagecompanion cat files/userlevels/<name>.esc > C:/.../out.esc`.

**Verify a save (key invariant):** Python `xml.etree` parse both the saved `.esc` and the source; compare tags + attributes-as-values (floats rounded) + text, ignoring whitespace/attr order. A **no-op save must be 0 diffs**; an edit must be exactly its own change. (This is what caught the NPC-scale-mutation bug.)

---

## Goal
Let users **remix existing Magic Rampage levels** inside the companion app and
(eventually) **export game-loadable `.esc` files**. Levels are played in the real
game by replacing a stock scene file with one of the same exact name (PC: drop into
`Magic Rampage/scenes/`). Niche by design — that's fine, this is built for love.

## Decisions locked (do not re-litigate)
- **Navigation:** ONE menu entry. Tapping a level opens the viewer as today; a
  **pencil button** in the top bar toggles `VIEW <-> EDIT` *in the same screen*.
  No second top-level button, no duplicate level list.
- **Assets are read-only at runtime:** bundled levels in `assets/levels/` can't be
  modified in place. "Edit" = open an **editable copy**. Saved edits become
  **projects** surfaced later as a **"My Levels"** third tab in `LevelListActivity`.
  Mental model: stock levels = immutable templates; edits = projects.
- **Save strategy = DOM-patch:** load the source `.esc` as an editable DOM and patch
  ONLY the nodes the user touched, so unedited entities round-trip byte-for-byte.
  This sidesteps the lossy pull-parser (it drops shape/type/static/applyLight/
  castShadow/EmissiveColor/CustomData-types/collision).
- **Palette strategy = harvest real blocks:** never synthesize entity XML from
  scratch. Most entities are `FileName`-referenced `.ent` placements (id + EntityName
  + Position + `<FileName>`) — trivially game-valid since the `.ent` (all 2,239 are
  bundled) holds the heavy data. Inline-defined entities (spawners) = copy a complete
  block from a real level and tweak.
- **Scope target:** Tier B mechanics + DOM-patch save + export. Consciously SKIP
  heavyweight blank-canvas / full-schema authoring.

## Key technical notes & hazards
- **`computeRenderY` (LevelRenderView.java:901):** stored `y` != on-screen `y`.
  Renderer draws at `y - z * sceneParallax * entity.parallax`. Any move tool MUST
  invert this (add the parallax term back) or entities with depth save to the wrong
  spot. Validate the math in Phase 1 (selection highlight) before relying on it.
- **Route mutations through a command layer starting Phase 3**, so undo/redo
  (Phase 5) isn't a painful retrofit.
- **Unique ids** when cloning/adding entities: use `maxId + 1` within the scene.
- **Zero regression:** VIEW mode must behave exactly as the current viewer
  throughout. The editor is additive (mode-gated), never a rewrite of view behavior.
- **Reuse, don't duplicate:** editor = `LevelRenderView` + a tools overlay owned by
  the activity. Keep editor UI (toolbar, inspector) in the activity layout, shown/
  hidden by mode — don't stuff it into the render view.

---

## Build order

### Phase 0 — Mode scaffolding (foundation; no editing yet) ✅ DONE
- [x] Add `VIEW`/`EDIT` state to the level screen (`editMode` + `setEditMode`/`isEditMode` in `LevelRenderView`).
- [x] Add a **pencil button** to the top bar in `activity_level_viewer.xml`; wire it in `LevelViewerActivity` to toggle mode.
- [x] Add an (empty) **editor overlay container** (`editorToolbar`, hidden in VIEW) in the activity layout.
- [x] Verify: builds clean (assembleDebug). Runtime toggle (button tint + toolbar show/hide + toast) to confirm on device.
- Touches: `LevelViewerActivity.java`, `activity_level_viewer.xml`, `LevelRenderView.java`, `strings.xml`.

### Phase 1 — Selection + highlight (non-destructive; validates coordinate math) ✅ DONE
- [x] In EDIT mode, repurpose `handleTap` to set a `selectedEntity` (top of z-stack) instead of logging.
- [x] Allow cycling through stacked entities on repeated near-taps (`handleEditSelection`, uses `TAP_SLOP_PX`).
- [x] Draw a selection box in `onDraw` (EDIT only) via `computeRenderY`, sized to true sprite bounds (`getSelectionHalfExtents`).
- [x] Tap empty space = deselect.
- [x] Verify: builds clean. Runtime check (highlight lands on sprite, cycling works) pending on device.
- Touches: `LevelRenderView.java` (`handleTap`, `handleEditSelection`, `onDraw`, `drawSelectionHighlight`).
- Note: tap target still uses the cheap BASE_TILE box; highlight uses true sprite bounds. Reconcile in Phase 3 (move).

### Phase 2 — Property inspector (read, then edit scalars) ✅ DONE
- [x] Show selected entity's fields: header (name • sprite) + read-only meta (blend, customData keys); editable X/Y/Z, ScaleX/Y, Angle, Flip X/Y.
- [x] Make scalars editable via guarded TextWatchers; mutate the in-memory `LevelEntity` + `invalidate()` for live re-render.
- [x] Verify: builds clean. Runtime check (edit z/scale → render updates) pending on device.
- Touches: `activity_level_viewer.xml` (editorBottomPanel/editorInspector), `LevelViewerActivity.java`, `LevelRenderView.java` (OnSelectionChangedListener), `strings.xml`.
- Deferred polish: keyboard may cover panel; EditText styling on dark bg; editing CustomData/blendMode (Phase 8).

### Phase 3 — Move tool (drag + the `computeRenderY` inversion + grid snap) ✅ DONE
- [x] In EDIT with a selection: drag starting on the selection's footprint moves it (uses `getSelectionHalfExtents` — reconciles the Phase 1 grab/visual note); drag elsewhere pans; 2nd finger = pinch.
- [x] Stored `y` via inverting `computeRenderY` (`parallaxOffsetY` adds back `z*sceneParallax*entity.parallax`); stored `x` direct (`dragSelectedEntityTo`).
- [x] Grid snapping to the 64-unit grid, default ON, toggled by a top-bar button shown only in EDIT (`ic_grid_snap`).
- [x] Move routed through `MoveCommand` on an undo/redo stack in `LevelRenderView` (`pushCommand`/`undo`/`redo` — Phase 5 wires buttons).
- [x] Verify: builds clean; inversion verified algebraically. Runtime drag/snap check pending on device.
- Touches: `LevelRenderView.java` (touch handler, move + command layer), `activity_level_viewer.xml`, `LevelViewerActivity.java`, `ic_grid_snap.xml`, `strings.xml`.

### Phase 4 — Add / delete / duplicate entities ✅ DONE
- [x] **Delete** selected (`StructuralCommand`, rebuilds the z-sorted list); guarded by a confirm dialog (undo button arrives Phase 5).
- [x] **Duplicate** selected (`LevelEntity.copy()` — full deep copy + fresh CustomData, `maxId+1`, +1 tile); selects the copy.
- [x] **Add from palette:** searchable dialog over bundled `.ent` files; creates a `FileName`-referenced entity at screen-center (snapped), resolved via `parseEntFile` so it renders.
- [x] All three routed through `StructuralCommand` on the undo/redo stack. New bottom toolbar (Add/Duplicate/Delete) padded for the nav-bar inset.
- [x] Verify: builds clean. Runtime check (palette filter/place, duplicate, delete-confirm, button enable state) pending on device.
- Touches: `LevelRenderView.java`, `LevelEntity.java` (`copy`), `LevelViewerActivity.java` (toolbar + palette + inset fix), `activity_level_viewer.xml`, `dialog_entity_palette.xml`, `ic_add/ic_delete/ic_duplicate.xml`, `strings.xml`.

### Phase 5 — Undo / redo ✅ DONE
- [x] Command stack now wraps all edit types: move/add/delete/duplicate (Phases 3–4) + numeric property edits (one command per focus session) + flip toggles.
- [x] Undo/Redo buttons in the bottom toolbar (right side); enable-state driven by `OnHistoryChangedListener`.
- [x] Verify: builds clean. Runtime check (undo/redo across each op type) pending on device.
- Touches: `LevelRenderView.java` (history listener), `LevelViewerActivity.java` (binding refactor + undo/redo wiring), `activity_level_viewer.xml`, `ic_undo/ic_redo.xml`, `strings.xml`.
- Note: property-edit commands commit on field **blur** (not via populateInspector — that path also runs during undo and would clear the redo stack).

### Phase 6 — Save (DOM-patch) + "My Levels"

**6a — Saver + Save button ✅ DONE (verified on emulator)**
- [x] Reconcile by document ORDER, not id (ids aren't unique: dungeon16/35/42/43.1). `editOrdinal` tagged at parse; `LevelSaver` re-reads the source `.esc` into a DOM, removes deleted nodes, patches survivors, appends new.
- [x] New entities: duplicates clone their source DOM node (via `sourceOrdinal`) → faithful; palette adds synthesize a minimal FileName block.
- [x] `patchNode` writes Position/flip always, but `<Scale>` only when `scaleEdited` (renderer mutates scaleX/Y for NPC spawns — the no-op diff caught this 4→1 corruption). Never writes spriteFrame / inserts Scale.
- [x] Save button (top bar, EDIT-only) + name dialog → `filesDir/userlevels/<name>.esc`. `LevelSaver.save(InputStream source, …)` is source-agnostic (not asset-only) per advisor.
- [x] VERIFIED: no-op save = 0 semantic diffs (589 entities); duplicate→save = +1 faithful clone, originals untouched. Pre-checks: all 74 parse with strict DOM; 0 id-less outer entities (ordinal alignment guaranteed).
- Caveats: exhaustive all-74 runtime no-op not run (tested Dungeon 1 + structural guarantees); scale edit on a FileName-referenced entity isn't persisted (no `<Scale>` node) — v1 limit.

**6b — My Levels + load-from-storage ✅ DONE (verified on emulator)**
- [x] `LevelParser` stream overload `parse(ctx, InputStream, name)`; `LevelViewerActivity` loads from a `levelPath` extra (storage) vs `levelFile` (asset), via `levelKey` for title/secrets/zoom.
- [x] "My Levels" third tab in `LevelListActivity` lists `filesDir/userlevels` (refreshed in `onResume`); tap opens via `levelPath`.
- [x] Re-saving reconciles against the right source: storage file when `levelPath` set, else the asset (`doSave` picks the source stream; same-file read-then-write is fine on Android).
- [x] Verify: My Levels tab lists `dungeon0_edited`; opening it loads from storage and renders with the saved name as title.
- Touches: `LevelParser.java`, `LevelViewerActivity.java`, `LevelListActivity.java`, `activity_level_list.xml`, `strings.xml`.
- Note: saved levels lose text-entity localization (biome-strings lookup keys off the saved name) — minor; CustomData "text" entities show keys. Re-save-edit-from-storage path implemented but not separately UI-driven.

### Phase 7 — Export to a user-accessible location ✅ DONE (verified on emulator)
- [x] "Export…" button in the Save dialog → Storage Access Framework (`CreateDocument`) → user picks destination (Downloads/Documents/...); no storage permission needed.
- [x] Reuses `LevelSaver` via a new `OutputStream` overload (writes the reconciled .esc to the picked Uri).
- [x] Verify: Export → picker → SAVE to Downloads wrote `dungeon0_edited.esc`; semantic diff vs original = 0.
- Touches: `LevelSaver.java` (OutputStream overload), `LevelViewerActivity.java`, `strings.xml`.
- Scope (per user): local on-device export only — no PC integration / scene-name mapping; user transports the file. Export toast reminds them to drop it in Magic Rampage/scenes/.

### Phase 8 — Polish / fidelity hardening (optional, ongoing)
- [x] **CustomData editing** in the inspector — tap the meta line (✎) → edit existing key values; persisted via a `customDataEdited`-gated DOM `<Value>` patch. Verified: one edit = exactly one `<Value>` change; no-op stays 0-diff.
- [ ] Inline-defined entities (spawners) palette via "copy real block" templates. *(deferred — larger)*
- [x] **Validation warnings** before Save/Export — a *warn-but-allow* dialog ("Save anyway" / Cancel) gates `showSaveDialog` (covers both Save and Export, which funnel through it). Checks shipped: **no player spawn** (`spawn0`) and **empty level**. `spawn0` is the universal player-start invariant — all 74 stock levels contain it, and the parser surfaces it as a top-level `entityName` (verified on the parse path + on emulator: a stock level saves with NO warning = no false positive). **Deliberately NOT checked** (would be false positives): *duplicate ids* — stock levels reuse ids (dungeon16/35/42/43.1) and the game tolerates it; saves reconcile by document order, and the editor assigns `maxId+1` so it never introduces collisions. *Out-of-bounds* — the scene model (`SceneProperties`) has no declared bounds. Strings are English-only, matching the editor's existing string convention. Touches: `LevelViewerActivity.java` (`attemptSave`/`validateLevel`), `strings.xml`.
- [x] **Palette thumbnails** — the Add-entity palette shows each entity's sprite preview. A self-contained loader (does NOT touch the render view's `spriteCache`, so it's safe off the UI thread) parses the `.ent` for its `<Sprite>`, decodes that PNG downsampled (`inSampleSize`), and crops frame 0 from any uniform sheet; results are cached in an 8 MB `LruCache` and loaded on a small thread pool with `setTag`-based recycling safety. Spriteless helper entities (markers/lines) show blank. Verified on emulator: `crate 0–3`, `number`, etc. render real sprites. Touches: `LevelViewerActivity.java` (`PaletteAdapter`/`resolveThumbnail`), `item_palette_entry.xml`.
- [x] **On-canvas rotate/scale handles** — EDIT mode draws a rotate handle (above top-center) and a uniform-scale handle (bottom-right corner) on the selection. In `ACTION_DOWN` they're hit-tested *before* body-move/pan; gesture references are captured once at DOWN; rotate maps the pointer's angle about the entity center to `entity.angle` (single `ROTATE_SIGN` term, verified on device), scale maps distance-to-center to a uniform `scaleX/Y` (sets `scaleEdited`); the whole gesture commits as one `RotateScaleCommand` on UP (undoable). Position (`x/y`) is never touched. Verified on emulator: rotate (→−87°, natural direction), uniform scale (→2.54×), undo→redo, and **no regression** to body-drag move (still grid-snaps) or pan. Touches: `LevelRenderView.java` (`onTouchEvent`, `drawSelectionHighlight`, handle helpers + `RotateScaleCommand`).
- Note: CustomData edits aren't on the undo stack (dialog has Cancel); only existing keys' values are editable (no add/remove/type change). Soft keyboard can overlap the dialog's buttons — scroll if needed.

---

## Reference (key files)
- `app/src/main/java/.../ui/levelviewer/LevelViewerActivity.java` — screen host.
- `app/src/main/java/.../ui/levelviewer/LevelRenderView.java` — renderer + gestures (~1380 lines; the reused core).
- `app/src/main/java/.../ui/levelviewer/LevelListActivity.java` — level list (Story/Others tabs).
- `app/src/main/java/.../level/LevelParser.java` — `.esc`/`.ent`/`.character` parser (read path; lossy).
- `app/src/main/java/.../level/Level.java`, `.../ui/levelviewer/LevelEntity.java`, `SceneProperties.java` — model.
- `app/src/main/res/layout/activity_level_viewer.xml` — top bar to add the pencil/toolbar.
- `app/src/main/assets/levels/*.esc` — bundled stock levels (read-only). `app/src/main/assets/entities/` — full entity library (2,239 `.ent`).
