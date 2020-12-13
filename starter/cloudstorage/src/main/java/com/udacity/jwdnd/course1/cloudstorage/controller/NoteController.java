package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
public class NoteController {
    private final UserService userService;
    private final NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/note/createUpdate")
    public String createOrUpdateNote(@ModelAttribute("noteForm") Note noteForm, Authentication auth, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "notes");

        int userId = userService.getUser(auth.getName()).getUserId();
        if (noteForm.getNoteId() == null) {
            noteForm.setUserId(userId);
            int result = noteService.insert(noteForm);
            if (result > 0) {
                attributes.addFlashAttribute("successMessage", "Your note was successfully added.");
            } else {
                attributes.addFlashAttribute("errorMessage", "Something went wrong with note creation. Please try again!");
            }
        } else {
            Note note = noteService.getNote(noteForm.getNoteId());
            if (note != null && note.getUserId() == userId) {
                int result = noteService.update(noteForm);
                if (result > 0) {
                    attributes.addFlashAttribute("successMessage", "Your note was successfully updated.");
                } else {
                    attributes.addFlashAttribute("errorMessage", "Something went wrong with note update. Please try again!");
                }
            } else {
                attributes.addFlashAttribute("errorMessage", "Something went wrong with note update. Please try again!");
            }
        }

        return "redirect:/result";
    }

    @GetMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable int noteId, Authentication auth, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "notes");
        Note note = noteService.getNote(noteId);
        if (note != null) {
            int userId = userService.getUser(auth.getName()).getUserId();
            if (userId == note.getUserId()) {
                noteService.delete(noteId);
                attributes.addFlashAttribute("successMessage", "Your note was successfully deleted.");
            } else {
                attributes.addFlashAttribute("errorMessage", "You don't have permissions to delete note.");
            }
        } else {
            attributes.addFlashAttribute("errorMessage", "Error while deleting note. Please try again!");
        }

        return "redirect:/result";
    }
}
