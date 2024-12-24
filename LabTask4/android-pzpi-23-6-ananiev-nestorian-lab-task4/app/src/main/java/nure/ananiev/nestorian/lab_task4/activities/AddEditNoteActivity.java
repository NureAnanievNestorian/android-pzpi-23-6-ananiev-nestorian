package nure.ananiev.nestorian.lab_task4.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import nure.ananiev.nestorian.lab_task4.enums.Importance;
import nure.ananiev.nestorian.lab_task4.models.Note;
import nure.ananiev.nestorian.lab_task4.repositories.NotesRepository;
import nure.ananiev.nestorian.lab_task4.R;

public class AddEditNoteActivity extends AppCompatActivity {

    public static String NOTE_ID_ARG = "NOTE_ID";
    private static final int DEFAULT_NOTE_ID = -1;

    private int noteId = DEFAULT_NOTE_ID;

    private EditText titleEditText, descriptionEditText;
    private TextView purposeDateTextView;
    private ImageButton editPurposeDateButton;
    private ImageView importanceImageView, attachedImageImageView;
    private TextView importanceTextView;
    private Date selectedPurposeDate = new Date();

    private Importance selectedImportance = Importance.LOW;
    private Uri attachedImageUri;

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm",
            Locale.getDefault()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);
        setupActionBar();
        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        purposeDateTextView = findViewById(R.id.purpose_date);
        editPurposeDateButton = findViewById(R.id.edit_purpose_date_button);
        importanceImageView = findViewById(R.id.importance_image);
        importanceTextView = findViewById(R.id.importance_title);
        attachedImageImageView = findViewById(R.id.attached_image);

        noteId = getIntent().getIntExtra(NOTE_ID_ARG, DEFAULT_NOTE_ID);

        setupPurposeDate();
        setupImportance();

        attachedImageImageView.setOnClickListener(v -> selectImageFromGallery());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isInEditMode()) {
            updateData();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupPurposeDate() {
        purposeDateTextView.setText(dateFormat.format(new Date()));
        editPurposeDateButton.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    showPurposeTime(hourOfDay, minute);
                    if (selectedPurposeDate.before(new Date())) {
                        showPurposeTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String dateString = addHeadingZeroes(dayOfMonth) + "." + addHeadingZeroes(monthOfYear + 1) + "." + year;
                            purposeDateTextView.setText(dateString);
                            timePickerDialog.show();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();


        });
    }

    private Date parseDateTime(String dateTime) {
        dateFormat.setTimeZone(TimeZone.getDefault());
        try {
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showPurposeTime(int hourOfDay, int minute) {
        String time = addHeadingZeroes(hourOfDay) + ":" + addHeadingZeroes(minute);
        purposeDateTextView.setText(
                purposeDateTextView.getText().toString().split(" ")[0] + " " + time
        );
        selectedPurposeDate = parseDateTime(purposeDateTextView.getText().toString());
    }

    private void setupImportance() {
        findViewById(R.id.importance_block).setOnClickListener(v -> {
            selectedImportance = Importance.values()[(selectedImportance.ordinal() + 1) % Importance.values().length];
            importanceImageView.setImageResource(selectedImportance.getIconResId());
            importanceTextView.setText(selectedImportance.getTitleRes());
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote();
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        if ((title + description).isBlank()) {
            return;
        }

        if (isInEditMode()) {
            Note note = NotesRepository.getNoteById(noteId);
            note.setTitle(title);
            note.setDescription(description);
            note.setPurposeDate(selectedPurposeDate);
            note.setImportance(selectedImportance);
            note.setAttachedImageUri(attachedImageUri == null ? null : attachedImageUri.toString());
            NotesRepository.updateNote(note);
        } else {
            Note savedNote = NotesRepository.addNote(title, description, selectedPurposeDate, selectedImportance, attachedImageUri == null ? null : attachedImageUri.toString());
            noteId = savedNote.getId();
        }
    }

    private boolean isInEditMode() {
        return noteId != DEFAULT_NOTE_ID;
    }

    private void updateData() {
        Note note = NotesRepository.getNoteById(noteId);
        titleEditText.setText(note.getTitle());
        descriptionEditText.setText(note.getDescription());
        purposeDateTextView.setText(note.getFormattedPurposeDate());
        importanceImageView.setImageResource(note.getImportance().getIconResId());

        if (note.getAttachedImageUri() != null && !note.getAttachedImageUri().isEmpty()) {
            attachedImageUri = Uri.parse(note.getAttachedImageUri());
            attachedImageImageView.setImageURI(attachedImageUri);
        }

        selectedImportance = note.getImportance();
    }

    private String addHeadingZeroes(int number) {
        return number > 9 ? Integer.toString(number) : "0" + number;
    }

    /**
     * @noinspection deprecation
     */
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private Uri copyImageToLocalFile(Uri imageUri) {
        Uri localFileUri = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            String randomFileName = "image_" + UUID.randomUUID().toString() + ".jpg";

            File file = new File(getFilesDir(), randomFileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            localFileUri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            attachedImageUri = copyImageToLocalFile(selectedImageUri);

            if (attachedImageUri != null) {
                attachedImageImageView.setImageURI(attachedImageUri);
            }
        }
    }
}