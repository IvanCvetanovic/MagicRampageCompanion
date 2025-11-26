package xyz.magicrampagecompanion.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import xyz.magicrampagecompanion.data.models.ItemData;

public final class LiveStatsSyncer {
    private LiveStatsSyncer() {}
    private static final String TAG = "LiveStatsSyncer";

    private static final String URL_STRING = "https://api.dkgamedev.com/api/?tag=read&key=live_stats_V2";
    private static final String PREFS_NAME = "live_stats_cache";
    private static final String KEY_ENML   = "enml_text";
    private static final String KEY_VER    = "enml_version";
    private static final String KEY_TS     = "enml_timestamp";

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    public static void sync(Context ctx) {
        Context app = ctx.getApplicationContext();
        EXEC.execute(() -> {
            long t0 = System.currentTimeMillis();
            String enml = null;
            boolean fromNetwork = false;

            Log.i(TAG, "==== Live stats sync begin ====");

            // 1) Try network
            try {
                String fetched = fetchRemote(URL_STRING);
                if (fetched != null && !fetched.trim().isEmpty()) {
                    fromNetwork = true;
                    enml = fetched;
                    String ver = extractVersion(fetched);
                    cacheSave(app, enml, ver);
                    Log.i(TAG, "Network fetch OK. version=" + (ver == null ? "?" : ver));
                } else {
                    Log.w(TAG, "Network fetch returned empty body.");
                }
            } catch (Exception e) {
                Log.w(TAG, "Network fetch failed (will try cache).", e);
            }

            // 2) Fallback to cache
            if (enml == null) {
                SharedPreferences sp = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                long ts = sp.getLong(KEY_TS, 0L);
                String ver = sp.getString(KEY_VER, "");
                enml = sp.getString(KEY_ENML, null);
                if (enml != null) {
                    Log.i(TAG, "Using cached stats. version=" + (ver == null || ver.isEmpty() ? "?" : ver)
                            + " cachedAt=" + (ts == 0L ? "?" : ts));
                } else {
                    Log.w(TAG, "No cached stats available. Keeping defaults.");
                    Log.i(TAG, "==== Live stats sync end (no data) ====");
                    return;
                }
            }

            // 3) Parse & apply (with detailed accounting)
            ResultCounts rc = applyEnml(enml);

            long t1 = System.currentTimeMillis();
            String ver = extractVersion(enml);
            Log.i(TAG, "Live stats applied. version=" + (ver == null ? "?" : ver)
                    + " source=" + (fromNetwork ? "network" : "cache")
                    + " timeMs=" + (t1 - t0));
            Log.i(TAG, "Summary: parsed=" + rc.parsedPairs
                    + " changed=" + rc.changed
                    + " unchanged=" + rc.unchanged
                    + " unmapped=" + rc.unmapped
                    + " errors=" + rc.errors);
            Log.i(TAG, "==== Live stats sync end ====");
        });
    }

    // --------- Networking ---------
    private static String fetchRemote(String urlStr) {
        HttpURLConnection conn = null;
        long t0 = System.currentTimeMillis();
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            if (code != 200) {
                Log.w(TAG, "HTTP " + code + " for live stats.");
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line; while ((line = br.readLine()) != null) sb.append(line).append('\n');
            br.close();

            long t1 = System.currentTimeMillis();
            Log.d(TAG, "fetchRemote OK in " + (t1 - t0) + "ms, bytes=" + sb.length());
            return sb.toString();
        } catch (Exception e) {
            Log.w(TAG, "fetchRemote error", e);
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    // --------- Cache ---------
    private static void cacheSave(Context ctx, String enml, String version) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_ENML, enml)
                .putString(KEY_VER, version == null ? "" : version)
                .putLong(KEY_TS, System.currentTimeMillis())
                .apply();
        Log.d(TAG, "Cache saved. version=" + (version == null ? "?" : version));
    }

