package com.example.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.accountkit.AccountKit;

import example.base.store.UserPreference;
import example.base.util.LogUtil;
import pl.droidsonroids.gif.GifDrawable;
import com.example.BuildConfig;
import com.example.R;
import com.example.configs.VersionChecker;

/**
 * Created by Silver on 11/11/2016.
 */

public class OtherUtil {

    public static GifDrawable getGifDrawable(Context context) {
        try {
            return new GifDrawable(context.getResources(), R.mipmap.loading);
        } catch (Exception e) {
            LogUtil.e(e);
        }

        return null;
    }

    public static void checkVersion(final Context context) {
        try {
            VersionChecker versionChecker = new VersionChecker(context);
            String latestStoreVersion = versionChecker.execute().get();
            String currentVersion = BuildConfig.VERSION_NAME;

            if (Integer.valueOf(currentVersion.replace(".", ""))
                    < Integer.valueOf(latestStoreVersion.replace(".", ""))) {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.new_version))
                        .content(context.getString(R.string.prompt_play_store))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog,
                                                @NonNull DialogAction which) {
                                try {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("market://details?id=" + context.getPackageName())));
                                } catch (Exception e) {
                                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
                                }
                            }
                        })
                        .positiveText(R.string.update)
                        .negativeText(R.string.no)
                        .show();
            }

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
