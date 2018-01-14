package example.base.util;

import android.graphics.Color;

/**
 * Created by Silver on 20/11/15.
 */
public class ColorUtil {
    public static int colorWithAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }
}
