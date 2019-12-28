package org.example.service;

import javassist.NotFoundException;
import org.example.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    List<Photo> findAllPhotos();

    void savePhoto(Photo photo, MultipartFile file);

    Photo findPhotoById(Long photoId) throws NotFoundException;

    void deletePhotoById(Long photoId);
}
