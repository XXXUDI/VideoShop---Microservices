package com.socompany.videoservice.controller;

import com.socompany.videoservice.dto.VideoRequestDto;
import com.socompany.videoservice.dto.VideoResponseDto;
import com.socompany.videoservice.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


// TODO: Розділити відправку відео на метаданні і JSON file.

@RestController
@RequestMapping("/v1/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/get")
    public ResponseEntity<VideoResponseDto> getVideoById(@RequestParam long videoId) {

        var result = videoService.getVideo(videoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<VideoResponseDto> uploadVideo(@RequestParam("file") MultipartFile file, @RequestBody VideoRequestDto videoRequestDto, @RequestParam long videoId) throws IOException {
        videoService.saveVideo(videoRequestDto, file);

        var result = videoService.getVideo(videoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update")
    public ResponseEntity<VideoResponseDto> updateVideo(@RequestBody VideoRequestDto videoRequestDto, @RequestParam("file") MultipartFile file, @RequestParam long videoId) throws IOException {
        videoService.updateVideo(videoRequestDto, file, videoId);

        var result = videoService.getVideo(videoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteVideo(@RequestParam long videoId) throws IOException {
        if(videoService.deleteVideo(videoId)) {
            return ResponseEntity.ok("Video deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video not found");
        }
    }



}
