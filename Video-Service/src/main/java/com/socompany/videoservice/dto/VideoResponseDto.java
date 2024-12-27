package com.socompany.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoResponseDto {
    private long id;
    private String title;
    private String description;
    private String videoUrl;
    private float price;
    private String format;
    private long duration;
    private long size;


}
