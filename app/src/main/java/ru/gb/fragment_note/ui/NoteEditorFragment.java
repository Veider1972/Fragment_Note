package ru.gb.fragment_note.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.gb.fragment_note.repository.Constants;
import ru.gb.fragment_note.repository.Note;
import ru.gb.notes.R;

public class NoteEditorFragment extends Fragment implements Constants {

    private Spinner spinner;
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText descriptionTextInputEditText;
    private TextView dateTextView;

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    private static final String IMAGE_KEY = "IMAGE_KEY";
    private static final String DATE_KEY = "DATE_KEY";

    private Note note;
    public void setNote(Note note) {
        this.note = note;
    }
    private NoteEditorController noteEditorController;

    interface NoteEditorController {
        void resultNoteEdit(EditResult result, Note note, NoteEditorFragment noteEditorFragment);
    }

    public static NoteEditorFragment getInstance(Note note) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(NOTE_KEY, note);
        NoteEditorFragment noteEditorFragment = new NoteEditorFragment();
        noteEditorFragment.setArguments(arguments);
        return noteEditorFragment;
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
        View noteEditorFragment = inflater.inflate(R.layout.note_editor_fragment, container, false);
        titleTextInputEditText = noteEditorFragment.findViewById(R.id.title_text_editor);
        descriptionTextInputEditText = noteEditorFragment.findViewById(R.id.description_text_editor);
        dateTextView = noteEditorFragment.findViewById(R.id.date);
        dateTextView.setOnClickListener(view -> {
            DatePickerDialog datePicker = new DatePickerDialog(this.getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar date = Calendar.getInstance();
                date.set(year, monthOfYear, dayOfMonth);
                note.setDate(date);
                dateTextView.setText(new SimpleDateFormat("d.MM.yy", Locale.getDefault()).format(note.getDate().getTime()));
            }, note.getDate().get(Calendar.YEAR), note.getDate().get(Calendar.MONTH), note.getDate().get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        spinner = noteEditorFragment.findViewById(R.id.spinner_image);
        assert this.getContext() != null;
        SpinnerAdapter adapter = new SpinnerAdapter(this.getContext());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                note.setPicture(images[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        MaterialButton acceptButton = noteEditorFragment.findViewById(R.id.accept_button);
        MaterialButton cancelButton = noteEditorFragment.findViewById(R.id.cancel_button);
        acceptButton.setOnClickListener(view -> {
            assert titleTextInputEditText.getText() != null;
            note.setTitle(titleTextInputEditText.getText().toString());
            assert descriptionTextInputEditText.getText() != null;
            note.setDescription(descriptionTextInputEditText.getText().toString());
            noteEditorController.resultNoteEdit(EditResult.RESULT_UPDATE, note, this);

        });
        cancelButton.setOnClickListener(view -> noteEditorController.resultNoteEdit(EditResult.RESULT_CANCEL, note, this));

        Bundle args = getArguments();
        if (savedInstanceState == null) {
            assert args != null;
            note = (Note) args.getSerializable(NOTE_KEY);
            titleTextInputEditText.setText(note.getTitle());
            descriptionTextInputEditText.setText(note.getDescription());
            dateTextView.setText(new SimpleDateFormat("d.MM.yy", Locale.getDefault()).format(note.getDate().getTime()));
            spinner.setSelection(getImageById(note.getPicture()));
        } else {
            titleTextInputEditText.setText(savedInstanceState.getString(TITLE_KEY));
            descriptionTextInputEditText.setText(savedInstanceState.getString(DESCRIPTION_KEY));
            dateTextView.setText(savedInstanceState.getString(DATE_KEY));
            spinner.setSelection((int) savedInstanceState.get(IMAGE_KEY));
            note = (Note) savedInstanceState.getSerializable(NOTE_KEY);
        }
        return noteEditorFragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        assert titleTextInputEditText.getText() != null;
        outState.putString(TITLE_KEY, titleTextInputEditText.getText().toString());
        assert descriptionTextInputEditText.getText() != null;
        outState.putString(DESCRIPTION_KEY, descriptionTextInputEditText.getText().toString());
        outState.putInt(IMAGE_KEY, spinner.getSelectedItemPosition());
        outState.putString(DATE_KEY, dateTextView.getText().toString());
        outState.putSerializable(NOTE_KEY, note);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        noteEditorController = null;
        super.onDetach();
    }

    static class SpinnerAdapter extends BaseAdapter implements Constants {

        private final LayoutInflater layoutInflater;

        public SpinnerAdapter(@NonNull Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.row_image, null);
            ImageView imageView = view.findViewById(R.id.image_icon);
            imageView.setImageResource(images[position]);
            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }
}