    // --------- Parse & Apply ---------
    private static ResultCounts applyEnml(String enml) {
        String[] lines = enml.split("\n");
        ResultCounts rc = new ResultCounts();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("//")) continue;
            if (line.equals("{") || line.equals("}")) continue;
            if (!line.contains("=")) continue;

            // Remove trailing ';'
            if (line.endsWith(";")) line = line.substring(0, line.length() - 1).trim();

            String[] parts = line.split("=", 2);
            if (parts.length != 2) continue;

            String key = parts[0].trim();
            String value = parts[1].trim();
            if (key.isEmpty() || value.isEmpty()) continue;

            rc.parsedPairs++;

            ApplyOutcome outcome = applyStatVerbose(key, value);
            switch (outcome) {
                case CHANGED:   rc.changed++; break;
                case UNCHANGED: rc.unchanged++; break;
                case UNMAPPED:  rc.unmapped++; break;
                case ERROR:     rc.errors++; break;
            }
        }
        return rc;
    }

    private static String extractVersion(String enml) {
        for (String raw : enml.split("\n")) {
            String line = raw.trim();
            if (line.startsWith("LIVE_STAT_VERSION")) {
                if (line.endsWith(";")) line = line.substring(0, line.length() - 1).trim();
                String[] parts = line.split("=", 2);
                if (parts.length == 2) return parts[1].trim();
            }
        }
        return null;
    }

    // ---- Logging-aware application ----
    private enum ApplyOutcome { CHANGED, UNCHANGED, UNMAPPED, ERROR }
    private static class ResultCounts {
        int parsedPairs, changed, unchanged, unmapped, errors;
    }

    private static ApplyOutcome applyStatVerbose(String key, String value) {
        try {
            switch (key) {
                // ======== Skill Tree (percent values as integers in ENML) ========
                case "SK_SWORD_BOOST":
                    return setIntWithLog("SK_SWORD_BOOST", ItemData.SkillTreeSwordBonus,
                            toInt(value), v -> ItemData.SkillTreeSwordBonus = v);

                case "SK_DAGGER_BOOST":
                    return setIntWithLog("SK_DAGGER_BOOST", ItemData.SkillTreeDaggerBonus,
                            toInt(value), v -> ItemData.SkillTreeDaggerBonus = v);

                case "SK_STAFF_BOOST":
                    return setIntWithLog("SK_STAFF_BOOST", ItemData.SkillTreeStaffBonus,
                            toInt(value), v -> ItemData.SkillTreeStaffBonus = v);

                case "SK_SPEAR_BOOST":
                    return setIntWithLog("SK_SPEAR_BOOST", ItemData.SkillTreeSpearBonus,
                            toInt(value), v -> ItemData.SkillTreeSpearBonus = v);

                case "SK_HAMMER_BOOST":
                    return setIntWithLog("SK_HAMMER_BOOST", ItemData.SkillTreeHammerBonus,
                            toInt(value), v -> ItemData.SkillTreeHammerBonus = v);

                case "SK_AXE_BOOST":
                    return setIntWithLog("SK_AXE_BOOST", ItemData.SkillTreeAxeBonus,
                            toInt(value), v -> ItemData.SkillTreeAxeBonus = v);

                case "SK_SPEED_BOOST":
                    return setIntWithLog("SK_SPEED_BOOST", ItemData.SkillTreeSpeedBonus,
                            toInt(value), v -> ItemData.SkillTreeSpeedBonus = v);

                case "SK_JUMP_BOOST":
                    return setIntWithLog("SK_JUMP_BOOST", ItemData.SkillTreeJumpBonus,
                            toInt(value), v -> ItemData.SkillTreeJumpBonus = v);

                // ======== Elixirs (multipliers → (value*100 - 100)) ========
                case "ARCANE_PRECISION_TONIC_DAMAGE_NERF": {
                    // Keep fixed
                    int pct = -15;
                    return setIntWithLog(
                            "ARCANE_PRECISION_TONIC_DAMAGE_NERF(raw=" + value + ")",
                            ItemData.precisionTonicArmorBonus,
                            pct,
                            v -> ItemData.precisionTonicArmorBonus = v
                    );
                }
                case "ELIXIR_OF_DUPLICATION_ARMOR_NERF": {
                    // Keep fixed
                    int pct = -15;
                    return setIntWithLog(
                            "ELIXIR_OF_DUPLICATION_ARMOR_NERF(raw=" + value + ")",
                            ItemData.elixirOfDuplicationArmorBonus,
                            pct,
                            v -> ItemData.elixirOfDuplicationArmorBonus = v
                    );
                }
                case "ELIXIR_OF_DUPLICATION_SPEED_BOOST": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("ELIXIR_OF_DUPLICATION_SPEED_BOOST(raw=" + value + ")",
                            ItemData.elixirOfDuplicationSpeedBonus, pct, v -> ItemData.elixirOfDuplicationSpeedBonus = v);
                }
                case "MONSTERS_JUICE_ATTACK_BUFF": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("MONSTERS_JUICE_ATTACK_BUFF(raw=" + value + ")",
                            ItemData.monstersJuiceDamageBonus, pct, v -> ItemData.monstersJuiceDamageBonus = v);
                }
                case "MONSTERS_JUICE_ARMOR_NERF": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("MONSTERS_JUICE_ARMOR_NERF(raw=" + value + ")",
                            ItemData.monstersJuiceArmorBonus, pct, v -> ItemData.monstersJuiceArmorBonus = v);
                }
                case "PEPPER_BREW_ARMOR_BUFF": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("PEPPER_BREW_ARMOR_BUFF(raw=" + value + ")",
                            ItemData.pepperBrewArmorBonus, pct, v -> ItemData.pepperBrewArmorBonus = v);
                }
                case "STARLIGHT_SUPERTONIC_DAMAGE_BOOST": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("STARLIGHT_SUPERTONIC_DAMAGE_BOOST(raw=" + value + ")",
                            ItemData.starlightSupertonicDamageBonus, pct, v -> ItemData.starlightSupertonicDamageBonus = v);
                }
                case "TONIC_OF_INVULNERABILITY_ARMOR_BUFF": {
                    int pct = (int) Math.round(Double.parseDouble(value) * 100.0 - 100.0);
                    return setIntWithLog("TONIC_OF_INVULNERABILITY_ARMOR_BUFF(raw=" + value + ")",
                            ItemData.tonicOfInvulnerabilityArmorBonus, pct, v -> ItemData.tonicOfInvulnerabilityArmorBonus = v);
                }

                default:
                    // Unmapped key – useful for discovery (set to DEBUG to avoid noise)
                    Log.d(TAG, "Unmapped: " + key + " = " + value);
                    return ApplyOutcome.UNMAPPED;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed applying stat: " + key + " = " + value, e);
            return ApplyOutcome.ERROR;
        }
    }

    // Helper: log only when change occurs
    private interface IntSetter { void set(int v); }
    private static ApplyOutcome setIntWithLog(String label, int oldVal, int newVal, IntSetter setter) {
        if (oldVal == newVal) {
            Log.v(TAG, label + " unchanged (" + oldVal + ")");
            return ApplyOutcome.UNCHANGED;
        } else {
            setter.set(newVal);
            Log.i(TAG, label + " " + oldVal + " -> " + newVal);
            return ApplyOutcome.CHANGED;
        }
    }

    // ----- parsers -----
    private static int toInt(String s) {
        return (int) Math.round(Double.parseDouble(s));
    }

    /** Multiplier → percent, e.g., "1.25" → 25; "0.6" → -40 */
    private static int toPercentFromMultiplier(String s) {
        double m = Double.parseDouble(s);
        int pct = (int) Math.round((m - 1.0) * 100.0);
        return pct;
    }

    /** For “NERF” semantics where 1.05 should produce -5 (you store negatives for nerfs) */
    private static int toPercentNegFromMultiplier(String s) {
        double m = Double.parseDouble(s);
        int pct = (int) Math.round((1.0 - m) * 100.0);
        return pct;
    }
}
