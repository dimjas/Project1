package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int insert(Note note) {
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public int update(Note noteForm) {
        Note note = noteMapper.getNote(noteForm.getNoteId());
        note.setNoteTitle(noteForm.getNoteTitle());
        note.setNoteDescription(noteForm.getNoteDescription());
        return noteMapper.update(note);
    }

    public void delete(int noteId) {
        noteMapper.delete(noteId);
    }

    public Note getNote(int noteId) {
        return noteMapper.getNote(noteId);
    }

    public List<Note> getNotesForUser(int userId) {
        return noteMapper.getNotesForUser(userId);
    }
}
