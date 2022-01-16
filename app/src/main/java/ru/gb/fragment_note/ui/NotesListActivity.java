package ru.gb.fragment_note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Random;

import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.fragment_note.repository.Notes;
import ru.gb.notes.R;

public class NotesListActivity extends AppCompatActivity implements NoteEditorDialogFragment.NoteEditorController, NotesListFragment.NotesListController, Constants {

    private final Notes notes = Notes.getInstance();
    private NotesListFragment notesListFragment;
    private static NotesListActivity notesListActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesListActivity = this;
        setContentView(R.layout.notes_list);
        notesListFragment = new NotesListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, notesListFragment)
                .commit();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            notes.add("Заголовок " + i, "Текст заметки " + i, Calendar.getInstance(), images[rnd.nextInt(3)]);
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
                Note note = new Note(notes.getNewID(), "", "", Calendar.getInstance(), 0);
                startNoteEdit(note);
                break;
            case R.id.settings:
                Toast.makeText(this, "Вызов настроек", Toast.LENGTH_LONG).show();
                break;
            case R.id.app_bar_search:
                Toast.makeText(this, "Поиск заметки", Toast.LENGTH_LONG).show();
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
        NoteEditorDialogFragment noteEditorDialogFragment = new NoteEditorDialogFragment();
        noteEditorDialogFragment.setNote(note);
        noteEditorDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void resultNoteEdit(EditResult result, Note note, NoteEditorDialogFragment noteEditorDialogFragment) {
        switch (result) {
            case RESULT_UPDATE:
                if (note != null) {
                    notes.addOrUpdateNote(note);
                    notesListFragment.getNotesViewerAdapter().notifyItemChanged(notes.getIndexByID(note.getId()));
                }
                break;
            case RESULT_CANCEL:
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .remove(noteEditorDialogFragment)
                .commit();
    }

    boolean isCloseAccepted = false;

    @Override
    public void onBackPressed() {
        if (isCloseAccepted) {
            this.finish();
            return;
        }
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            assert notesListFragment.getView() != null;
            Snackbar snackbar = Snackbar.make(notesListFragment.getView(), R.string.main_activity_on_close_question, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.button_yes_text, view -> {
                isCloseAccepted = true;
                notesListActivity.onBackPressed();
            });
            snackbar.show();
        }
        if (backStackEntryCount > 0)
            super.onBackPressed();
    }
}