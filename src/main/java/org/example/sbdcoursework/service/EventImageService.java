package org.example.sbdcoursework.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface EventImageService {

    String store(UUID eventUuid, MultipartFile image);

    void delete(String imageFilename);

    Resource loadAsResource(String imageFilename);
}
