package nure.ananiev.nestorian.pract_task_2_part_1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        textView = findViewById(R.id.text_view);
    }

    public void onButtonClick(View view) {
        textView.setText("Text changed");
    }

    public void onButtonClick1(View view) {
        Button button = (Button) view;
        Toast.makeText(this, button.getText(), Toast.LENGTH_SHORT).show();
    }
}