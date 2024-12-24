package nure.ananiev.nestorian.lab_task5.activities;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import nure.ananiev.nestorian.lab_task5.R;
import nure.ananiev.nestorian.lab_task5.fragments.notes.NotesFragment;

public class MainActivity extends AppCompatActivity {

    public static final String SETTINGS_SHARED_PREFERENCES_NAME = "settings";
    public static final String DARK_MODE_KEY = "dark_mode";
    public static final String FONT_SIZE_KEY = "font_size";

    public static final int DEFAULT_FONT_SIZE = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale();
        adjustTheme();
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.current_fragment, new NotesFragment())
                    .commit();
        }
    }

    private void adjustFontScale() {
        int fontSize = getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                .getInt(FONT_SIZE_KEY, DEFAULT_FONT_SIZE);
        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) fontSize / DEFAULT_FONT_SIZE;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void adjustTheme() {
        boolean isDarkMode = getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                .getBoolean(DARK_MODE_KEY, false);

        if (isDarkMode) {
            setTheme(R.style.NotesDarkTheme);
        } else {
            setTheme(R.style.NotesLightTheme);
        }
    }

}