package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.exception.NotFoundException;
import org.example.service.AlbumService;
import org.example.service.PhotoService;
import org.example.util.AttributeNames;
import org.example.util.Mappings;
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

    @GetMapping(Mappings.HOME)
    public String home(Model model) {
        log.debug("I am in the AlbumController home()");

        model.addAttribute(AttributeNames.ALBUMS, albumService.findAllAlbums());

        return ViewNames.INDEX;
    }

    @GetMapping(Mappings.SHOW_ALBUMS)
    public void showFirstPhoto(@PathVariable Long albumId, HttpServletResponse response) {
        log.debug("I am in the AlbumController showFirstPhoto()");

        Photo photo = photoService.findAllPhotos(albumId).get(0);
        renderPhoto(response, photo);
    }

    @GetMapping(Mappings.DISPLAY_PHOTOS_ALBUM_ID_PHOTO_ID_DISPLAY)
    public void displayPhotos(@PathVariable Long photoId, HttpServletResponse response) {
        log.debug("I am in the AlbumController displayPhotos()");

        Photo photo = photoService.findPhotoById(photoId);
        renderPhoto(response, photo);
    }

    private void renderPhoto(HttpServletResponse response, Photo photo) {

        try {
            response.setContentType(AttributeNames.CONTENT_TYPE);
            InputStream inputStream = new ByteArrayInputStream(photo.getImage());
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @GetMapping(Mappings.ADD_ALBUM)
    public String displayForm(Model model) {
        log.debug("I am in the AlbumController displayForm()");

        model.addAttribute(AttributeNames.ALBUM, new Album());

        return ViewNames.ADD_ALBUM;
    }

    @PostMapping(Mappings.ADD_ALBUM)
    public String createAlbum(@ModelAttribute(AttributeNames.ALBUM) Album album) {
        log.debug("I am in the AlbumController createAlbum()");

        Album savedAlbum = albumService.saveAlbum(album);

        return "redirect:" + Mappings.ADD_PHOTO + savedAlbum.getId();
    }

    @GetMapping(Mappings.REMOVE_ALBUM)
    public String displayExistingAlbums(Model model) {
        log.debug("I am in the AlbumController displayExistingAlbums() ");

        model.addAttribute(AttributeNames.ALBUMS, albumService.findAllAlbums());

        return ViewNames.REMOVE_ALBUM;
    }

    @GetMapping(Mappings.REMOVE_ALBUM_ALBUM_ID_DELETE)
    public String removeAlbum(@PathVariable Long albumId) {
        log.debug("I am in the AlbumController removeAlbum()");

        albumService.deleteAlbumById(albumId);

        return "redirect:" + Mappings.REMOVE_ALBUM;
    }

    @GetMapping(Mappings.EDIT_ALBUM_ALBUM_ID)
    public String editAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController editAlbum()");

        model.addAttribute(AttributeNames.ALBUM, albumService.findAlbumById(albumId));
        model.addAttribute(AttributeNames.PHOTOS, photoService.findAllPhotos(albumId));

        return ViewNames.EDIT_ALBUM;
    }

    @PostMapping(Mappings.EDIT_ALBUM_ALBUM_ID_UPDATE_DATA)
    public String submitUpdatedData(@PathVariable Long albumId, @ModelAttribute(AttributeNames.ALBUM) Album album) {
        log.debug("I am in the AlbumController submitUpdatedData()");

        albumService.saveAlbum(album);

        return "redirect:" + Mappings.EDIT_ALBUM + albumId;
    }

    @PostMapping(Mappings.EDIT_ALBUM_ALBUM_ID_UPDATE_PHOTO)
    public String submitNewPhoto(@PathVariable Long albumId, @RequestParam(AttributeNames.IMAGE_FILE) MultipartFile file) {
        log.debug("I am in the AlbumController submitNewPhoto()");

        if (file.getContentType().equals(AttributeNames.CONTENT_TYPE)) {
            photoService.savePhoto(albumId, file);
        }

        return "redirect:" + Mappings.EDIT_ALBUM + albumId;
    }

    @GetMapping(Mappings.EDIT_ALBUM_ALBUM_ID_PHOTO_ID_DELETE)
    public String deletePhoto(@PathVariable Long albumId, @PathVariable Long photoId) {
        log.debug("I am in the PhotoController deletePhoto()");

        photoService.deletePhotoById(photoId);

        return "redirect:" + Mappings.EDIT_ALBUM + albumId;
    }

    @GetMapping(Mappings.SHOW_ALBUM_ALBUM_ID)
    public String showAlbum(@PathVariable Long albumId, Model model) {
        log.debug("I am in the AlbumController showAlbum()");

        model.addAttribute(AttributeNames.ALBUM, albumService.findAlbumById(albumId));
        model.addAttribute(AttributeNames.PHOTOS, photoService.findAllPhotos(albumId));

        return ViewNames.SHOW_ALBUM;
    }
}
