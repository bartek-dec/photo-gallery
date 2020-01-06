package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.domain.Photo;
import org.example.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }


    @GetMapping("add_photo/{albumId}/show/{photoId}/show")
    public void displayPhotos(@PathVariable("photoId") Long photoId,
                              HttpServletResponse response) throws IOException {
        log.debug("I am in the PhotoController displayPhotos()");

        Photo photo = photoService.findPhotoById(Long.valueOf(photoId));

        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
    }


    @GetMapping("add_photo/{albumId}")
    public String displayFreshForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController displayFreshForm()");

        List<Photo> photos = photoService.findAllPhotos(albumId);

        log.debug("Albums size is " + photos.size());

        model.addAttribute("id", albumId);
        model.addAttribute("photos", photos);

        return "add_photo";
    }

    @GetMapping("add_photo/{albumId}/show/{photoId}")
    public String displayForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController displayForm()");

        List<Photo> photos = photoService.findAllPhotos(albumId);

        log.debug("Albums size is " + photos.size());

        model.addAttribute("id", albumId);
        model.addAttribute("photos", photos);

        return "add_photo";
    }

    @PostMapping("add_photo/{id}")
    public String addPhoto(@PathVariable("id") Long id, @RequestParam("imagefile") MultipartFile file) {
        log.debug("I am in the PhotoController addPhoto()");

        Photo savedPhoto = photoService.savePhoto(id, file);

        return "redirect:/add_photo/" + id + "/show/" + savedPhoto.getId();
    }

}
