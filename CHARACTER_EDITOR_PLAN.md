# Character Editor — Build Plan & Progress

> Adding a **Character (`.character`) editor** to the companion app, alongside the Level Editor.
> This file is the source of truth across sessions. Tick the boxes as work lands:
> `- [ ]` = to be completed, `- [x]` = completed.

**Status:** Phases 0–8 implemented. P0–P6 emulator-verified. **P7** (sprite preview: atlas-aware body+armor+hair composite, body tinted) and **P8** (insert My Characters into a Level Editor spawner's `mobs` list, "+ char" button) are built + pushed, **pending on-device (phone) verification** (emulator retired at user's request). Deploy path **verified game-loadable**.
**Next action:** On a physical phone, verify P7 (visual — character preview renders) and P8 (semantic-diff — `mobs` edit round-trips via the existing `customDataEdited`→`patchNode` path, confirmed in code). Then feature-complete.
**Last updated:** 2026-06-20 (P7 + P8 implemented + pushed; phone verification pending)
**QA pass (2026-06-20, emulator):** `CharacterDocument` round-trips **byte-identical** across all 257 bundled files (unit test) + setValue/add/remove unit tests. On device: no-op save = **`cmp` byte-identical** (1011 B); a value edit = **exactly** that one line (`jumpImpulse 31→35`, tabs/CRLF preserved); add-field inserts a tab-indented line before `EOF____`; remove-field works; validation warns on empty name; My Characters reopen round-trips; SAF export = byte-identical to the in-app save. Logcat/crash-buffer clean throughout.

---

## Deploy path — VERIFIED (this is the gate; it passed)
Unlike a guess, this was checked against the user's **Steam install** (`D:\SteamLibrary\steamapps\common\Magic Rampage`):
- `.character` files are **loose, human-readable text** on disk under **`npcs/enemies/` (165), `npcs/allies/` (42), `npcs/bosses/` (40), `npcs/fighters/` (9)** — same moddable resource tree as the `scenes/` the Level Editor already targets.
- `resources.list` contains **no `.character` entries and no checksums** → **no integrity gate** on these files.
- A live `npcs/enemies/skeleton.character` is **byte-identical** to the app's bundled `assets/entities/skeleton.character`.
- Engine is **Ethanon** (`eth.log.txt`, `app.enml`), which loads these at runtime like scenes.

**Conclusion:** This is a **real, game-loadable modding tool** — export a `skeleton.character` and drop it into `npcs/enemies/` to override that enemy. **Deploy wrinkle vs. levels:** the target is `npcs/<category>/`, not one folder; the in-game spawner references the full path (`npcs/enemies/zombie-guard.character`). The app bundles characters **flattened** into `assets/entities/` (no category subdir) and is a **snapshot** (257 bundled vs 264 live) — same "edit-a-possibly-stale-copy, transport it yourself" model the Level Editor accepted.

## Goal
Let users **remix Magic Rampage characters** (enemies/allies/bosses) inside the companion app and **export game-loadable `.character` files**. Niche by design — same spirit as the Level Editor.

## Decisions locked (do not re-litigate)
- **Source = bundled `.character` (read-only at runtime):** edits become **"My Characters"** copies in `filesDir/usercharacters/`, exported via SAF. Mental model: bundled = immutable templates; edits = projects. (Mirrors the Level Editor.)
- **Save = in-place byte-level TEXT PATCH, never regenerate.** Load the raw file, replace ONLY the edited value(s) on their exact line(s), write everything else verbatim. This is the text analog of the Level Editor's DOM-patch and the ONLY way to survive the format's traps (below). **No-op save must be `cmp`-byte-identical.**
- **Do NOT touch `LevelParser.parseCharacterInternal`** (LevelParser.java:352). It is lossy (strips `;`, lowercases keys, keeps only a map, drops order/comments/dupes/unknown fields) AND it feeds the **level renderer** (spawner NPCs). The editor gets its **own** new `CharacterDocument`; the lossy parser stays as-is. Zero regression to VIEW/level rendering.
- **Navigation = an "Editor" hub (two-card chooser).** Rename the existing `🗺️ Level Viewer` main-menu button to **Editor** (string `level_viewer`, strings.xml:962) and repoint `MainActivity.java:98` from `LevelListActivity` to a new `EditorHubActivity`: a landing screen with two cards — **Levels** (→ existing `LevelListActivity`, unchanged) and **Characters** (→ new `CharacterListActivity`). **View⇄edit stays a mode INSIDE each tool** (levels = pencil toggle; characters = form) — do NOT gate view/edit at the hub (that double-gates the in-tool mode). The character list = a searchable list over the bundled files + a **"My Characters"** tab; **no** Story/Others taxonomy. Do **not** reuse `Enemies`/`EnemyDetail` — they render the **baked `ItemData.enemyList`** (curated subset, Parcelable, with **no link to the source file**), so they can't drive file editing.
- **No VIEW/EDIT mode toggle** (characters have no spatial canvas). Open a character → editable **form**. Saving **always writes a copy** (assets are immutable at runtime) — the Save→name flow must make the **"saved as a copy"** semantics obvious so users don't think they mutated the stock file.
- **Export preserves the original filename** (`skeleton.character`) — the **filename is the override key**, not the `name =` field. Export reminds the user of the `npcs/<category>/` drop target.
- **MVP form = generic per-block key/value editor** (reuse the proven CustomData-dialog pattern from the Level Editor). Equipped-item blocks edit as raw fields too. Curated "friendly" grouping + sprite preview + `ItemData` integration are **later phases**, not v1.

