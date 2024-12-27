package com.socompany.videoservice.service;

import com.socompany.core.event.VideoEvent;
import com.socompany.videoservice.dto.VideoRequestDto;
import com.socompany.videoservice.dto.VideoResponseDto;
import com.socompany.videoservice.entity.VideoMetadata;
import com.socompany.videoservice.mapper.VideoRequestMapper;
import com.socompany.videoservice.mapper.VideoResponseMapper;
import com.socompany.videoservice.repository.VideoMetadataRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Optional;


/*
    This class isn`t storing video, just makes some bl:
    1. Saves video (entity) to db.
    2. Send kafka messages (published, unpublished, removed, purchased)
    3. Returns VideoResponseDto

    --- Additional ---

    We set wrong duration, but we can include another libraries to realize getting true duration of video if needed.
 */

@Service
public class VideoService {

    @Autowired
    private VideoStoreService videoStoreService;

    @Autowired
    private VideoMetadataRepository videoMetadataRepository;

    @Autowired
    private VideoRequestMapper videoRequestMapper;

    @Autowired
    private KafkaTemplate<String, VideoEvent> kafkaTemplate;

    @Autowired
    private VideoResponseMapper videoResponseMapper;

    public VideoResponseDto saveVideo(VideoRequestDto videoRequestDto, MultipartFile multipartFile) throws IOException {
        try {
            videoRequestDto.setVideoUrl(videoStoreService.saveVideo(multipartFile, videoRequestDto.getTitle()));
        } catch (IOException e) {
            throw new FileAlreadyExistsException("Video already exists. Please remove it or update!");
        }

        videoRequestDto.setSize(multipartFile.getSize());
        videoRequestDto.setDuration(multipartFile.getSize()); // Wrong duration but we mock the duration as size

        VideoMetadata videoMetadata = videoMetadataRepository
                .save(videoRequestMapper.map(videoRequestDto));

        VideoEvent event = new VideoEvent(
                videoMetadata.getId(),
                videoMetadata.getTitle(),
                videoMetadata.getDescription(),
                videoMetadata.getPrice(),
                "published"
        );

        ProducerRecord<String, VideoEvent> record = new ProducerRecord<>(
                "video-published-events-topic",
                event
        );

        kafkaTemplate.send(record);

        return videoResponseMapper.map(videoMetadata);
    }

    public Optional<VideoResponseDto> getVideo(long videoId) {
        return videoMetadataRepository.findById(videoId).map(videoResponseMapper::map);
    }

    public boolean deleteVideo(long videoId) throws IOException {
        var videoMetadata = videoMetadataRepository.findById(videoId);

        if (videoMetadata.isPresent()) {
            videoStoreService.deleteVideo(videoMetadata.get().getTitle());
            videoMetadataRepository.deleteById(videoId);

            VideoEvent event = new VideoEvent(
                    videoMetadata.get().getId(),
                    videoMetadata.get().getTitle(),
                    videoMetadata.get().getDescription(),
                    videoMetadata.get().getPrice(),
                    "unpublished"
            );

            ProducerRecord<String, VideoEvent> record = new ProducerRecord<>(
                    "video-unpublished-events-topic",
                    event
            );

            kafkaTemplate.send(record);

            return true;
        } else {
            return false;
        }
    }

    public Optional<VideoResponseDto> updateVideo(VideoRequestDto videoRequestDto, MultipartFile multipartFile, long id) throws IOException {
        var oldVideoMetadata = videoMetadataRepository.findById(id);
        if (oldVideoMetadata.isPresent()) {
            videoStoreService.deleteVideo(oldVideoMetadata.get().getTitle());

            videoStoreService.saveVideo(multipartFile, videoRequestDto.getTitle());
        } else {
            throw new FileNotFoundException("Video does not exist.");
        }

        videoRequestDto.setSize(multipartFile.getSize());
        videoRequestDto.setDuration(multipartFile.getSize()); // Wrong duration but we mock the duration as size

        VideoResponseDto responseDto = oldVideoMetadata.map(videoMetadata -> videoRequestMapper.map(videoRequestDto, videoMetadata))
                .map(videoMetadataRepository::saveAndFlush).map(videoResponseMapper::map).orElseThrow(() -> new RuntimeException("Something went wrong!"));

        return Optional.of(responseDto);
    }

}
