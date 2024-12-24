package nure.ananiev.nestorian.lab_task5.fragments.addEditNote;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import nure.ananiev.nestorian.lab_task5.R;

public class AddEditNoteFragment extends Fragment {
    private static final String NOTE_ID_ARG = "NOTE_ID";
    private static final int DEFAULT_NOTE_ID = -1;
    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private EditText titleEditText, descriptionEditText;
    private TextView purposeDateTextView;
    private ImageView importanceImageView, attachedImageImageView;
    private TextView importanceTextView;
    private Uri attachedImageUri;
    private int savedNoteId = DEFAULT_NOTE_ID;

    private AddEditNoteViewModel viewModel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public static AddEditNoteFragment newInstance(Integer noteId) {
        AddEditNoteFragment fragment = new AddEditNoteFragment();
        Bundle args = new Bundle();
        if (noteId != null) {
            args.putInt(NOTE_ID_ARG, noteId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int noteId = getArguments() != null && !getArguments().isEmpty() ?
                getArguments().getInt(NOTE_ID_ARG, DEFAULT_NOTE_ID) : savedInstanceState != null ?
                savedInstanceState.getInt(NOTE_ID_ARG, DEFAULT_NOTE_ID) : DEFAULT_NOTE_ID;

        viewModel = new ViewModelProvider(this).get(AddEditNoteViewModel.class);
        viewModel.loadNote(noteId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_note, container, false);
        setupUI(view);
        observeViewModel();
        return view;
    }

    private void setupUI(View view) {
        titleEditText = view.findViewById(R.id.title_edit_text);
        descriptionEditText = view.findViewById(R.id.description_edit_text);
        purposeDateTextView = view.findViewById(R.id.purpose_date);
        ImageButton editPurposeDateButton = view.findViewById(R.id.edit_purpose_date_button);
        importanceImageView = view.findViewById(R.id.importance_image);
        importanceTextView = view.findViewById(R.id.importance_title);
        attachedImageImageView = view.findViewById(R.id.attached_image);

        editPurposeDateButton.setOnClickListener(v -> {
            viewModel.updateTitleAndDescription(titleEditText.getText().toString(), descriptionEditText.getText().toString());
            showDateTimePicker();
        });
        view.findViewById(R.id.importance_block).setOnClickListener(v -> {
            viewModel.updateTitleAndDescription(titleEditText.getText().toString(), descriptionEditText.getText().toString());
            viewModel.toggleImportance();
        });
        attachedImageImageView.setOnClickListener(v -> selectImageFromGallery());
    }

    private void observeViewModel() {
        viewModel.getNote().observe(getViewLifecycleOwner(), note -> {
            if (note != null) {
                if (!titleEditText.getText().toString().equals(note.getTitle())) {
                    titleEditText.setText(note.getTitle());
                }
                if (!descriptionEditText.getText().toString().equals(note.getDescription())) {
                    descriptionEditText.setText(note.getDescription());
                }
                purposeDateTextView.setText(note.getFormattedPurposeDate());
                importanceImageView.setImageResource(note.getImportance().getIconResId());
                importanceTextView.setText(note.getImportance().getTitleRes());

                if (note.getAttachedImageUri() != null && !note.getAttachedImageUri().isEmpty()) {
                    attachedImageUri = Uri.parse(note.getAttachedImageUri());
                    attachedImageImageView.setImageURI(attachedImageUri);
                }
            }
        });
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            showPurposeTime(hourOfDay, minute);
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            String dateString = addHeadingZeroes(dayOfMonth) + "." + addHeadingZeroes(monthOfYear + 1) + "." + year;
            purposeDateTextView.setText(dateString);
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showPurposeTime(int hourOfDay, int minute) {
        String time = addHeadingZeroes(hourOfDay) + ":" + addHeadingZeroes(minute);
        purposeDateTextView.setText(purposeDateTextView.getText().toString().split(" ")[0] + " " + time);
        Date newPurposeDate = parseDateTime(purposeDateTextView.getText().toString());
        viewModel.updatePurposeDate(newPurposeDate);
    }

    private Date parseDateTime(String dateTime) {
        dateFormat.setTimeZone(TimeZone.getDefault());
        try {
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            return new Date();
        }
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

    /**
     * @noinspection deprecation
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            attachedImageUri = copyImageToLocalFile(data.getData());
            attachedImageImageView.setImageURI(attachedImageUri);
        }
    }

    /**
     * @noinspection CallToPrintStackTrace
     */
    private Uri copyImageToLocalFile(Uri imageUri) {
        Uri localFileUri = null;
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);

            String randomFileName = "image_" + UUID.randomUUID().toString() + ".jpg";

            File file = new File(requireContext().getFilesDir(), randomFileName);
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
    public void onStop() {
        super.onStop();
        savedNoteId = viewModel.saveNote(titleEditText.getText().toString(), descriptionEditText.getText().toString(), attachedImageUri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (savedNoteId != -1) {
            outState.putInt(NOTE_ID_ARG, savedNoteId);
        }
    }
}

