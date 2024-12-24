package nure.ananiev.nestorian.lab_task5.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nure.ananiev.nestorian.lab_task5.db.DBHelper;
import nure.ananiev.nestorian.lab_task5.enums.Importance;
import nure.ananiev.nestorian.lab_task5.models.Note;

public class NotesRepository {

    private final DBHelper dbHelper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public NotesRepository(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION));
            String creationDateString = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CREATION_DATE));
            String purposeDateString = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PURPOSE_DATE));
            Importance importance = Importance.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMPORTANCE)));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_URI));

            try {
                Date creationDate = dateFormat.parse(creationDateString);
                Date purposeDate = dateFormat.parse(purposeDateString);
                cursor.close();
                db.close();
                return new Note(id, creationDate, title, description, purposeDate, importance, imageUri);
            } catch (ParseException ignored) {
            }
        }

        if (cursor != null) cursor.close();
        db.close();
        return null;
    }

    public Note addNote(String title, String description, Date purposeDate, Importance importance, String attachedImageUri) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_TITLE, title);
        values.put(DBHelper.COLUMN_DESCRIPTION, description);
        values.put(DBHelper.COLUMN_CREATION_DATE, dateFormat.format(new Date()));
        values.put(DBHelper.COLUMN_PURPOSE_DATE, dateFormat.format(purposeDate));
        values.put(DBHelper.COLUMN_IMPORTANCE, importance.name());
        values.put(DBHelper.COLUMN_IMAGE_URI, attachedImageUri);

        long id = db.insert(DBHelper.TABLE_NAME, null, values);
        db.close();

        return getNoteById((int) id);
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_TITLE, note.getTitle());
        values.put(DBHelper.COLUMN_DESCRIPTION, note.getDescription());
        values.put(DBHelper.COLUMN_PURPOSE_DATE, dateFormat.format(note.getPurposeDate()));
        values.put(DBHelper.COLUMN_IMPORTANCE, note.getImportance().name());
        values.put(DBHelper.COLUMN_IMAGE_URI, note.getAttachedImageUri());

        db.update(DBHelper.TABLE_NAME, values, DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(note.getId())});
        db.close();
    }

    public void removeNote(int noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    public List<Note> searchNotes(String query, Importance importance) {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "";
        List<String> selectionArgsList = new ArrayList<>();

        if (!query.isEmpty()) {
            selection = DBHelper.COLUMN_TITLE + " LIKE ? OR " + DBHelper.COLUMN_DESCRIPTION + " LIKE ?";
            selectionArgsList.add("%" + query + "%");
            selectionArgsList.add("%" + query + "%");
        }

        if (importance != null) {
            selection += (selection.isEmpty() ? "" : " AND ") + DBHelper.COLUMN_IMPORTANCE + " = ?";
            selectionArgsList.add(importance.name());
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, selection, selectionArgs, null, null, DBHelper.COLUMN_CREATION_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION));
                String creationDateString = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CREATION_DATE));
                String purposeDateString = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PURPOSE_DATE));
                Importance importanceValue = Importance.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMPORTANCE)));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_URI));

                try {
                    Date creationDate = dateFormat.parse(creationDateString);
                    Date purposeDate = dateFormat.parse(purposeDateString);
                    notes.add(new Note(id, creationDate, title, description, purposeDate, importanceValue, imageUri));
                } catch (ParseException ignored) {
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notes;
    }
}

