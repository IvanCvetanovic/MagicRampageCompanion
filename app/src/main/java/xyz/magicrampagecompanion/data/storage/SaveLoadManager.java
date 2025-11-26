package xyz.magicrampagecompanion.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.magicrampagecompanion.data.models.EquipmentSet;
import xyz.magicrampagecompanion.data.models.ItemData;
import xyz.magicrampagecompanion.data.models.Armor;
import xyz.magicrampagecompanion.data.models.CharacterClass;
import xyz.magicrampagecompanion.data.models.Elixir;
import xyz.magicrampagecompanion.data.models.Ring;
import xyz.magicrampagecompanion.data.models.Weapon;
import xyz.magicrampagecompanion.enums.Elements;

public final class SaveLoadManager {
    private static final String PREFS = "EquipmentSaves";

    // quick multi-set save (kept for convenience)
    private static final String KEY_QUICK = "quick_sets_json";

    // named single-set saves map: { name -> setJson }
    private static final String KEY_NAMED_MAP = "named_sets_map_json";

    private SaveLoadManager() {}

    // ---------- QUICK (existing) ----------
    public static void saveQuick(Context ctx, java.util.List<EquipmentSet> sets) {
        JSONArray arr = new JSONArray();
        for (EquipmentSet s : sets) arr.put(toJson(ctx, s));
        prefs(ctx).edit().putString(KEY_QUICK, arr.toString()).apply();
    }

