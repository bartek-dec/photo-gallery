package org.example.controller;

import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.service.PhotoService;
import org.example.util.AttributeNames;
import org.example.util.Mappings;
import org.example.util.ViewNames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PhotoControllerTest {

    @Mock
    private PhotoService photoService;

    private PhotoController photoController;
    private MockMvc mockMvc;
    private Album album;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        photoController = new PhotoController(photoService);
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();
        album = Album.builder().id(1L).name("Madera").build();
    }

    @Test
    void displayForm() throws Exception {
        List<Photo> photos = new ArrayList<>();
        photos.add(Photo.builder().build());

        when(photoService.findAllPhotos(anyLong())).thenReturn(photos);

        mockMvc.perform(get(Mappings.ADD_PHOTO + album.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ALBUM_ID, album.getId()))
                .andExpect(model().attribute(AttributeNames.PHOTOS, photos))
                .andExpect(view().name(ViewNames.ADD_PHOTO));

        verify(photoService, times(1)).findAllPhotos(anyLong());
    }

    @Test
    void addPhoto() throws Exception {
        String fileName = "imagefile";
        MockMultipartFile file = new MockMultipartFile(fileName, "test.txt", AttributeNames.CONTENT_TYPE,
                fileName.getBytes());

        mockMvc.perform(multipart(Mappings.ADD_PHOTO + album.getId())
                .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/add_photo/1"))
                .andExpect(redirectedUrl(Mappings.ADD_PHOTO + album.getId()));

        verify(photoService, times(1)).savePhoto(anyLong(), any());
    }

    @Test
    void deletePhoto() throws Exception {
        Photo photo = Photo.builder().id(2L).build();

        mockMvc.perform(get(Mappings.ADD_PHOTO + album.getId() + Mappings.SHOW + photo.getId() + "/"
                + Mappings.DELETE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.ADD_PHOTO + album.getId()));

        verify(photoService, times(1)).deletePhotoById(anyLong());
    }

    @Test
    void showImage() throws Exception {
        Photo photo = Photo.builder().id(1L).build();

        when(photoService.findPhotoById(anyLong())).thenReturn(photo);

        mockMvc.perform(get(Mappings.SHOW_IMG + album.getId() + "/" + photo.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.PHOTO, photo))
                .andExpect(model().attribute(AttributeNames.ALBUM_ID, album.getId()))
                .andExpect(view().name(ViewNames.SHOW_IMG));

        verify(photoService, times(1)).findPhotoById(anyLong());
    }
}