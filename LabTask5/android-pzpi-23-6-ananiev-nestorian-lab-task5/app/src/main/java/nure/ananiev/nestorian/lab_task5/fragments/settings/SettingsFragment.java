package nure.ananiev.nestorian.lab_task5.fragments.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.materialswitch.MaterialSwitch;

import nure.ananiev.nestorian.lab_task5.R;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        MaterialSwitch themeSwitch = view.findViewById(R.id.theme_switch);
        SeekBar fontSizeSeekBar = view.findViewById(R.id.font_size_seek_bar);
        TextView fontSizeText = view.findViewById(R.id.font_size_text);


        themeSwitch.setChecked(settingsViewModel.isDarkMode());
        fontSizeSeekBar.setProgress(settingsViewModel.getFontSize());
        fontSizeText.setText(getString(R.string.font_size, fontSizeSeekBar.getProgress()));

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsViewModel.setDarkMode(isChecked);
            requireActivity().recreate();
        });

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                settingsViewModel.setFontSize(progress);
                fontSizeText.setText(getString(R.string.font_size, fontSizeSeekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                requireActivity().recreate();
            }
        });

        return view;
    }
}