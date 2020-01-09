package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.service.AlbumService;
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

    //repair form used to update images in update form functionality
    //reformat code (get rid of doubled code)
    @GetMapping("/")
    public String home(Model model) {
        log.debug("I am in the AlbumController home()");

        model.addAttribute("albums", albumService.findAllAlbums());

        return "index";
    }

    @GetMapping("/show/{albumId}")
    public void showAlbums(@PathVariable("albumId") Long albumId, HttpServletResponse response) throws IOException {
        log.debug("I am in the AlbumController showAlbums()");

        Photo photo = photoService.findAllPhotos(albumId).get(0);
        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("add_album")
    public String addAlbum(Model model) {
        log.debug("I am in the AlbumController addAlbum()");

        model.addAttribute("album", new Album());

        return "add_album";
    }

    @PostMapping("add_album")
    public String createAlbum(@ModelAttribute("album") Album album) {
        log.debug("I am in the AlbumController createAlbum()");

        Album savedAlbum = albumService.saveAlbum(album);

        return "redirect:/add_photo/" + savedAlbum.getId();
    }

    @GetMapping("remove_album")
    public String displayExistingAlbums(Model model) {
        log.debug("I am in the AlbumController displayExistingAlbums() ");

        model.addAttribute("albums", albumService.findAllAlbums());

        return "remove_album";
    }

    @GetMapping("remove_album/{albumId}/delete")
    public String removeAlbum(@PathVariable Long albumId) {
        log.debug("I am in the AlbumController removeAlbum()");

        albumService.deleteAlbumById(albumId);

        return "redirect:/remove_album";
    }

    @GetMapping("edit_album/{albumId}")
    public String editAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController editAlbum()");

        model.addAttribute("album", albumService.findAlbumById(albumId));
        model.addAttribute("photos", photoService.findAllPhotos(albumId));
        model.addAttribute("id", albumId);
        return "edit_album";
    }

    @PostMapping("edit_album/{albumId}/updateData")
    public String submitAlbumsData(@PathVariable Long albumId, @ModelAttribute("album") Album album) {
        log.debug("I am in the AlbumController submitAlbumsData()");

        albumService.saveAlbum(album);

        return "redirect:/edit_album/" + albumId;
    }

    @PostMapping("edit_album/{albumId}/updatePhoto")
    public String submitAlbumsPhoto(@PathVariable Long albumId, @RequestParam("imagefile") MultipartFile file) {
        log.debug("I am in the AlbumController submitAlbumsPhoto()");

        photoService.savePhoto(albumId, file);

        return "redirect:/edit_album/" + albumId;
    }

    @GetMapping("edit_album/{albumId}/{photoId}/display")
    public void reloadEditAlbum(@PathVariable Long photoId, HttpServletResponse response) throws IOException {
        log.debug("I am in the AlbumController reloadEditAlbum()");

        Photo photo = photoService.findPhotoById(photoId);
        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("edit_album/{albumId}/{photoId}/delete")
    public String deletePhoto(@PathVariable("albumId") Long albumId, @PathVariable("photoId") Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return "redirect:/edit_album/" + albumId;
    }

    @GetMapping("show_album/{albumId}")
    public String showAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController showAlbum()");

        model.addAttribute("photos", photoService.findAllPhotos(albumId));
        model.addAttribute("album", albumService.findAlbumById(albumId));

        return "show_album";
    }

    @GetMapping("show_album/{albumId}/{photoId}/display")
    public void displayAlbum(@PathVariable Long photoId, HttpServletResponse response) throws IOException {
        log.debug("I am in the AlbumController diaplayAlbum()");

        Photo photo = photoService.findPhotoById(photoId);
        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(photo.getImage());
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }
}
