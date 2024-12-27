package com.socompany.videoservice.service;

import org.hibernate.boot.Metadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class VideoStoreService {

    public final String VIDEO_STORAGE_DIRECTORY_PATH = "C:\\Users\\sasha\\OneDrive\\Documents\\VideoCopilot";

    public String saveVideo(MultipartFile multipartFile, String videoName) throws IOException {

        Path directoryPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH);
        if(!Files.exists(directoryPath)) {
            Files.createDirectory(directoryPath);
        }

        Path videoPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH + videoName);
        if(!Files.exists(videoPath)) {
            multipartFile.transferTo(videoPath);
            return videoPath.toString();
        } else {
            throw new IOException("Video already exists");
        }
    }

    public Optional<String> getVideo(String videoName) throws IOException {
        Path directoryPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH);
        if(!Files.exists(directoryPath)) {
            return Optional.empty();
        }

        Path videoPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH + videoName);
        return Optional.of(videoPath.toString());
    }

    public boolean deleteVideo(String videoName) throws IOException {
        Path directoryPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH);
        Path videoPath = Path.of(VIDEO_STORAGE_DIRECTORY_PATH + videoName);
        if(!Files.exists(videoPath) || !Files.exists(directoryPath)) {
            return false;
        }
        Files.delete(videoPath);
        return true;
    }
}
