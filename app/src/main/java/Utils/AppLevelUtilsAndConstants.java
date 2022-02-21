package Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AppLevelUtilsAndConstants {
    public static class PreferencesKeys {
        public static final String JSON_STRING_KEY = "JsonString";
        public static final String IS_MAIN_ACTIVITY_VISITED = "IsMainActivityVisited";
    }
    public static class IntentKeys {
        public static final String DEPARTURE_ITEM = "Destination_item";
        public static final String DESTINATION_ITEM = "Arrival_item";
        public static final String DATE_ITEM = "Date_item";
    }
    public static void writeToPreferences(Context context, String key, Object value, Class<?> clazz){
            SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (clazz == String.class) {
                editor.putString(key, (String) value);
            }
            if (clazz == Boolean.class) {
                editor.putBoolean(key, (Boolean) value);
            }
            if (clazz == Integer.class) {
                editor.putInt(key, (Integer) value);
            }
            if (clazz == Float.class) {
                editor.putFloat(key, (Float) value);
            }
            if (clazz == Long.class) {
                editor.putLong(key, (Long) value);
            }
            editor.apply();
    }
    public static Object readFromPreferences(Context context, String key, Class<?> clazz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, MODE_PRIVATE);
        if (clazz == String.class) {
            return sharedPreferences.getString(key, null);
        }
        if (clazz == Boolean.class) {
            return sharedPreferences.getBoolean(key, false);
        }
        if (clazz == Integer.class) {
            return sharedPreferences.getInt(key, 0);
        }
        if (clazz == Float.class) {
            return sharedPreferences.getFloat(key, 0);
        }
        if (clazz == Long.class) {
            return sharedPreferences.getLong(key, 0);
        }
        return null;
    }
}
