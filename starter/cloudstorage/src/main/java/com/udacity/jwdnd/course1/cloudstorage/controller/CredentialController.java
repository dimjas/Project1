package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/credentials")
    public String notesView(Authentication auth, Model model) {
        int userId = userService.getUser(auth.getName()).getUserId();
        model.addAttribute("credentials", credentialService.getCredentialsForUser(userId));
        return "credentials";
    }

    @PostMapping("/credential/createUpdate")
    public String createOrUpdateCredential(@ModelAttribute("credentialForm") Credential credentialForm, Authentication auth,
                                           RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "credentials");

        int userId = userService.getUser(auth.getName()).getUserId();
        if (credentialForm.getCredentialId() == null) {
            credentialForm.setUserId(userId);

            int result = credentialService.insert(credentialForm);
            if (result > 0) {
                attributes.addFlashAttribute("successMessage", "Your credential was successfully added.");
            } else {
                attributes.addFlashAttribute("errorMessage", "Something went wrong with credential creation. Please try again!");
            }
        } else {
            Credential credential = getCredential(credentialForm.getCredentialId());
            if (credential != null && credential.getUserId() == userId) {
                int result = credentialService.update(credentialForm);
                if (result > 0) {
                    attributes.addFlashAttribute("successMessage", "Your credential was successfully updated.");
                } else {
                    attributes.addFlashAttribute("errorMessage", "Something went wrong with credential update. Please try again!");
                }
            } else {
                attributes.addFlashAttribute("errorMessage", "Something went wrong with credential update. Please try again!");
            }
        }

        return "redirect:/result";
    }

    @GetMapping("/credential/{credentialId}")
    @ResponseBody
    public Credential getCredential(@PathVariable int credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteNote(@PathVariable int credentialId, Authentication auth, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "credentials");
        Credential credential = credentialService.getCredential(credentialId);
        if (credential != null) {
            int userId = userService.getUser(auth.getName()).getUserId();
            if (userId == credential.getUserId()) {
                credentialService.delete(credentialId);
                attributes.addFlashAttribute("successMessage", "Your credential was successfully deleted.");
            } else {
                attributes.addFlashAttribute("errorMessage", "You don't have permissions to delete credential.");
            }
        } else {
            attributes.addFlashAttribute("errorMessage", "Error while deleting credential. Please try again!");
        }

        return "redirect:/result";
    }
}
