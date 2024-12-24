package nure.ananiev.nestorian.lab_task5.fragments.addEditNote;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;

import nure.ananiev.nestorian.lab_task5.enums.Importance;
import nure.ananiev.nestorian.lab_task5.models.Note;
import nure.ananiev.nestorian.lab_task5.repositories.NotesRepository;

public class AddEditNoteViewModel extends AndroidViewModel {
    private final MutableLiveData<Note> noteLiveData = new MutableLiveData<>();
    private Note currentNote;
    private final NotesRepository notesRepository;

    public AddEditNoteViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application);
    }


    public LiveData<Note> getNote() {
        return noteLiveData;
    }

    public void loadNote(int noteId) {
        if (noteId != -1) {
            currentNote = notesRepository.getNoteById(noteId);
        } else {
            currentNote = new Note(-1, new Date(), "", "", new Date(), Importance.LOW, null);
        }
        noteLiveData.setValue(currentNote);
    }

    public void updateTitleAndDescription(String newTitle, String newDescription) {
        if (currentNote != null) {
            currentNote.setTitle(newTitle);
            currentNote.setDescription(newDescription);
            noteLiveData.setValue(currentNote);
        }
    }

    public void toggleImportance() {
        if (currentNote != null) {
            Importance newImportance = Importance.values()[(currentNote.getImportance().ordinal() + 1) % Importance.values().length];
            currentNote.setImportance(newImportance);
            noteLiveData.setValue(currentNote);
        }
    }

    public void updatePurposeDate(Date newDate) {
        if (currentNote != null) {
            currentNote.setPurposeDate(newDate);
            noteLiveData.setValue(currentNote);
        }
    }

    public int saveNote(String title, String description, Uri attachedImageUri) {
        if (currentNote != null && !(title + description).isEmpty()) {
            currentNote.setTitle(title);
            currentNote.setDescription(description);
            if (attachedImageUri != null) {
                currentNote.setAttachedImageUri(attachedImageUri.toString());
            }

            if (currentNote.getId() == -1) {
                currentNote = notesRepository.addNote(
                        currentNote.getTitle(),
                        currentNote.getDescription(),
                        currentNote.getPurposeDate(),
                        currentNote.getImportance(),
                        currentNote.getAttachedImageUri()
                );
            } else {
                notesRepository.updateNote(currentNote);
            }
            return currentNote.getId();
        }
        return -1;
    }
}

