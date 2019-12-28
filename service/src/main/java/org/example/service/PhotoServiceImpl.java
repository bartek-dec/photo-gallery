package org.example.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.Photo;
import org.example.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public List<Photo> findAllPhotos() {
        log.debug("I am in the PhotoServiceImpl findAllPhotos()");

        List<Photo> photos = new ArrayList<>();
        photoRepository.findAll().iterator().forEachRemaining(photos::add);

        return photos;
    }

    @Override
    public void savePhoto(Photo photo, MultipartFile file) {
        log.debug("I am in the PhotoServiceImpl savePhoto()");

        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            photo.setImage(byteObjects);
            photoRepository.save(photo);
        } catch (IOException e) {
            log.debug("Error occurred " + e);
            e.printStackTrace();
        }
    }

    @Override
    public Photo findPhotoById(Long photoId) throws NotFoundException {
        log.debug("I am in the PhotoServiceImpl findPhotoById()");

        Optional<Photo> photoOptional = photoRepository.findById(photoId);

        if (!photoOptional.isPresent()) {
            throw new NotFoundException("Photo not found for ID value " + photoId);
        }
        return photoOptional.get();
    }

    @Override
    public void deletePhotoById(Long photoId) {
        log.debug("I am in the PhotoServiceImpl deletePhotoById()");

        photoRepository.deleteById(photoId);
    }
}
