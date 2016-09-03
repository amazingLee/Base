package io.github.amazinglee.baseutil.local;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDate {
    public static final String PREFERENCE_NAME = "local_date";
    private static Context sContext;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    private LocalDate() {
        mSharedPreferences = sContext.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    public static LocalDate getInstance(Context context) {
        sContext = context;
        return LocalDateHolder.instance;
    }

    public static class LocalDateHolder {
        private static LocalDate instance = new LocalDate();
    }

    /**
     * Save the data
     *
     * @param strName  key
     * @param strValue The key value
     */
    public void setLocalDate(String strName, String strValue) {
        editor = mSharedPreferences.edit();
        editor.putString(strName, strValue);
        editor.apply();
    }

    /**
     * Read the data
     *
     * @param strName  key
     * @param dftValue The default value
     * @return Key value
     */
    public String getLocalDate(String strName, String dftValue) {
        return mSharedPreferences.getString(strName, dftValue);
    }

    /**
     * Boolean value of the type of storage
     *
     * @param name key
     * @param value The key value
     */
    public void setLocalDate(String name, boolean value) {
        editor = mSharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    /**
     * To obtain the value of the Boolean type
     *
     * @param name key
     * @param dftValue The default value
     * @return Key value
     */
    public boolean getLocalDate(String name, boolean dftValue) {
        return mSharedPreferences.getBoolean(name, dftValue);
    }
}
