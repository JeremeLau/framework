package com.guoguang.utils.acache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @Author: Jereme
 * @CreateDate: 2019-07-31
 */
public class ACacheUtils {

    public static final int TIME_HOUR = ACache.TIME_HOUR;
    public static final int TIME_DAY = ACache.TIME_DAY;
    public static final int TIME_MONTH = TIME_DAY * 30;
    public static final int TIME_FOREVER = 0;

    public static boolean getAsBoolean(Context context, String key, boolean defaultValue) {
        Object object = ACache.get(context).getAsObject(key);
        return object == null ? defaultValue : (boolean) object;
    }

    public static void setAsBoolean(Context context, String key, boolean value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static String getAsString(Context context, String key, String defaultValue) {
        String value = ACache.get(context).getAsString(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsString(Context context, String key, String value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static byte[] getAsBinary(Context context, String key, byte[] defaultValue) {
        byte[] value = ACache.get(context).getAsBinary(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsBinary(Context context, String key, byte[] value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static Bitmap getAsBitmap(Context context, String key, Bitmap defaultValue) {
        Bitmap value = ACache.get(context).getAsBitmap(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsBitmap(Context context, String key, Bitmap value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static Drawable getAsDrawable(Context context, String key, Drawable defaultValue) {
        Drawable value = ACache.get(context).getAsDrawable(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsDrawable(Context context, String key, Drawable value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static JSONArray getAsJSONArray(Context context, String key, JSONArray defaultValue) {
        JSONArray value = ACache.get(context).getAsJSONArray(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsJSONArray(Context context, String key, JSONArray value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static JSONObject getAsJSONObject(Context context, String key, JSONObject defaultValue) {
        JSONObject value = ACache.get(context).getAsJSONObject(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsJSONObject(Context context, String key, JSONObject value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }

    public static Object getAsObject(Context context, String key, Object defaultValue) {
        Object value = ACache.get(context).getAsObject(key);
        return value == null ? defaultValue : value;
    }

    public static void setAsObject(Context context, String key, Serializable value, int saveTime) {
        if (saveTime == TIME_FOREVER) {
            ACache.get(context).put(key, value);
        } else {
            ACache.get(context).put(key, value, saveTime);
        }
    }
}
