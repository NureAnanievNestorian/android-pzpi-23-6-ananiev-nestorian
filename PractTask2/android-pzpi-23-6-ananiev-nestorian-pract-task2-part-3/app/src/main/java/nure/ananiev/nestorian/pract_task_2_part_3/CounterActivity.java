package nure.ananiev.nestorian.pract_task_2_part_3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CounterActivity extends AppCompatActivity {

    private final String COUNTER_KEY = "COUNTER_KEY";

    private Button counterButton;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt(COUNTER_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        counterButton = findViewById(R.id.counter_button);
        updateCounterText();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNTER_KEY, counter);
    }

    public void onCounterButtonClick(View view) {
        counter++;
        updateCounterText();
    }

    @SuppressLint("SetTextI18n")
    private void updateCounterText() {
        counterButton.setText(getString(R.string.counter) + " " + counter);
    }
}