## Decisions to confirm (genuinely open)
- **Category awareness on export.** Bundled files are flat; the game wants `npcs/<category>/`. v1 can just remind the user; a nicer build could store/guess the category. Decide when P5 lands.
- **Undo/redo depth.** The Level Editor has a full command stack. For a form editor, per-field revert may suffice for v1; full undo is a P3+ option.

## Key technical notes & hazards
- **BYTE-EXACT I/O (the #1 trap).** Files are **CRLF** (`\r\n`) and end with `…}\r\n\r\nEOF____` — **no trailing newline**. A naive `BufferedReader.readLine()` + `println()` will convert CRLF→LF **and** append a final newline, breaking byte-identity. `CharacterDocument` must **preserve raw line terminators and the exact trailing bytes**. Verify with `cmp`, **not** a whitespace-normalizing differ.
- **Duplicate keys are real.** e.g. `rat-fire-mage.character`'s `equippedItem0` has `attackCooldown` **twice**. A key→value map (like the existing parser) silently drops one. The model must key edits to a **specific line occurrence**, not a name.
- **Escaped values.** e.g. `facts = …:true\;max-dodges-in-a-row:0\;…;` uses `\;` inside the value. The existing parser's `.replace(";","")` mangles this. The patcher must treat the value as the **raw substring after the first `=`**, not re-tokenize it.
- **Comments must survive.** `//damageSound = …`, `//walkStyle = slow;` appear inline — patch must leave them untouched.
- **Block structure:** one `character { }` + `equippedItem0..2 { }` (weapon/armor/accessory) + rarely `item0`/`item1`/`id` (5 files) + a literal `EOF____` terminator. Flat `key = value;` within blocks. Keys can be re-typed (string/number/bool/filename/color-int/escaped-string) — v1 treats all values as raw strings.
- **Unique among My Characters:** name copies `<base>_edited` like the Level Editor; on export the user renames to the exact target filename.

---

## Build order

### Phase 0 — `CharacterDocument` core (byte-exact round-trip) — THE GATE
- [ ] New `CharacterDocument` (new package, e.g. `.../character/`): parse raw bytes → ordered model of **blocks**, each holding ordered **entries** (raw line, optional key, raw value, isComment) — retaining everything.
- [ ] `serialize()` writes back **byte-for-byte** (preserve CRLF + trailing `EOF____`, no added newline).
- [ ] `setValue(blockIndex, entryIndex, newValue)` patches a single entry's value substring only.
- [ ] **Gate:** a host/unit test that round-trips **all bundled `.character`** and asserts `cmp`-identical (0 bytes differ). Do not proceed until green.
- Touches: new `CharacterDocument.java` (+ a small test harness).

### Phase 1 — Editor hub + browse list
- [ ] **Editor hub:** rename `🗺️ Level Viewer` → **Editor** (string `level_viewer`) and repoint `MainActivity.java:98` to a new `EditorHubActivity` — two cards: **Levels** (→ existing `LevelListActivity`, untouched) and **Characters** (→ `CharacterListActivity`). View⇄edit is NOT chosen here; it's the in-tool mode.
- [ ] `CharacterListActivity` (mirror `LevelListActivity`): list bundled `.character` (name from the file's `name =` or filename) + **search**; a **"My Characters"** tab listing `filesDir/usercharacters`.
- [ ] Tap → opens the editor for that file (asset path vs storage path extra, like the Level Editor's `levelFile`/`levelPath`).
- Touches: new `EditorHubActivity.java` + `activity_editor_hub.xml`, `CharacterListActivity.java`, `activity_character_list.xml`, `MainActivity.java`, `strings.xml`.

### Phase 2 — Read-only form
- [ ] `CharacterEditorActivity`: render each block as a section; each entry as a label (key) + read-only value; comments shown subtly. Reuse the detail-row styling from `EnemyDetail`/item detail layouts.
- Touches: `CharacterEditorActivity.java`, `activity_character_editor.xml`.

### Phase 3 — Editable values + in-place save
- [ ] Make values editable (EditText per entry); route every change through `CharacterDocument.setValue`.
- [ ] **Save** button → name dialog → `filesDir/usercharacters/<name>.character` via `CharacterDocument.serialize()`.
- [ ] **Verify:** no-op save = `cmp`-identical; a single field edit = exactly that byte change (diff shows only the one value).
- Touches: `CharacterEditorActivity.java`, `strings.xml`.

### Phase 4 — My Characters + reopen-from-storage
- [ ] "My Characters" tab opens saved files from storage; re-save reconciles against the **storage** file when opened from storage, else the asset (mirror the Level Editor's source-stream pick).
- Touches: `CharacterListActivity.java`, `CharacterEditorActivity.java`.

### Phase 5 — Export (SAF)
- [ ] "Export…" in the Save dialog → `CreateDocument` → user picks destination; **default filename = original** (`skeleton.character`). Export toast reminds: drop into `Magic Rampage/npcs/<category>/`.
- Touches: `CharacterEditorActivity.java`, `strings.xml`.

### Phase 6 — Add/remove + validation + niceties
- [ ] Add/remove a field within a block; add/remove an `equippedItem` block (harvest a real block, like the Level Editor's spawner templates).
- [ ] **Validation** (warn-but-allow): missing `EOF____`, empty `name`, malformed block — same dialog pattern as the Level Editor.
- [ ] Curated field grouping (Identity/Stats/Behaviour/Appearance/Sounds/Equipped) layered over the generic editor.
- Touches: `CharacterEditorActivity.java`, `CharacterDocument.java`, `strings.xml`.

### Phase 7 — Sprite preview (later)
- [ ] Reuse the level renderer's character compositing (`LevelParser.parseCharacterFile` → body/armor/hair/weapon sprites) to show a preview of the edited character.
- Touches: a small preview view + `CharacterEditorActivity`.

### Phase 8 — Cross-feature: use My Characters in the Level Editor (later; the payoff)
- [ ] In the Level Editor's spawner editing, let the inline mob list (`mobs = npcs/enemies/x.character,…`) pick from **My Characters**.
- [ ] **Deploy reality:** in-game this only works if BOTH files are deployed — the level (its spawner referencing `npcs/<category>/myEnemy.character`) AND the character file dropped into the game's `npcs/<category>/`. Surface a clear "export both" reminder; don't imply the reference alone is enough.
- Touches: Level Editor spawner editor + `CharacterDocument` filename resolution.

---

## Resume guide — build, run, verify
- **Git/commits:** same as Level Editor — Ivan Cvetanovic, **no Claude/Anthropic attribution**, scoped pathspec (`.idea/*` must not leak).
- **Build/emulator:** identical recipe to `LEVEL_EDITOR_PLAN.md` (`./gradlew :app:assembleDebug`; AVD `Pixel_10_Pro_XL`; `MSYS_NO_PATHCONV=1` for `/sdcard` adb paths; disable IMEs to stop the soft keyboard covering dialog buttons during UI automation).
- **Pull a saved character:** `adb exec-out run-as xyz.magicrampagecompanion cat files/usercharacters/<name>.character > out.character`.
- **Verify (key invariant):** `cmp` the saved file against the source — **no-op save = 0 bytes differ**; a single edit = exactly that byte change. Byte `cmp`, NOT the level editor's whitespace-normalizing `sdiff.py` (text patching demands byte-identity).
- **Real-deploy smoke (optional):** copy an exported file into `D:\SteamLibrary\steamapps\common\Magic Rampage\npcs\enemies\` and launch the game to confirm the override takes (e.g. rename an enemy).

## Reference (key files)
- `app/src/main/java/.../level/LevelParser.java` — `parseCharacterInternal` (:352) / `processCharacterBlock` (:390): the **lossy** reader to use as a **tokenization reference only** (do not modify; it feeds the renderer).
- `app/src/main/java/.../ui/enemies/Enemies.java`, `EnemyDetail.java` — visual conventions for stat rows (display-only, baked `ItemData.enemyList`; not a file editor).
- `app/src/main/java/.../ui/levelviewer/LevelListActivity.java`, `LevelViewerActivity.java`, `level/LevelSaver.java` — templates for list+tabs, open asset-vs-storage, SAF export.
- Bundled data: `app/src/main/assets/entities/*.character` (257). Live game: `…/Magic Rampage/npcs/{enemies,allies,bosses,fighters}/*.character` (264).
