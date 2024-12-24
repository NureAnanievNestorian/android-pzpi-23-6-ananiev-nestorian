package nure.ananiev.nestorian.lab_task5.fragments.notes;


import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;


import nure.ananiev.nestorian.lab_task5.R;
import nure.ananiev.nestorian.lab_task5.adapters.NotesAdapter;
import nure.ananiev.nestorian.lab_task5.enums.Importance;
import nure.ananiev.nestorian.lab_task5.fragments.addEditNote.AddEditNoteFragment;
import nure.ananiev.nestorian.lab_task5.fragments.settings.SettingsFragment;
import nure.ananiev.nestorian.lab_task5.interfaces.NoteOnClickListener;

public class NotesFragment extends Fragment implements NoteOnClickListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private TextView noNotesTextView;
    private SearchView searchView;

    private NotesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.notes_recycler);
        noNotesTextView = view.findViewById(R.id.no_notes_text);

        viewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        initAdapter();
        observeNotes();
        addMenu();
        viewModel.updateFilteredNotes();

        return view;
    }

    private void initAdapter() {
        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void observeNotes() {
        viewModel.getFilteredNotes().observe(getViewLifecycleOwner(), notes -> {
            adapter.setNotes(notes);
            noNotesTextView.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    private void addMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);

                MenuItem searchItem = menu.findItem(R.id.search_note_button);
                SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
                searchView = (SearchView) searchItem.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
                searchView.setMaxWidth(Integer.MAX_VALUE);

                searchView.setOnSearchClickListener(v -> menu.findItem(R.id.add_note_button).setVisible(false));

                searchView.setOnCloseListener(() -> {
                    menu.findItem(R.id.add_note_button).setVisible(true);
                    viewModel.setSearchQuery("");
                    return false;
                });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        viewModel.setSearchQuery(newText);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.add_note_button) {
                    openAddEditNoteFragment();
                    return true;
                } else if (id == R.id.filter_importance_all) {
                    viewModel.setImportanceFilter(null);
                    return true;
                } else if (id == R.id.filter_importance_high) {
                    viewModel.setImportanceFilter(Importance.HIGH);
                    return true;
                } else if (id == R.id.filter_importance_medium) {
                    viewModel.setImportanceFilter(Importance.MEDIUM);
                    return true;
                } else if (id == R.id.filter_importance_low) {
                    viewModel.setImportanceFilter(Importance.LOW);
                    return true;
                } else if (id == R.id.settings) {
                    openSettingsFragment();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void openAddEditNoteFragment() {
        openAddEditNoteFragment(null);
    }

    private void openAddEditNoteFragment(Integer noteId) {
        getParentFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.current_fragment, AddEditNoteFragment.newInstance(noteId))
                .addToBackStack(null)
                .commit();
    }

    private void openSettingsFragment() {
        getParentFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.current_fragment, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int noteId = adapter.getLongClickedNoteId();

        int itemId = item.getItemId();
        if (itemId == R.id.edit_note) {
            openAddEditNoteFragment(noteId);
            return true;
        } else if (itemId == R.id.delete_note) {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.note_delete_confirmation)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        viewModel.deleteNoteById(noteId);
                    })
                    .create()
                    .show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onNoteClick(int noteId) {
        openAddEditNoteFragment(noteId);
    }

}