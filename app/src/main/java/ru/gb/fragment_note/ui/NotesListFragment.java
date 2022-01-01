package ru.gb.fragment_note.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.gb.fragment_note.recyclerview.NotesListAdapter;
import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.fragment_note.repository.Notes;
import ru.gb.notes.R;

public class NotesListFragment extends Fragment implements NotesListAdapter.OnNoteClickListener, Constants {

    private final Notes notes = Notes.getInstance();

    private NotesListAdapter notesViewerAdapter;
    private NotesListController notesListController;

    interface NotesListController {
        void startNoteEdit(Note note);
    }

    public void notifyDataSetChanged() {
        notesViewerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof NotesListController)
            this.notesListController = (NotesListController) context;
        else
            throw new IllegalStateException("Activity must implement NotesListController");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View notesViewFragment = inflater.inflate(R.layout.notes_list_fragment, container, false);
        notesViewerAdapter = new NotesListAdapter();
        notesViewerAdapter.setNotes(notes);
        notesViewerAdapter.setOnNoteClickListener(this);
        RecyclerView notesViewer = notesViewFragment.findViewById(R.id.notes_viewer);
        notesViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        notesViewer.setAdapter(notesViewerAdapter);
        return notesViewFragment;
    }

    @Override
    public void onDetach() {
        notesListController = null;
        super.onDetach();
    }

    @Override
    public void onNoteClick(Note note) {
        notesListController.startNoteEdit(note);
    }
}
