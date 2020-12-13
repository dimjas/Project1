package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class FileController {
    private final UserService userService;
    private final FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/file/add")
    public String addFile(@RequestParam("fileUpload") MultipartFile multipartFile, Authentication auth, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "files");

        if (multipartFile.isEmpty()) {
            attributes.addFlashAttribute("errorMessage", "No file selected to upload!");
            return "redirect:/result";
        }

        int userId = userService.getUser(auth.getName()).getUserId();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (fileService.fileExistWithNameForUser(fileName, userId)) {
            attributes.addFlashAttribute("errorMessage", "File name already exists. Please choose another file to upload.");
            return "redirect:/result";
        }

        File file = new File();
        file.setFileName(fileName);
        file.setUserId(userId);
        file.setContentType(multipartFile.getContentType());
        file.setFileSize(Long.toString(multipartFile.getSize()));
        try {
            file.setFileData(multipartFile.getBytes());
            int result = fileService.insert(file);
            if(result > 0) {
                attributes.addFlashAttribute("successMessage", "Your file was successfully uploaded.");
            } else {
                attributes.addFlashAttribute("errorMessage", "Error uploading file. Please try again!");
            }
        } catch (IOException e) {
            attributes.addFlashAttribute("errorMessage", "Error uploading file. Please try again!");
        } finally {
            return "redirect:/result";
        }
    }

    @GetMapping("/file/view/{fileId}")
    public ResponseEntity downloadFile(@PathVariable int fileId) {
        File file = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/file/delete/{fileId}")
    public String deleteFile(@PathVariable int fileId, Authentication auth, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "files");
        File file = fileService.getFile(fileId);
        if (file != null) {
            int userId = userService.getUser(auth.getName()).getUserId();
            if (userId == file.getUserId()) {
                fileService.delete(fileId);
                attributes.addFlashAttribute("successMessage", "Your file was successfully deleted.");
            } else {
                attributes.addFlashAttribute("errorMessage", "You don't have permissions to delete file.");
            }
        } else {
            attributes.addFlashAttribute("errorMessage", "Error while deleting file. Please try again!");
        }

        return "redirect:/result";
    }
}
