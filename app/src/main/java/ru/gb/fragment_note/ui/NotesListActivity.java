package ru.gb.fragment_note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Random;

import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.fragment_note.repository.Notes;
import ru.gb.notes.R;

public class NotesListActivity extends AppCompatActivity implements NotesListFragment.NotesListController, NoteEditorFragment.NoteEditorController, Constants {

    private final Notes notes = Notes.getInstance();
    private NotesListFragment notesListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        notesListFragment = new NotesListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.notes_list_fragment_container, notesListFragment)
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
                Toast.makeText(this,"Вызов настроек", Toast.LENGTH_LONG).show();
                break;
            case R.id.app_bar_search:
                Toast.makeText(this,"Поиск заметки", Toast.LENGTH_LONG).show();
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
        NoteEditorFragment noteEditorFragment = NoteEditorFragment.getInstance(note);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.note_editor_fragment_container, noteEditorFragment)
                .commit();
    }

    @Override
    public void resultNoteEdit(EditResult result, Note note, NoteEditorFragment noteEditorFragment) {
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
                .remove(noteEditorFragment)
                .commit();
    }
}
