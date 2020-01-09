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

        Photo photo = photoService.findPhotoById(photoId);

        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("add_photo/{albumId}")
    public String displayForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController displayForm()");

        fillInModel(albumId, model);

        return "add_photo";
    }

    @GetMapping("add_photo/{albumId}/show/{photoId}")
    public String reloadForm(@PathVariable("albumId") Long albumId, Model model) {
        log.debug("I am in the PhotoController reloadForm()");

        //fillInModel(albumId, model);

        return "redirect:/add_photo/" + albumId;
    }

    private void fillInModel(@PathVariable("albumId") Long albumId, Model model) {
        List<Photo> photos = photoService.findAllPhotos(albumId);

        model.addAttribute("id", albumId);
        model.addAttribute("photos", photos);
    }

    @PostMapping("add_photo/{albumId}")
    public String addPhoto(@PathVariable Long albumId, @RequestParam("imagefile") MultipartFile file) {
        log.debug("I am in the PhotoController addPhoto()");

        Photo savedPhoto = photoService.savePhoto(albumId, file);

        return "redirect:/add_photo/" + albumId + "/show/" + savedPhoto.getId();
    }

    @GetMapping("add_photo/{albumId}/show/{photoId}/delete")
    public String deletePhoto(@PathVariable("albumId") Long albumId, @PathVariable("photoId") Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return "redirect:/add_photo/" + albumId;
    }

    @GetMapping("show_img/{photoId}")
    public String showImg(@PathVariable Long photoId, Model model) {
        log.debug("I am in the PhotoController showImg()");

        model.addAttribute("photo", photoService.findPhotoById(photoId));

        return "show_img";
    }

}
