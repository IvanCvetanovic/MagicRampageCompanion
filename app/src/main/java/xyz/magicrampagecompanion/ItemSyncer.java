package xyz.magicrampagecompanion;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class ItemSyncer {
    private static final String TAG = "ItemSyncer";

    // Raw JSON URL of your gist
    private static final String JSON_URL =
            "https://gist.githubusercontent.com/andresan87/5670c559e5a930129aa03dfce7827306/raw/items.json";

    public static void run(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // 1) Download JSON
                HttpURLConnection conn = (HttpURLConnection) new URL(JSON_URL).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(15000);
                int code = conn.getResponseCode();
                if (code != 200) {
                    Log.e(TAG, "HTTP " + code + " fetching item JSON");
                    conn.disconnect();
                    return;
                }
                BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();
                conn.disconnect();

                // 2) Parse → map by lowercased name_en
                JSONArray arr = new JSONArray(sb.toString());
                HashMap<String, JsonItem> map = new HashMap<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    JsonItem ji = toJsonItem(o);

                    if (ji.name_en != null && !ji.name_en.isEmpty()) {
                        map.put(ji.name_en.toLowerCase(), ji);
                    }
                    // fallback: also allow mapping by "name"
                    if (ji.name != null && !ji.name.isEmpty()) {
                        map.putIfAbsent(ji.name.toLowerCase(), ji);
                    }
                }

                // 3) Sync all lists
                int updatedWeapons = 0;
                updatedWeapons += syncWeapons(map, ItemData.swordList);
                updatedWeapons += syncWeapons(map, ItemData.daggerList);
                updatedWeapons += syncWeapons(map, ItemData.axeList);
                updatedWeapons += syncWeapons(map, ItemData.spearList);
                updatedWeapons += syncWeapons(map, ItemData.hammerList);
                updatedWeapons += syncWeapons(map, ItemData.staffList);

                int updatedArmors = syncArmors(map, ItemData.armorList);
                int updatedRings  = syncRings(map, ItemData.ringList);

                final int uw = updatedWeapons, ua = updatedArmors, ur = updatedRings;
                new Handler(Looper.getMainLooper()).post(() ->
                        Log.i(TAG, "Item sync done. weapons=" + uw + ", armors=" + ua + ", rings=" + ur));
            } catch (Exception e) {
                Log.e(TAG, "Item sync failed", e);
            }
        });
    }

    // ===================
    // Weapons full update
    // ===================
    private static int syncWeapons(HashMap<String, JsonItem> map, List<Weapon> list) {
        int updated = 0;
        for (int i = 0; i < list.size(); i++) {
            Weapon old = list.get(i);
            JsonItem ji = map.get(safeLower(old.getName()));
            if (ji == null) {
                Log.w(TAG, "No JSON match for weapon: " + old.getName());
                continue;
            }

            Elements element   = mapElement(ji.element, old.getElement());
            int upgrades = protectUpgrade(old.getUpgrades(), ji.maxLevelAllowed);
            int minDamage      = ji.damage != null ? ji.damage : old.getMinDamage();
            int maxDamage      = ji.maxLevelDamage != null ? ji.maxLevelDamage : old.getMaxDamage();
            int cooldown       = ji.attackCooldown != null ? ji.attackCooldown : old.getAttackCooldown();
            int pierce         = ji.pierceCount != null ? ji.pierceCount : old.getPierceCount();

            boolean pierceArea = ji.enablePierceAreaDamage != null ? ji.enablePierceAreaDamage : old.isEnablePierceAreaDamage();
            boolean projPers   = ji.persistAgainstProjectile != null ? ji.persistAgainstProjectile : old.isPersistAgainstProjectile();
            boolean poisonous  = ji.poisonous != null ? ji.poisonous : old.isPoisonous();
            boolean frost      = ji.frost != null ? ji.frost : old.isFrost();

            int speedPct       = pctInt(ji.speedBoost, old.getSpeed());
            int jumpPct        = pctInt(ji.jumpBoost,  old.getJump());
            double armorBonus  = pctDouble(ji.armorBoost, old.getArmorBonus());

            boolean same =
                    element == old.getElement() &&
                            upgrades == old.getUpgrades() &&
                            minDamage == old.getMinDamage() &&
                            maxDamage == old.getMaxDamage() &&
                            cooldown == old.getAttackCooldown() &&
                            pierce == old.getPierceCount() &&
                            pierceArea == old.isEnablePierceAreaDamage() &&
                            projPers == old.isPersistAgainstProjectile() &&
                            poisonous == old.isPoisonous() &&
                            frost == old.isFrost() &&
                            speedPct == old.getSpeed() &&
                            jumpPct == old.getJump() &&
                            (int)armorBonus == (int)old.getArmorBonus();

            if (same) continue;

            Weapon updatedW = new Weapon(
                    old.getName(),
                    old.getType(),
                    element,
                    minDamage,
                    maxDamage,
                    upgrades,
                    armorBonus,
                    speedPct,
                    jumpPct,
                    old.getImageResId(),
                    cooldown,
                    pierce,
                    pierceArea,
                    projPers,
                    poisonous,
                    frost
            );

            Log.i(TAG, "Updated weapon: " + old.getName());
            logDiff(old.getName(), "element", old.getElement(), element);
            logDiff(old.getName(), "upgrades", old.getUpgrades(), upgrades);
            logDiff(old.getName(), "minDamage", old.getMinDamage(), minDamage);
            logDiff(old.getName(), "maxDamage", old.getMaxDamage(), maxDamage);
            logDiff(old.getName(), "attackCooldown", old.getAttackCooldown(), cooldown);
            logDiff(old.getName(), "pierceCount", old.getPierceCount(), pierce);
            logDiff(old.getName(), "enablePierceAreaDamage", old.isEnablePierceAreaDamage(), pierceArea);
            logDiff(old.getName(), "persistAgainstProjectile", old.isPersistAgainstProjectile(), projPers);
            logDiff(old.getName(), "poisonous", old.isPoisonous(), poisonous);
            logDiff(old.getName(), "frost", old.isFrost(), frost);
            logDiff(old.getName(), "speed", old.getSpeed(), speedPct);
            logDiff(old.getName(), "jump", old.getJump(), jumpPct);
            logDiff(old.getName(), "armorBonus", old.getArmorBonus(), armorBonus);
            list.set(i, updatedW);
            updated++;
        }
        return updated;
    }

    // ==================
    // Armors full update
    // ==================
    private static int syncArmors(HashMap<String, JsonItem> map, List<Armor> list) {
        int updated = 0;
        for (int i = 0; i < list.size(); i++) {
            Armor old = list.get(i);
            JsonItem ji = map.get(safeLower(old.getName()));
            if (ji == null) {
                Log.w(TAG, "No JSON match for armor: " + old.getName());
                continue;
            }

            Elements element   = mapElement(ji.element, old.getElement());
            boolean frostImm   = ji.frost != null ? ji.frost : old.isFrostImmune();
            int upgrades = protectUpgrade(old.getUpgrades(), ji.maxLevelAllowed);

            int minArmor       = ji.armor != null ? ji.armor : old.getMinArmor();
            int maxArmor       = ji.maxLevelArmor != null ? ji.maxLevelArmor : old.getMaxArmor();

            int speedPct       = pctInt(ji.speedBoost, old.getSpeed());
            int jumpPct        = pctInt(ji.jumpBoost,  old.getJump());
            int magicPct       = pctInt(ji.magicBoost, (int)old.getMagic());
            int swordPct       = pctInt(ji.swordBoost, (int)old.getSword());
            int staffPct       = pctInt(ji.staffBoost, (int)old.getStaff());
            int daggerPct      = pctInt(ji.daggerBoost,(int)old.getDagger());
            int axePct         = pctInt(ji.axeBoost,   (int)old.getAxe());
            int hammerPct      = pctInt(ji.hammerBoost,(int)old.getHammer());
            int spearPct       = pctInt(ji.spearBoost, (int)old.getSpear());

            boolean same =
                    element == old.getElement() &&
                            frostImm == old.isFrostImmune() &&
                            upgrades == old.getUpgrades() &&
                            minArmor == old.getMinArmor() &&
                            maxArmor == old.getMaxArmor() &&
                            speedPct == old.getSpeed() &&
                            jumpPct == old.getJump() &&
                            magicPct == (int)old.getMagic() &&
                            swordPct == (int)old.getSword() &&
                            staffPct == (int)old.getStaff() &&
                            daggerPct == (int)old.getDagger() &&
                            axePct == (int)old.getAxe() &&
                            hammerPct == (int)old.getHammer() &&
                            spearPct == (int)old.getSpear();

            if (same) continue;

            Armor updatedA = new Armor(
                    old.getName(),
                    element,
                    frostImm,
                    minArmor,
                    maxArmor,
                    upgrades,
                    speedPct,
                    jumpPct,
                    magicPct,
                    swordPct,
                    staffPct,
                    daggerPct,
                    axePct,
                    hammerPct,
                    spearPct,
                    old.getImageResId()
            );

            Log.i(TAG, "Updated armor: " + old.getName());
            logDiff(old.getName(), "element", old.getElement(), element);
            logDiff(old.getName(), "frostImmune", old.isFrostImmune(), frostImm);
            logDiff(old.getName(), "upgrades", old.getUpgrades(), upgrades);
            logDiff(old.getName(), "minArmor", old.getMinArmor(), minArmor);
            logDiff(old.getName(), "maxArmor", old.getMaxArmor(), maxArmor);
            logDiff(old.getName(), "speed", old.getSpeed(), speedPct);
            logDiff(old.getName(), "jump", old.getJump(), jumpPct);
            logDiff(old.getName(), "magic", (int)old.getMagic(), magicPct);
            logDiff(old.getName(), "sword", (int)old.getSword(), swordPct);
            logDiff(old.getName(), "staff", (int)old.getStaff(), staffPct);
            logDiff(old.getName(), "dagger", (int)old.getDagger(), daggerPct);
            logDiff(old.getName(), "axe", (int)old.getAxe(), axePct);
            logDiff(old.getName(), "hammer", (int)old.getHammer(), hammerPct);
            logDiff(old.getName(), "spear", (int)old.getSpear(), spearPct);

            list.set(i, updatedA);
            updated++;
        }
        return updated;
    }

    // ================
    // Rings full update
    // ================
    private static int syncRings(HashMap<String, JsonItem> map, List<Ring> list) {
        int updated = 0;
        for (int i = 0; i < list.size(); i++) {
            Ring old = list.get(i);
            JsonItem ji = map.get(safeLower(old.getName()));
            if (ji == null) {
                Log.w(TAG, "No JSON match for ring: " + old.getName());
                continue;
            }

            Elements element   = mapElement(ji.element, old.getElement());

            int armor          = ji.armor != null ? ji.armor : old.getArmor();
            int speedPct       = pctInt(ji.speedBoost, old.getSpeed());
            int jumpPct        = pctInt(ji.jumpBoost,  old.getJump());
            int magicPct       = pctInt(ji.magicBoost, (int)old.getMagic());
            int swordPct       = pctInt(ji.swordBoost, (int)old.getSword());
            int staffPct       = pctInt(ji.staffBoost, (int)old.getStaff());
            int daggerPct      = pctInt(ji.daggerBoost,(int)old.getDagger());
            int axePct         = pctInt(ji.axeBoost,   (int)old.getAxe());
            int hammerPct      = pctInt(ji.hammerBoost,(int)old.getHammer());
            int spearPct       = pctInt(ji.spearBoost, (int)old.getSpear());
            double armorBonus  = pctDouble(ji.armorBoost, old.getArmorBonus());

            boolean same =
                    element == old.getElement() &&
                            armor == old.getArmor() &&
                            speedPct == old.getSpeed() &&
                            jumpPct == old.getJump() &&
                            magicPct == (int)old.getMagic() &&
                            swordPct == (int)old.getSword() &&
                            staffPct == (int)old.getStaff() &&
                            daggerPct == (int)old.getDagger() &&
                            axePct == (int)old.getAxe() &&
                            hammerPct == (int)old.getHammer() &&
                            spearPct == (int)old.getSpear() &&
                            (int)armorBonus == (int)old.getArmorBonus();

            if (same) continue;

            Ring updatedR = new Ring(
                    old.getName(),
                    element,
                    armor,
                    armorBonus,
                    speedPct,
                    jumpPct,
                    magicPct,
                    swordPct,
                    staffPct,
                    daggerPct,
                    axePct,
                    hammerPct,
                    spearPct,
                    old.getImageResId()
            );

            Log.i(TAG, "Updated ring: " + old.getName());
            logDiff(old.getName(), "element", old.getElement(), element);
            logDiff(old.getName(), "armor", old.getArmor(), armor);
            logDiff(old.getName(), "armorBonus", old.getArmorBonus(), armorBonus);
            logDiff(old.getName(), "speed", old.getSpeed(), speedPct);
            logDiff(old.getName(), "jump", old.getJump(), jumpPct);
            logDiff(old.getName(), "magic", (int)old.getMagic(), magicPct);
            logDiff(old.getName(), "sword", (int)old.getSword(), swordPct);
            logDiff(old.getName(), "staff", (int)old.getStaff(), staffPct);
            logDiff(old.getName(), "dagger", (int)old.getDagger(), daggerPct);
            logDiff(old.getName(), "axe", (int)old.getAxe(), axePct);
            logDiff(old.getName(), "hammer", (int)old.getHammer(), hammerPct);
            logDiff(old.getName(), "spear", (int)old.getSpear(), spearPct);

            list.set(i, updatedR);
            updated++;
        }
        return updated;
    }

    // ---- helpers ----
    private static String safeLower(String s) {
        if (s == null) return "";
        return s.replace("\\", "").toLowerCase();
    }

    private static Elements mapElement(String s, Elements fallback) {
        if (s == null || s.trim().isEmpty()) return fallback;
        switch (s.toLowerCase()) {
            case "air":       return Elements.AIR;
            case "water":     return Elements.WATER;
            case "earth":     return Elements.EARTH;
            case "fire":      return Elements.FIRE;
            case "light":     return Elements.LIGHT;
            case "dark":
            case "darkness":  return Elements.DARKNESS;
            case "neutral":   return Elements.NEUTRAL;
            default:          return fallback;
        }
    }

    // Convert multiplier (e.g., 1.10) → integer percent (10)
    private static int pctInt(Double mult, int fallback) {
        if (mult == null) return fallback;
        return (int)Math.round((mult - 1.0) * 100.0);
        // If mult==1.0 -> 0; if 1.06 -> 6; if 0.90 (unlikely) -> -10
    }

    // Same but keep as double (for armorBonus fields)
    private static double pctDouble(Double mult, double fallback) {
        if (mult == null) return fallback;
        return Math.round((mult - 1.0) * 100.0);
    }

    private static JsonItem toJsonItem(JSONObject o) {
        JsonItem ji = new JsonItem();
        ji.name             = optStr(o, "name");
        ji.name_en          = optStr(o, "name_en");
        ji.element          = optStr(o, "element");

        ji.maxLevelAllowed  = optInt(o, "maxLevelAllowed");

        ji.attackCooldown   = optInt(o, "attackCooldown");
        ji.damage           = optInt(o, "damage");
        ji.maxLevelDamage   = optInt(o, "maxLevelDamage");
        ji.pierceCount      = optInt(o, "pierceCount");

        ji.enablePierceAreaDamage   = optBool(o, "enablePierceAreaDamage");
        ji.persistAgainstProjectile = optBool(o, "persistAgainstProjectile");
        ji.poisonous                = optBool(o, "poisonous");
        ji.frost                    = optBool(o, "frost");

        ji.speedBoost       = optDouble(o, "speedBoost");
        ji.jumpBoost        = optDouble(o, "jumpBoost");
        ji.armorBoost       = optDouble(o, "armorBoost");

        ji.armor            = optInt(o, "armor");
        ji.maxLevelArmor    = optInt(o, "maxLevelArmor");

        ji.magicBoost       = optDouble(o, "magicBoost");
        ji.swordBoost       = optDouble(o, "swordBoost");
        ji.staffBoost       = optDouble(o, "staffBoost");
        ji.daggerBoost      = optDouble(o, "daggerBoost");
        ji.axeBoost         = optDouble(o, "axeBoost");
        ji.hammerBoost      = optDouble(o, "hammerBoost");
        ji.spearBoost       = optDouble(o, "spearBoost");
        return ji;
    }

    private static String optStr(JSONObject o, String k) {
        return o.has(k) && !o.isNull(k) ? o.optString(k, null) : null;
    }
    private static Integer optInt(JSONObject o, String k) {
        return o.has(k) && !o.isNull(k) ? o.optInt(k) : null;
    }
    private static Boolean optBool(JSONObject o, String k) {
        return o.has(k) && !o.isNull(k) ? o.optBoolean(k) : null;
    }
    private static Double optDouble(JSONObject o, String k) {
        return o.has(k) && !o.isNull(k) ? o.optDouble(k) : null;
    }

    // ---- logging helper ----
    private static void logDiff(String itemName, String field, Object oldVal, Object newVal) {
        if (oldVal == null && newVal == null) return;
        if (oldVal != null && oldVal.equals(newVal)) return;
        if (newVal != null && newVal.equals(oldVal)) return;

        Log.i(TAG, "  " + itemName + " | " + field + ": " + oldVal + " → " + newVal);
    }

    // Never allow upgrades to drop below 1 when the old value is already ≥1.
    private static int protectUpgrade(int oldUpgrades, Integer jsonMaxLevelAllowed) {
        if (jsonMaxLevelAllowed == null) return oldUpgrades;
        if (oldUpgrades >= 1 && jsonMaxLevelAllowed < 1) {
            Log.i(TAG, "  [protect] kept upgrades at 1 (JSON had " + jsonMaxLevelAllowed + ")");
            return 1;
        }
        return jsonMaxLevelAllowed;
    }


}
