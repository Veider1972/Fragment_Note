package ru.gb.fragment_note.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.gb.notes.R;
import ru.gb.fragment_note.repository.Note;

public class NotesListHolder extends RecyclerView.ViewHolder {

    private final TextView titleView;
    private final TextView descriptionView;
    private Note note;

    public NotesListHolder(@NonNull View itemView, NotesListAdapter.OnNoteClickListener onNoteClickListener) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title);
        descriptionView = itemView.findViewById(R.id.description);
        itemView.setOnClickListener(view -> {
            if (onNoteClickListener != null)
                onNoteClickListener.onNoteClick(note);
        });
    }


    void bind(Note note)
    {
        this.note = note;
        titleView.setText(note.getTitle());
        descriptionView.setText(note.getDescription());
    }

}
