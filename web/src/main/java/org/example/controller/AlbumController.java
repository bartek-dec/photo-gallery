package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.service.AlbumService;
import org.example.service.PhotoService;
import org.example.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@Slf4j
public class AlbumController {

    private final AlbumService albumService;
    private final PhotoService photoService;

    @Autowired
    public AlbumController(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        log.debug("I am in the AlbumController home()");

        model.addAttribute("albums", albumService.findAllAlbums());

        return ViewNames.INDEX;
    }

    @GetMapping("/show/{albumId}")
    public void showFirstPhoto(@PathVariable("albumId") Long albumId, HttpServletResponse response) throws IOException {
        log.debug("I am in the AlbumController showFirstPhoto()");

        Photo photo = photoService.findAllPhotos(albumId).get(0);
        renderPhoto(response, photo);
    }

    @GetMapping("displayPhotos/{albumId}/{photoId}/display")
    public void displayPhotos(@PathVariable Long photoId, HttpServletResponse response) throws IOException {
        log.debug("I am in the AlbumController displayPhotos()");

        Photo photo = photoService.findPhotoById(photoId);
        renderPhoto(response, photo);
    }

    private void renderPhoto(HttpServletResponse response, Photo photo) throws IOException {
        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("add_album")
    public String displayForm(Model model) {
        log.debug("I am in the AlbumController displayForm()");

        model.addAttribute("album", new Album());

        return ViewNames.ADD_ALBUM;
    }

    @PostMapping("add_album")
    public String createAlbum(@ModelAttribute("album") Album album) {
        log.debug("I am in the AlbumController createAlbum()");

        Album savedAlbum = albumService.saveAlbum(album);

        return ViewNames.REDIRECT_ADD_PHOTO + savedAlbum.getId();
    }

    @GetMapping("remove_album")
    public String displayExistingAlbums(Model model) {
        log.debug("I am in the AlbumController displayExistingAlbums() ");

        model.addAttribute("albums", albumService.findAllAlbums());

        return ViewNames.REMOVE_ALBUM;
    }

    @GetMapping("remove_album/{albumId}/delete")
    public String removeAlbum(@PathVariable Long albumId) {
        log.debug("I am in the AlbumController removeAlbum()");

        albumService.deleteAlbumById(albumId);

        return ViewNames.REDIRECT_REMOVE_ALBUM;
    }

    @GetMapping("edit_album/{albumId}")
    public String editAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController editAlbum()");

        model.addAttribute("album", albumService.findAlbumById(albumId));
        model.addAttribute("photos", photoService.findAllPhotos(albumId));

        return ViewNames.EDIT_ALBUM;
    }

    @PostMapping("edit_album/{albumId}/updateData")
    public String submitUpdatedData(@PathVariable Long albumId, @ModelAttribute("album") Album album) {
        log.debug("I am in the AlbumController submitUpdatedData()");

        albumService.saveAlbum(album);

        return ViewNames.REDIRECT_EDIT_ALBUM + albumId;
    }

    @PostMapping("edit_album/{albumId}/updatePhoto")
    public String submitNewPhoto(@PathVariable Long albumId, @RequestParam("imagefile") MultipartFile file) {
        log.debug("I am in the AlbumController submitNewPhoto()");

        photoService.savePhoto(albumId, file);

        return ViewNames.REDIRECT_EDIT_ALBUM + albumId;
    }

    @GetMapping("edit_album/{albumId}/{photoId}/delete")
    public String deletePhoto(@PathVariable("albumId") Long albumId, @PathVariable("photoId") Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return ViewNames.REDIRECT_EDIT_ALBUM + albumId;
    }

    @GetMapping("show_album/{albumId}")
    public String showAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController showAlbum()");

        model.addAttribute("album", albumService.findAlbumById(albumId));
        model.addAttribute("photos", photoService.findAllPhotos(albumId));

        return ViewNames.SHOW_ALBUM;
    }
}
