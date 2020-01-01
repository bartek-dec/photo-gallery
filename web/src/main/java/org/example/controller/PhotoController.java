package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("add_photo")
    public String displayForm() {
        log.debug("I am in the PhotoController displayForm()");

        return "add_photo";
    }

    @PostMapping("add_photo")
    public String addPhoto() {
        log.debug("I am in the PhotoController addPhoto()");

        return "redirect:/add_photo";
    }

}
