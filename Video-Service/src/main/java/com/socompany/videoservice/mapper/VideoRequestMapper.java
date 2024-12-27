package com.socompany.videoservice.mapper;

import com.socompany.videoservice.dto.VideoRequestDto;
import com.socompany.videoservice.entity.VideoMetadata;
import org.springframework.stereotype.Component;

@Component
public class VideoRequestMapper implements Mapper<VideoRequestDto, VideoMetadata>{
    @Override
    public VideoMetadata map(VideoRequestDto fromObject) {
        return VideoMetadata
                .builder()
                .title(fromObject.getTitle())
                .description(fromObject.getDescription())
                .price(fromObject.getPrice())
                .url(fromObject.getVideoUrl())
                .format(fromObject.getFormat())
                .size(fromObject.getSize())
                .duration(fromObject.getDuration())
                .build();
    }

    @Override
    public VideoMetadata map(VideoRequestDto fromObj, VideoMetadata toObj) {
        toObj.setTitle(fromObj.getTitle());
        toObj.setDescription(fromObj.getDescription());
        toObj.setPrice(fromObj.getPrice());
        toObj.setUrl(fromObj.getVideoUrl());
        toObj.setFormat(fromObj.getFormat());
        toObj.setSize(fromObj.getSize());
        toObj.setDuration(fromObj.getDuration());
        return toObj;
    }
}
