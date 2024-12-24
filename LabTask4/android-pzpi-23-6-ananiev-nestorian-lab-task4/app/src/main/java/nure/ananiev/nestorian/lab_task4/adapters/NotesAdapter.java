package nure.ananiev.nestorian.lab_task4.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nure.ananiev.nestorian.lab_task4.R;
import nure.ananiev.nestorian.lab_task4.interfaces.NoteOnClickListener;
import nure.ananiev.nestorian.lab_task4.models.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private List<Note> notes = new ArrayList<>();
    private int longClickedNoteId = -1;
    private final NoteOnClickListener noteOnClickListener;

    public NotesAdapter(NoteOnClickListener noteOnClickListener) {
        this.noteOnClickListener = noteOnClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public int getLongClickedNoteId() {
        return longClickedNoteId;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note, v -> noteOnClickListener.onNoteClick(note.getId()), v -> {
            longClickedNoteId = note.getId();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
