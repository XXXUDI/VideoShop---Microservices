package com.socompany.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoRequestDto {

    private String title;
    private String description;
    private String videoUrl;
    private float price;
    private String format;
    private long duration;
    private long size;

    public VideoRequestDto(String title, String description, float price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
