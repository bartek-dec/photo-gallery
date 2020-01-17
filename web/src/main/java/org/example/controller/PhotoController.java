package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.PhotoService;
import org.example.util.AttributeNames;
import org.example.util.Mappings;
import org.example.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping(Mappings.ADD_PHOTO_ALBUM_ID)
    public String displayForm(@PathVariable Long albumId, Model model) {
        log.debug("I am in the PhotoController displayForm()");

        model.addAttribute(AttributeNames.ALBUM_ID, albumId);
        model.addAttribute(AttributeNames.PHOTOS, photoService.findAllPhotos(albumId));

        return ViewNames.ADD_PHOTO;
    }

    @PostMapping(Mappings.ADD_PHOTO_ALBUM_ID)
    public String addPhoto(@PathVariable Long albumId, @RequestParam(AttributeNames.IMAGE_FILE) MultipartFile file) {
        log.debug("I am in the PhotoController addPhoto()");

        if (file.getContentType().equals(AttributeNames.CONTENT_TYPE)) {
            photoService.savePhoto(albumId, file);
        }

        return "redirect:" + Mappings.ADD_PHOTO + albumId;
    }

    @GetMapping(Mappings.ADD_PHOTO_ALBUM_ID_SHOW_PHOTO_ID_DELETE)
    public String deletePhoto(@PathVariable Long albumId, @PathVariable Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return "redirect:" + Mappings.ADD_PHOTO + albumId;
    }

    @GetMapping(Mappings.SHOW_IMG_ALBUM_ID_PHOTO_ID)
    public String showImage(@PathVariable Long albumId, @PathVariable Long photoId, Model model) {
        log.debug("I am in the PhotoController showImg()");

        model.addAttribute(AttributeNames.PHOTO, photoService.findPhotoById(photoId));
        model.addAttribute(AttributeNames.ALBUM_ID, albumId);

        return ViewNames.SHOW_IMG;
    }
}
