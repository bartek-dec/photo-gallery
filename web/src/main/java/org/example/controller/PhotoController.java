package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Photo;
import org.example.service.PhotoService;
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
    public String displayForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController displayForm()");

        model.addAttribute("albumId", albumId);
        model.addAttribute("photos", photoService.findAllPhotos(albumId));

        return ViewNames.ADD_PHOTO;
    }

    @GetMapping(Mappings.ADD_PHOTO_ALBUM_ID_SHOW_PHOTO_ID)
    public String reloadForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController reloadForm()");

        return "redirect:/" + Mappings.ADD_PHOTO + albumId;
    }

    @PostMapping(Mappings.ADD_PHOTO_ALBUM_ID)
    public String addPhoto(@PathVariable Long albumId, @RequestParam("imagefile") MultipartFile file) {
        log.debug("I am in the PhotoController addPhoto()");

        Photo savedPhoto = photoService.savePhoto(albumId, file);

        return "redirect:/" + Mappings.ADD_PHOTO + albumId + Mappings.SHOW + savedPhoto.getId();
    }

    @GetMapping(Mappings.ADD_PHOTO_ALBUM_ID_SHOW_PHOTO_ID_DELETE)
    public String deletePhoto(@PathVariable("albumId") Long albumId, @PathVariable("photoId") Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return "redirect:/" + Mappings.ADD_PHOTO + albumId;
    }

    @GetMapping(Mappings.SHOW_IMG_ALBUM_ID_PHOTO_ID)
    public String showImage(@PathVariable Long albumId, @PathVariable Long photoId, Model model) {
        log.debug("I am in the PhotoController showImg()");

        model.addAttribute("photo", photoService.findPhotoById(photoId));
        model.addAttribute("albumId", albumId);

        return ViewNames.SHOW_IMG;
    }

}
