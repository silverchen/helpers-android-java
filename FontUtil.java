package com.example.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import java.lang.reflect.Field;

import example.base.util.LogUtil;

/**
 * Created by Silver on 8/8/15.
 */
public class FontUtil {
    static Typeface defaultTypeface;
    static Typeface neutraTypeface;
    static Typeface neutraBoldTypeface;
    static Typeface robotoBoldTypeface;

    public static Typeface RobotoBoldTypeface(Context context) {
        if (robotoBoldTypeface == null && context != null) {
            robotoBoldTypeface = Typeface.createFromAsset(context.getAssets(), "HelveticaNeueLTStd-Bd.otf");
        }

        return robotoBoldTypeface;
    }

    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);

            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LogUtil.e(e);
        }
    }

    public static class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }
}
