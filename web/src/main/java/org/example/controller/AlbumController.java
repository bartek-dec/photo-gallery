package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Album;
import org.example.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/")
    public String home(Model model) {
        log.debug("I am in the AlbumController home()");

        model.addAttribute("albums", albumService.findAllAlbums());

        log.debug("I am in the AlbumController home() after getting data from service");

        return "index";
    }

    @GetMapping("add_album")
    public String addAlbum(Model model) {
        log.debug("I am in the AlbumController addAlbum()");

        model.addAttribute("album", new Album());

        return "add_album";
    }

    @PostMapping("add_album")
    public String createAlbum(Album album) {
        log.debug("I am in the AlbumController createAlbum()");

        albumService.saveAlbum(album);

        return "redirect:/add_photo";
    }

    @GetMapping("remove_album")
    public String removeAlbum() {
        log.debug("I am in the AlbumController removeAlbum() ");

        return "remove_album";
    }

    @GetMapping("edit_album")
    public String editAlbum() {
        log.debug("I am in the AlbumController editAlbum()");

        return "edit_album";
    }

    @GetMapping("show_album/{id}")
    public String showAlbum(@PathVariable String id, Model model) {
        log.debug("I am in the AlbumController showAlbum()");

        model.addAttribute("photos", albumService.findAlbumById(Long.valueOf(id)).getPhotos());

        return "show_album";
    }
}
