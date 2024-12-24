package nure.ananiev.nestorian.lab_task5.fragments.settings;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;

import nure.ananiev.nestorian.lab_task5.activities.MainActivity;

public class SettingsViewModel extends AndroidViewModel {

    private final SharedPreferences sharedPreferences;

    public SettingsViewModel(Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(MainActivity.SETTINGS_SHARED_PREFERENCES_NAME, Application.MODE_PRIVATE);
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(MainActivity.DARK_MODE_KEY, false);
    }

    public void setDarkMode(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MainActivity.DARK_MODE_KEY, isDarkMode);
        editor.apply();
    }

    public int getFontSize() {
        return sharedPreferences.getInt(MainActivity.FONT_SIZE_KEY, MainActivity.DEFAULT_FONT_SIZE);
    }

    public void setFontSize(int fontSize) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MainActivity.FONT_SIZE_KEY, fontSize);
        editor.apply();
    }
}