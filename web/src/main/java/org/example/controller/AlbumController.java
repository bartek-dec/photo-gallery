package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/")
    public String home() {
        log.debug("I am in the AlbumController home()");

        return "index";
    }

    @GetMapping("add_album")
    public String addAlbum() {
        log.debug("I am in the AlbumController addAlbum()");

        return "add_album";
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
}
