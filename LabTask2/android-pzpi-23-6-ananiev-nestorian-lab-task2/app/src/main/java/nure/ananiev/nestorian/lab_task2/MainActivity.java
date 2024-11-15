package nure.ananiev.nestorian.lab_task2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private View colorPanel;
    private int redComponent, greenComponent, blueComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redSeekBar = findViewById(R.id.red_seek_bar);
        greenSeekBar = findViewById(R.id.green_seek_bar);
        blueSeekBar = findViewById(R.id.blue_seek_bar);
        colorPanel = findViewById(R.id.color_panel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSeekBarListener(redSeekBar);
        initSeekBarListener(greenSeekBar);
        initSeekBarListener(blueSeekBar);
    }

    private void initSeekBarListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == redSeekBar) {
                    redComponent = seekBar.getProgress();
                } else if (seekBar == greenSeekBar) {
                    greenComponent = seekBar.getProgress();
                } else if (seekBar == blueSeekBar) {
                    blueComponent = seekBar.getProgress();
                }
                updatePanelColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updatePanelColor() {
        colorPanel.setBackgroundColor(Color.rgb(redComponent, greenComponent, blueComponent));
    }
}