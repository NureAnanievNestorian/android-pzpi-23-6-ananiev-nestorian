package nure.ananiev.nestorian.lab_task3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.math.BigDecimal;
import java.math.RoundingMode;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private BigDecimal currentValue = BigDecimal.ZERO;
    private BigDecimal lastValue = BigDecimal.ZERO;
    private boolean isLastOperationClicked, isError;
    private MathOperation currentOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        resultTextView = findViewById(R.id.result_text);
    }

    public void onDigitClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        String currentText = resultTextView.getText().toString();

        if (isLastOperationClicked || currentText.equals("0") || isError) {
            currentText = "";
        }
        isError = false;

        currentText += buttonText;
        resultTextView.setText(currentText);
        currentValue = new BigDecimal(currentText);
        isLastOperationClicked = false;
    }

    public void onPlusClick(View view) {
        onOperationClick(MathOperation.PLUS);
    }

    public void onMinusClick(View view) {
        onOperationClick(MathOperation.MINUS);
    }

    public void onMultiplyClick(View view) {
        onOperationClick(MathOperation.MULTIPLY);
    }

    public void onDivideClick(View view) {
        onOperationClick(MathOperation.DIVIDE);
    }

    private void onOperationClick(MathOperation operation) {
        if (isError) {
            return;
        }
        if (!isLastOperationClicked) {
            lastValue = currentValue;
            isLastOperationClicked = true;
        }
        currentOperation = operation;
    }

    public void onEqualsClick(View view) {
        if (currentOperation == null || isError) {
            return;
        }

        switch (currentOperation) {
            case PLUS:
                currentValue = lastValue.add(currentValue);
                break;
            case MINUS:
                currentValue = lastValue.subtract(currentValue);
                break;
            case DIVIDE:
                if (currentValue.compareTo(BigDecimal.ZERO) == 0) {
                    resultTextView.setText(R.string.error);
                    currentValue = BigDecimal.ZERO;
                    currentOperation = null;
                    isError = true;
                    return;
                }
                currentValue = lastValue.divide(currentValue, 10, RoundingMode.HALF_UP);
                break;
            case MULTIPLY:
                currentValue = lastValue.multiply(currentValue);
                break;
        }

        resultTextView.setText(formatResult(currentValue));
        currentOperation = null;
    }

    @SuppressLint("DefaultLocale")
    private String formatResult(BigDecimal result) {
        return result.stripTrailingZeros().toPlainString();
    }

    public void onClearClick(View view) {
        resultTextView.setText("0");
        currentValue = BigDecimal.ZERO;
        lastValue = BigDecimal.ZERO;
        currentOperation = null;
        isLastOperationClicked = false;
    }

    public void onDotClick(View view) {
        if (isError) {
            return;
        }
        String currentText = resultTextView.getText().toString();

        if (isLastOperationClicked) {
            resultTextView.setText("0.");
            isLastOperationClicked = false;
        } else if (!currentText.contains(".")) {
            resultTextView.setText(currentText + ".");
        }
    }

    public void onChangeSignClick(View view) {
        if (isError) {
            return;
        }
        String currentText = resultTextView.getText().toString();

        if (!currentText.isEmpty() && !currentText.equals("0")) {
            if (currentText.startsWith("-")) {
                currentText = currentText.substring(1);
            } else {
                currentText = "-" + currentText;
            }
            resultTextView.setText(currentText);
            currentValue = new BigDecimal(currentText);
        }
    }

    public void onBackspaceClick(View view) {
        if (isError) {
            resultTextView.setText("0");
            isError = false;
            return;
        }

        String currentText = resultTextView.getText().toString();

        if (!currentText.isEmpty()) {
            currentText = currentText.substring(0, currentText.length() - 1);
            if (currentText.isEmpty() || currentText.equals("-")) {
                currentText = "0";
            }
            resultTextView.setText(currentText);
            currentValue = new BigDecimal(currentText);
        }
    }
}