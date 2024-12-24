package nure.ananiev.nestorian.lab_task5.fragments.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import nure.ananiev.nestorian.lab_task5.enums.Importance;
import nure.ananiev.nestorian.lab_task5.models.Note;
import nure.ananiev.nestorian.lab_task5.repositories.NotesRepository;

public class NotesViewModel extends AndroidViewModel {

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Importance> importanceFilter = new MutableLiveData<>(null);
    private final MutableLiveData<List<Note>> filteredNotes = new MutableLiveData<>();
    private final NotesRepository notesRepository;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        notesRepository = new NotesRepository(application);
    }

    public LiveData<List<Note>> getFilteredNotes() {
        return filteredNotes;
    }

    public void setSearchQuery(String query) {
        if (!query.equals(searchQuery.getValue())) {
            searchQuery.setValue(query);
            updateFilteredNotes();
        }
    }

    public void setImportanceFilter(Importance filter) {
        if (filter != importanceFilter.getValue()) {
            importanceFilter.setValue(filter);
            updateFilteredNotes();
        }
    }

    public void deleteNoteById(int noteId) {
        notesRepository.removeNote(noteId);
        updateFilteredNotes();
    }

    public void updateFilteredNotes() {
        String query = searchQuery.getValue();
        Importance filter = importanceFilter.getValue();
        List<Note> notes = notesRepository.searchNotes(query != null ? query : "", filter);
        filteredNotes.setValue(notes);
    }
}
