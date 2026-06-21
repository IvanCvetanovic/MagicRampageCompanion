package xyz.magicrampagecompanion.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Two-tier (memory + disk) cache for the runtime-composited menu art — the menu-row banners and the
 * Editor-hub scenes. Those are painted from in-game art on a background thread, which makes the views
 * open empty and "pop" the art in a moment later. Caching the rendered bitmap means the expensive
 * compositing happens once; every later open loads it instantly (and the cold-start path can decode
 * it synchronously, so there is no pop).
 *
 * <p>The on-disk cache dir is keyed by the app's versionCode (+ a {@link #SCHEMA} bump for dev), so an
 * app update auto-invalidates stale art. Cached bitmaps are shared across ImageViews/activities —
 * callers must never recycle them.</p>
 */
public final class ArtCache {

    /** Bump when a builder's output changes but the app's versionCode hasn't (dev only). */
    private static final int SCHEMA = 1;

    /** ~32 MB of decoded bitmaps is plenty for ~12 menu images; evicted least-recently-used. */
    private static final LruCache<String, Bitmap> MEM = new LruCache<String, Bitmap>(32 * 1024 * 1024) {
        @Override protected int sizeOf(String key, Bitmap value) { return value.getByteCount(); }
    };

    public interface Builder { Bitmap build(); }

    private ArtCache() {}

    /**
     * Memory-or-disk lookup only — does not build. Fast enough (a lossless PNG decode) to call on the
     * UI thread during {@code onCreate} so cached art is present on the first frame. Returns null on a
     * miss.
     */
    public static Bitmap loadCached(Context ctx, String key) {
        Bitmap b = MEM.get(key);
        if (b != null) return b;
        File f = file(ctx, key);
        if (f.exists()) {
            b = BitmapFactory.decodeFile(f.getAbsolutePath());
            if (b != null) { MEM.put(key, b); return b; }
            f.delete(); // partial/corrupt write → drop and let it rebuild
        }
        return null;
    }

    /**
     * Memory → disk → build. Call OFF the UI thread (the build can be expensive). The freshly built
     * bitmap is persisted to memory + disk so subsequent opens hit {@link #loadCached}.
     */
    public static Bitmap loadOrBuild(Context ctx, String key, Builder builder) {
        Bitmap b = loadCached(ctx, key);
        if (b != null) return b;
        b = builder.build();
        if (b != null) {
            MEM.put(key, b);
            writeAtomic(file(ctx, key), b);
        }
        return b;
    }

    private static File file(Context ctx, String key) {
        File dir = new File(ctx.getApplicationContext().getCacheDir(),
                "art_" + versionCode(ctx) + "_" + SCHEMA);
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, key.replaceAll("[^A-Za-z0-9_]", "-") + ".png");
    }

    /** Write to a temp file then rename, so a kill mid-write can't leave a half-PNG that decodes wrong. */
    private static void writeAtomic(File dst, Bitmap b) {
        File tmp = new File(dst.getAbsolutePath() + ".tmp");
        try (FileOutputStream os = new FileOutputStream(tmp)) {
            b.compress(Bitmap.CompressFormat.PNG, 100, os); // lossless → cached == freshly built
            os.flush();
        } catch (Exception e) {
            tmp.delete();
            return;
        }
        if (!tmp.renameTo(dst)) tmp.delete();
    }

    /** App versionCode (minSdk 28 → getLongVersionCode is available); used to invalidate on update. */
    private static int versionCode(Context ctx) {
        try {
            return (int) ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0).getLongVersionCode();
        } catch (Exception e) {
            return 0;
        }
    }
}
