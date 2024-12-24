package nure.ananiev.nestorian.lab_task4.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nure.ananiev.nestorian.lab_task4.enums.Importance;
import nure.ananiev.nestorian.lab_task4.interfaces.NoteOnClickListener;
import nure.ananiev.nestorian.lab_task4.models.Note;
import nure.ananiev.nestorian.lab_task4.adapters.NotesAdapter;
import nure.ananiev.nestorian.lab_task4.repositories.NotesRepository;
import nure.ananiev.nestorian.lab_task4.R;

public class MainActivity extends AppCompatActivity implements NoteOnClickListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private TextView noNotesTextView;
    private Importance importanceFilter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.notes_recycler);
        noNotesTextView = findViewById(R.id.no_notes_text);
        addMenu();
        initAdapter();
        registerForContextMenu(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Note> notes = NotesRepository.getAllNotes();
        adapter.setNotes(notes);
        noNotesTextView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void initAdapter() {
        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void addMenu() {
        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);

                MenuItem searchItem = menu.findItem(R.id.search_note_button);
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                searchView = (SearchView) searchItem.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setMaxWidth(Integer.MAX_VALUE);

                searchView.setOnSearchClickListener(v -> menu.findItem(R.id.add_note_button).setVisible(false));

                searchView.setOnCloseListener(() -> {
                    menu.findItem(R.id.add_note_button).setVisible(true);
                    adapter.setNotes(NotesRepository.getAllNotes());
                    return false;
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        searchNotes();
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.add_note_button) {
                    startAddEditNoteActivity();
                } else if (id == R.id.filter_importance_all) {
                    importanceFilter = null;
                    searchNotes();
                    return true;
                } else if (id == R.id.filter_importance_high) {
                    importanceFilter = Importance.HIGH;
                    searchNotes();
                    return true;
                } else if(id == R.id.filter_importance_medium) {
                    importanceFilter = Importance.MEDIUM;
                    searchNotes();
                    return true;
                } else if (id == R.id.filter_importance_low) {
                    importanceFilter = Importance.LOW;
                    searchNotes();
                    return true;
                }
                return false;
            }
        }, this);
    }

    private void startAddEditNoteActivity(int editingNoteId) {
        Intent intent = new Intent(this, AddEditNoteActivity.class);
        if (editingNoteId != -1) {
            intent.putExtra(AddEditNoteActivity.NOTE_ID_ARG, editingNoteId);
        }
        startActivity(intent);
    }

    private void searchNotes() {
        adapter.setNotes(NotesRepository.searchNotes(searchView.getQuery().toString(), importanceFilter));
    }

    private void startAddEditNoteActivity() {
        startAddEditNoteActivity(-1);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int noteId = adapter.getLongClickedNoteId();

        int itemId = item.getItemId();
        if (itemId == R.id.edit_note) {
            startAddEditNoteActivity(noteId);
            return true;
        } else if (itemId == R.id.delete_note) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.note_delete_confirmation)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        NotesRepository.removeNote(noteId);
                        adapter.setNotes(NotesRepository.getAllNotes());
                    })
                    .create()
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onNoteClick(int noteId) {
        startAddEditNoteActivity(noteId);
    }
}