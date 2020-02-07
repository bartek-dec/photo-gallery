package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.exception.NotFoundException;
import org.example.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final AlbumService albumService;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, AlbumService albumService) {
        this.photoRepository = photoRepository;
        this.albumService = albumService;
    }

    @Override
    public List<Photo> findAllPhotos(Long albumId) {
        log.debug("I am in the PhotoServiceImpl findAllPhotos()");

        Album album = albumService.findAlbumById(albumId);

        if (album == null) {
            throw new NotFoundException();
        }

        List<Photo> photos = new ArrayList<>(album.getPhotos());
        Collections.sort(photos);
        return photos;
    }

    @Override
    public Photo savePhoto(Long albumId, MultipartFile file) {
        log.debug("I am in the PhotoServiceImpl savePhoto()");

        Album album = albumService.findAlbumById(albumId);

        if (album == null) {
            log.debug("Album with ID = " + albumId + " has not been found");
            throw new NotFoundException();
        }

        Photo photo = new Photo();

        if (!file.isEmpty()) {
            try {
                photo.setImage(file.getBytes());
                photo.setAlbum(album);
                album.getPhotos().add(photo);

                return photoRepository.save(photo);
            } catch (IOException e) {
                log.debug("Error occurred " + e);
                e.printStackTrace();
            }
        } else {
            log.debug("Failed to upload file");
        }
        return null;
    }

    @Override
    public Photo findPhotoById(Long photoId) {
        log.debug("I am in the PhotoServiceImpl findPhotoById()");

        Optional<Photo> photoOptional = photoRepository.findById(photoId);

        if (!photoOptional.isPresent()) {
            throw new NotFoundException();
        }
        return photoOptional.get();
    }

    @Override
    public void deletePhotoById(Long photoId) {
        log.debug("I am in the PhotoServiceImpl deletePhotoById()");

        photoRepository.deleteById(photoId);
    }
}
