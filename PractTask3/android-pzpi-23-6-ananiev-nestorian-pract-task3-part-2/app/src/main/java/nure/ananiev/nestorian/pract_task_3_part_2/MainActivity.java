package nure.ananiev.nestorian.pract_task_3_part_2;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView textView = findViewById(R.id.handlerMessageTextView);
        Handler handler = new Handler(Looper.getMainLooper());

        Button startDelayedHandlerButton = findViewById(R.id.startDelayedHandlerButton);
        startDelayedHandlerButton.setOnClickListener(v -> {
            handler.postDelayed(() -> textView.setText("Handler executed after delay"), 2000);
        });

        Button startBackgroundThreadButton = findViewById(R.id.backgroundThreadHandlerButton);
        startBackgroundThreadButton.setOnClickListener(v -> new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.post(() -> textView.setText("Updated from background thread"));
        }).start());

        Button sendMessageWithHandlerButton = findViewById(R.id.sendMessageWithHandlerButton);
        sendMessageWithHandlerButton.setOnClickListener(v -> {
            Handler handler1 = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    textView.setText("Message received: " + msg.what);
                }
            };

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = handler1.obtainMessage();
                msg.what = 1;
                handler1.sendMessage(msg);
            }).start();

        });

        Button startHandlerThreadButton = findViewById(R.id.startHandlerThreadButton);
        startHandlerThreadButton.setOnClickListener(v -> {
            HandlerThread handlerThread = new HandlerThread("BackgroundThread");
            handlerThread.start();
            Handler backgroundHandler = new Handler(handlerThread.getLooper());

            backgroundHandler.post(() -> {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> textView.setText("Updated from HandlerThread"));
            });
        });
    }
}