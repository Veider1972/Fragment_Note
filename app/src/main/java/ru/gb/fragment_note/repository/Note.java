package ru.gb.fragment_note.repository;

import java.io.Serializable;

public class Note implements Serializable {
    private String title;
    private String description;
    private final int id;

    public int getId() {
        return id;
    }

    public Note(int id, String title, String description) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
