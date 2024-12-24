package nure.ananiev.nestorian.lab_task5.adapters;

import android.net.Uri;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nure.ananiev.nestorian.lab_task5.R;
import nure.ananiev.nestorian.lab_task5.models.Note;

public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(Note note, View.OnClickListener clickListener, View.OnLongClickListener longClickListener) {
        TextView titleTextView = itemView.findViewById(R.id.title_text);
        TextView descriptionTextView = itemView.findViewById(R.id.description_text);
        TextView dateTextView = itemView.findViewById(R.id.creation_date);
        ImageView importanceImageView = itemView.findViewById(R.id.importance_image);
        ImageView attachedImageImageView = itemView.findViewById(R.id.attached_image);

        titleTextView.setVisibility(note.getTitle().isBlank() ? View.GONE : View.VISIBLE);
        descriptionTextView.setVisibility(note.getDescription().isBlank() ? View.GONE : View.VISIBLE);

        boolean hasImage = note.getAttachedImageUri() != null && !note.getAttachedImageUri().isEmpty();
        attachedImageImageView.setVisibility(hasImage ? View.VISIBLE : View.GONE);

        titleTextView.setText(note.getTitle());
        descriptionTextView.setText(note.getDescription());
        dateTextView.setText(note.getFormattedCreationDate());
        importanceImageView.setImageResource(note.getImportance().getIconResId());
        if (hasImage) {
            attachedImageImageView.setImageURI(Uri.parse(note.getAttachedImageUri()));
        }

        itemView.setOnClickListener(clickListener);
        itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(v.getContext());
        inflater.inflate(R.menu.note_menu, menu);
    }
}
