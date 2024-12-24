package nure.ananiev.nestorian.lab_task4.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nure.ananiev.nestorian.lab_task4.enums.Importance;
import nure.ananiev.nestorian.lab_task4.models.Note;

public class NotesRepository {

    private static int lastNoteId = 0;
    private static final List<Note> notes = new ArrayList<>();

    public static List<Note> getAllNotes() {
        return notes;
    }

    public static Note getNoteById(int id) {
        Optional<Note> note = notes.stream()
                .filter(n -> n.getId() == id)
                .findFirst();
        return note.orElse(null);
    }

    public static Note addNote(String title, String description, Date purposeDate, Importance importance, String attachedImageUri) {
        Date currentDate = new Date();
        Note note = new Note(lastNoteId++, currentDate, title, description, purposeDate, importance, attachedImageUri);
        notes.add(note);
        return note;
    }

    public static void updateNote(Note note) {
        int noteIndex = notes.indexOf(getNoteById(note.getId()));
        notes.set(noteIndex, note);
    }

    public static void removeNote(int noteId) {
        Note note = getNoteById(noteId);
        if (note != null) {
            notes.remove(noteId);
        }
    }

    public static List<Note> searchNotes(String query, Importance importance) {
        return notes.stream()
                .filter(n -> n.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        n.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(n -> importance == null || n.getImportance() == importance)
                .collect(Collectors.toList());
    }
}
