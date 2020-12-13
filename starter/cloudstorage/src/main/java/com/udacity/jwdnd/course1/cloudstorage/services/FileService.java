package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFilesForUser(Integer userId) {
        return fileMapper.getFilesForUser(userId);
    }

    public File getFile(Integer fileId) {
        return fileMapper.getFile(fileId);
    }

    public boolean fileExistWithNameForUser(String fileName, Integer userId) {
        return fileMapper.getFilesWithNameForUser(fileName, userId).size() > 0;
    }

    public int insert(File file) {
        return fileMapper.insert(file);
    }

    public void delete(Integer fileId) {
        fileMapper.delete(fileId);
    }
}
