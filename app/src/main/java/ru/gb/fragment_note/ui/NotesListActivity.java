package ru.gb.fragment_note.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.fragment_note.repository.Notes;
import ru.gb.notes.R;

public class NotesListActivity extends AppCompatActivity implements NotesListFragment.NotesListController, NoteEditorFragment.NoteEditorController, Constants {

    private final Notes notes = Notes.getInstance();
    private NotesListFragment notesListFragment;
    private NoteEditorFragment noteEditorFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);

        notesListFragment = new NotesListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.notes_list_fragment_container, notesListFragment)
                //.addToBackStack(null)
                .commit();

        for (int i = 0; i < 10; i++) {
            notes.add("Заголовок " + i, "Текст заметки " + i);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if (notes.getSize()>0) {
                Note note = notes.getNote(0);
                noteEditorFragment = new NoteEditorFragment();
                noteEditorFragment.setNote(note);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.note_editor_fragment_container, noteEditorFragment)
                        .commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                Note note = new Note(notes.getNewID(), "", "");
                startNoteEdit(note);
                break;
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startNoteEdit(Note note) {
        noteEditorFragment = new NoteEditorFragment();
        noteEditorFragment.setNote(note);
        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.notes_list_fragment_container, noteEditorFragment)
                        .addToBackStack("Editor")
                        .commit();
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.note_editor_fragment_container, noteEditorFragment)
                        .commit();
                break;
        }
    }



    @Override
    public void resultNoteEdit(EditResult result, Note note) {
        int orientation = getResources().getConfiguration().orientation;
        switch (result) {
            case RESULT_UPDATE:
                if (note != null) {
                    notes.addOrUpdateNote(note);
                    notesListFragment.notifyDataSetChanged();
                }
                break;
            case RESULT_DELETE:
                if (note != null) {
                    notes.delete(note.getId());
                    notesListFragment.notifyDataSetChanged();
                }
                break;
            case RESULT_CANCEL:
                break;
        }
        switch (orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                this.onBackPressed();
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                getSupportFragmentManager().beginTransaction()
                        .remove(noteEditorFragment)
                        .commit();
                break;
        }
    }
}