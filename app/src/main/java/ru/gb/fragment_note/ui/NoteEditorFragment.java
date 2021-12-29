package ru.gb.fragment_note.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.notes.R;

public class NoteEditorFragment extends Fragment implements Constants {

        private TextInputEditText titleTextInputEditText;
    private TextInputEditText descriptionTextInputEditText;
    private MaterialButton acceptButton;
    private MaterialButton deleteButton;
    private MaterialButton cancelButton;

    public void setNote(Note note) {
        this.note = note;
    }

    private Note note;
    private NoteEditorController noteEditorController;

    interface NoteEditorController {
        void resultNoteEdit(EditResult result, Note note);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof NotesListFragment.NotesListController)
            this.noteEditorController = (NoteEditorController) context;
        else
            throw new IllegalStateException("Activity must implement NoteEditorController");
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View noteEditorFragment = inflater.inflate(R.layout.note_editor_fragment,container,false);
        titleTextInputEditText = noteEditorFragment.findViewById(R.id.title_text_editor);
        descriptionTextInputEditText = noteEditorFragment.findViewById(R.id.description_text_editor);
        acceptButton = noteEditorFragment.findViewById(R.id.accept_button);
        deleteButton = noteEditorFragment.findViewById(R.id.delete_button);
        cancelButton = noteEditorFragment.findViewById(R.id.cancel_button);
        acceptButton.setOnClickListener(view -> {
            note.setTitle(titleTextInputEditText.getText().toString());
            note.setDescription(descriptionTextInputEditText.getText().toString());
            noteEditorController.resultNoteEdit(EditResult.RESULT_UPDATE, note);
            //getActivity().onBackPressed();

        });
        cancelButton.setOnClickListener(view -> {
            noteEditorController.resultNoteEdit(EditResult.RESULT_CANCEL, null);
        });
        deleteButton.setOnClickListener(view ->{
            noteEditorController.resultNoteEdit(EditResult.RESULT_DELETE, note);
            //getActivity().onBackPressed();
        });

        if (note != null){
            titleTextInputEditText.setText(note.getTitle());
            descriptionTextInputEditText.setText(note.getDescription());
        }

        return noteEditorFragment;
    }
}
