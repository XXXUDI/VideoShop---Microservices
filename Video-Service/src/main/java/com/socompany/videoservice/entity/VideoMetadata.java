package com.socompany.videoservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoMetadata {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String description;
    private float price;

    // Url to video
    private String url;
    private String format;
    private long duration;
    // File Size
    private long size;

}
