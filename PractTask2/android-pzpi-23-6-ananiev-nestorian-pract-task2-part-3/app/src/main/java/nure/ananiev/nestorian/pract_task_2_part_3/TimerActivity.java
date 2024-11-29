package nure.ananiev.nestorian.pract_task_2_part_3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TimerActivity extends AppCompatActivity {

    private static final String TIMER_SECONDS_KEY = "TIMER_SECONDS_KEY";

    private int timerSeconds;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        if (savedInstanceState != null) {
            timerSeconds = savedInstanceState.getInt(TIMER_SECONDS_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        timerTextView = findViewById(R.id.timer);

        updateTimer();
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerSeconds++;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    @SuppressLint("SetTextI18n")
    private void updateTimer() {
        timerTextView.setText(Integer.toString(timerSeconds));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIMER_SECONDS_KEY, timerSeconds);
    }
}