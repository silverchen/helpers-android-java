package example.base.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Created by Silver on 8/16/16.
 */

public class LogUtil {

    public static boolean RELEASE_VERSION = true;
    private static String APP_LOG_FLAG = "example";
    public static int versionCode;

    public LogUtil() {
    }

    private static String getLogString(String format, Object... args) {
        if (!TextUtils.isEmpty(format)) {
            if (args != null && args.length > 0) {
                try {
                    return __generateLineInfo() + getThreadInfo() + String.format(Locale.ENGLISH, format, args);
                } catch (Exception e) {
                    e(e);
                    return __generateLineInfo() + getThreadInfo() + format;
                }
            } else {
                return __generateLineInfo() + getThreadInfo() + format;
            }
        } else {
            return "";
        }
    }

    public static void e(String format, Object... args) {
        if (!RELEASE_VERSION) {
            String ss = getLogString(format, args);
            Log.e(APP_LOG_FLAG, ss);
            if (ss.contains("UnknownFormatConversionException")) {
                i("OK", new Object[0]);
            }
        }
    }

    public static void w(String format, Object... args) {
        if (!RELEASE_VERSION) {
            String ss = getLogString(format, args);
            Log.w(APP_LOG_FLAG, ss);
            if (ss.contains("UnknownFormatConversionException")) {
                i("OK", new Object[0]);
            }
        }
    }

    public static void d(String format, Object... args) {
        if (!RELEASE_VERSION) {
            String ss = getLogString(format, args);
            Log.d(APP_LOG_FLAG, ss);
            if (ss.contains("UnknownFormatConversionException")) {
                i("OK", new Object[0]);
            }
        }
    }

    public static void i(String format, Object... args) {
        if (!RELEASE_VERSION) {
            String ss = getLogString(format, args);
            Log.i(APP_LOG_FLAG, ss);
            if (ss.contains("UnknownFormatConversionException")) {
                i("OK", new Object[0]);
            }
        }
    }

    public static void e(Throwable e) {
        if (e == null) {
            return;
        }
        if (!RELEASE_VERSION) {
            StackTraceElement[] stackTraceElement = e.getStackTrace();
            int currentIndex = -1;

            for (int result = 0; result < stackTraceElement.length; ++result) {
                if (stackTraceElement[result].getMethodName().compareTo("e") == 0) {
                    currentIndex = result + 1;
                    break;
                }
            }

            if (currentIndex >= 0) {
                String element = stackTraceElement[currentIndex].getClassName();
                String printWriter = element.substring(element.lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String.valueOf(stackTraceElement[currentIndex].getLineNumber());
                String traceContent = String.format(Locale.US, "position at %s.%s(%s.java:%s)", new Object[]{element, methodName, printWriter, lineNumber});
                Log.e(APP_LOG_FLAG, traceContent);
            } else {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                Log.e(APP_LOG_FLAG, stringWriter.toString());
            }
        }
    }

    private static String __generateLineInfo() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        int currentIndex = -1;

        String fullClassName;
        for (int traceElement = 0; traceElement < stackTraceElement.length; ++traceElement) {
            if (stackTraceElement[traceElement].getClassName().equals(LogUtil.class.getName())) {
                fullClassName = stackTraceElement[traceElement].getMethodName();
                if (fullClassName.equals("e") || fullClassName.equals("w") || fullClassName.equals("i") || fullClassName.equals("d")) {
                    currentIndex = traceElement + 1;
                    break;
                }
            }
        }

        if (currentIndex == -1) {
            Log.i(APP_LOG_FLAG, "CANNOT GENERATE DEBUG");
            return "";
        } else {
            StackTraceElement element = stackTraceElement[currentIndex];
            fullClassName = element.getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            return element.getMethodName() + "(" + className + ".java:" + element.getLineNumber() + "): ";
        }
    }

    private static String getThreadInfo() {
        return String.format(Locale.US, "[thread_id:%d name=%s] ", new Object[]{Long.valueOf(Thread.currentThread().getId()), Thread.currentThread().getName()});
    }
}
