package com.socompany.videoservice.mapper;

import com.socompany.videoservice.dto.VideoResponseDto;
import com.socompany.videoservice.entity.VideoMetadata;
import org.springframework.stereotype.Component;

@Component
public class VideoResponseMapper implements Mapper<VideoMetadata, VideoResponseDto>{
    @Override
    public VideoResponseDto map(VideoMetadata fromObject) {
        return VideoResponseDto.builder()
                .id(fromObject.getId())
                .title(fromObject.getTitle())
                .description(fromObject.getDescription())
                .price(fromObject.getPrice())
                .size(fromObject.getSize())
                .format(fromObject.getFormat())
                .duration(fromObject.getDuration())
                .videoUrl(fromObject.getUrl())
                .build();
    }
}
