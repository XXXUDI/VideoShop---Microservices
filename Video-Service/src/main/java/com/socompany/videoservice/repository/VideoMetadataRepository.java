package com.socompany.videoservice.repository;

import com.socompany.videoservice.entity.VideoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoMetadataRepository extends JpaRepository<VideoMetadata, Long> {

}
