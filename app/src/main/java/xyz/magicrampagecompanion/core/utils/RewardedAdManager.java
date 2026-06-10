package xyz.magicrampagecompanion.core.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import xyz.magicrampagecompanion.BuildConfig;
import xyz.magicrampagecompanion.ui.main.MainActivity;

public class RewardedAdManager {

    private static final String TAG = "RewardedAdManager";

    private final String adUnitId;

    private RewardedAd rewardedAd;
    private boolean isLoading = false;

    private RewardedAdManager(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    // Debug builds must never request production ads (AdMob policy).
    public static RewardedAdManager forEquipmentTester() {
        return new RewardedAdManager(BuildConfig.DEBUG
                ? BuildConfig.testAPIKeyEquipmentTester
                : BuildConfig.realAPIKeyEquipmentTester);
    }

    public static RewardedAdManager forLevelViewer() {
        return new RewardedAdManager(BuildConfig.DEBUG
                ? BuildConfig.testAPIKeyLevelViewer
                : BuildConfig.realAPIKeyLevelViewer);
    }

    public void loadAd(Context context) {
        if (isLoading || rewardedAd != null) {
            return;
        }
        isLoading = true;

        com.google.android.gms.ads.AdRequest adRequest = MainActivity.buildAdRequest(context);
        RewardedAd.load(context, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                Log.d(TAG, "Rewarded ad loaded.");
                rewardedAd = ad;
                isLoading = false;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Failed to load rewarded ad: " + loadAdError.getMessage());
                rewardedAd = null;
                isLoading = false;
            }
        });
    }

    public boolean isReady() {
        return rewardedAd != null;
    }

    public void show(Activity activity, RewardCallback callback) {
        if (rewardedAd == null) {
            Log.d(TAG, "Tried to show, but no ad ready.");
            if (callback != null) callback.onAdNotReady();
            return;
        }

        rewardedAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed.");
                rewardedAd = null;
                loadAd(activity); // preload next
                if (callback != null) callback.onAdClosed();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.e(TAG, "Failed to show ad: " + adError.getMessage());
                rewardedAd = null;
                loadAd(activity);
                if (callback != null) callback.onAdFailed(adError);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen.");
            }
        });

        rewardedAd.show(activity, rewardItem -> {
            if (callback != null) {
                callback.onUserEarnedReward(rewardItem);
            }
        });
    }

    // Simple callback interface for EquipmentTester
    public interface RewardCallback {
        void onUserEarnedReward(RewardItem rewardItem);
        void onAdClosed();
        void onAdFailed(AdError error);
        void onAdNotReady();
    }
}