    public static ArrayList<EquipmentSet> loadQuick(Context ctx) {
        String raw = prefs(ctx).getString(KEY_QUICK, "");
        ArrayList<EquipmentSet> result = new ArrayList<>();
        if (TextUtils.isEmpty(raw)) return result;
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                result.add(fromJson(ctx, arr.getJSONObject(i)));
            }
        } catch (JSONException ignored) {}
        return result;
    }

    // ---------- NAMED (new) ----------
    public static void saveSetNamed(Context ctx, String name, EquipmentSet set) {
        JSONObject map = readNamedMap(ctx);
        try {
            map.put(name, toJson(ctx, set));
        } catch (JSONException ignored) {}
        prefs(ctx).edit().putString(KEY_NAMED_MAP, map.toString()).apply();
    }

    public static EquipmentSet loadSetByName(Context ctx, String name) {
        JSONObject map = readNamedMap(ctx);
        JSONObject o = map.optJSONObject(name);
        if (o == null) return null;
        try {
            return fromJson(ctx, o);
        } catch (JSONException e) {
            return null;
        }
    }

    public static boolean exists(Context ctx, String name) {
        return readNamedMap(ctx).has(name);
    }

    public static ArrayList<String> listSavedSetNames(Context ctx) {
        JSONObject map = readNamedMap(ctx);
        ArrayList<String> out = new ArrayList<>();
        JSONArray names = map.names();
        if (names == null) return out;
        for (int i = 0; i < names.length(); i++) out.add(names.optString(i));
        java.util.Collections.sort(out, String::compareToIgnoreCase);
        return out;
    }

    public static void deleteNamed(Context ctx, String name) {
        JSONObject map = readNamedMap(ctx);
        map.remove(name);
        prefs(ctx).edit().putString(KEY_NAMED_MAP, map.toString()).apply();
    }

    private static JSONObject readNamedMap(Context ctx) {
        String raw = prefs(ctx).getString(KEY_NAMED_MAP, "{}");
        try { return new JSONObject(raw); }
        catch (JSONException e) { return new JSONObject(); }
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // ---------- serialize one EquipmentSet ----------
    private static JSONObject toJson(Context ctx, EquipmentSet s) {
        JSONObject o = new JSONObject();
        try {
            o.putOpt("armorName",  s.armor != null ? s.armor.getName() : null);
            o.putOpt("ringName",   s.ring  != null ? s.ring.getName()  : null);
            o.putOpt("weaponName", s.weapon!= null ? s.weapon.getName(): null);
            o.putOpt("className",  s.characterClass != null ? s.characterClass.getName(ctx) : null);

            o.put("armorUp",  s.armorUpgrades);
            o.put("weaponUp", s.weaponUpgrades);

            o.put("armorEl",  s.armorElement  != null ? s.armorElement.name()  : Elements.NEUTRAL.name());
            o.put("ringEl",   s.ringElement   != null ? s.ringElement.name()   : Elements.NEUTRAL.name());
            o.put("weaponEl", s.weaponElement != null ? s.weaponElement.name() : Elements.NEUTRAL.name());

            JSONArray skills = new JSONArray();
            if (s.skills != null) for (boolean b : s.skills) skills.put(b);
            o.put("skills", skills);

            o.putOpt("elixirName", s.elixir != null ? s.elixir.getName() : null);
        } catch (JSONException ignored) {}
        return o;
    }

    private static EquipmentSet fromJson(Context ctx, JSONObject o) throws JSONException {
        EquipmentSet s = new EquipmentSet();

        String armorName  = o.optString("armorName",  null);
        String ringName   = o.optString("ringName",   null);
        String weaponName = o.optString("weaponName", null);
        String className  = o.optString("className",  null);

        if (!TextUtils.isEmpty(armorName)) {
            for (Armor a : ItemData.armorList) if (armorName.equals(a.getName())) { s.armor = a; break; }
        }
        if (!TextUtils.isEmpty(ringName)) {
            for (Ring r : ItemData.ringList)  if (ringName.equals(r.getName())) { s.ring = r; break; }
        }
        if (!TextUtils.isEmpty(weaponName)) {
            if (s.weapon == null) for (Weapon w : ItemData.swordList)  if (weaponName.equals(w.getName())) { s.weapon = w; break; }
            if (s.weapon == null) for (Weapon w : ItemData.daggerList) if (weaponName.equals(w.getName())) { s.weapon = w; break; }
            if (s.weapon == null) for (Weapon w : ItemData.staffList)  if (weaponName.equals(w.getName())) { s.weapon = w; break; }
            if (s.weapon == null) for (Weapon w : ItemData.axeList)    if (weaponName.equals(w.getName())) { s.weapon = w; break; }
            if (s.weapon == null) for (Weapon w : ItemData.spearList)  if (weaponName.equals(w.getName())) { s.weapon = w; break; }
            if (s.weapon == null) for (Weapon w : ItemData.hammerList) if (weaponName.equals(w.getName())) { s.weapon = w; break; }
        }
        if (!TextUtils.isEmpty(className)) {
            for (CharacterClass c : ItemData.classList) if (className.equals(c.getName(ctx))) { s.characterClass = c; break; }
        }

        s.armorUpgrades  = o.optInt("armorUp",  0);
        s.weaponUpgrades = o.optInt("weaponUp", 0);

        s.armorElement   = parseEl(o.optString("armorEl",  Elements.NEUTRAL.name()));
        s.ringElement    = parseEl(o.optString("ringEl",   Elements.NEUTRAL.name()));
        s.weaponElement  = parseEl(o.optString("weaponEl", Elements.NEUTRAL.name()));

        JSONArray skills = o.optJSONArray("skills");
        if (skills != null) {
            int n = skills.length();
            s.skills = new boolean[Math.max(36, n)];
            for (int i = 0; i < n; i++) s.skills[i] = skills.optBoolean(i, false);
        }

        String elixirName = o.optString("elixirName", null);
        if (!TextUtils.isEmpty(elixirName) && ItemData.elixirList != null) {
            for (Elixir e : ItemData.elixirList) if (elixirName.equals(e.getName())) { s.elixir = e; break; }
        }

        return s;
    }

    private static Elements parseEl(String name) {
        try { return Elements.valueOf(name); }
        catch (Exception ignore) { return Elements.NEUTRAL; }
    }
}
