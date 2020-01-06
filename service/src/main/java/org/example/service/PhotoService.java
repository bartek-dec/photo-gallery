package org.example.service;

import org.example.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    List<Photo> findAllPhotos(Long albumId);

    Photo savePhoto(Long albumId, MultipartFile file);

    Photo findPhotoById(Long photoId);

    void deletePhotoById(Long photoId);
}
