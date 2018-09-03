package app.logo.com.opengldemo.util;

/**
 * Created by Administrator on 2018/9/2 0002.
 */

public class LogDebug {
    private static boolean debug = true;

    public static void e(String TAG, String msg) {
        if (debug)
            android.util.Log.e(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        if (debug)
            android.util.Log.d(TAG, msg);
    }
}
