package org.example.controller;

import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.service.AlbumService;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AlbumControllerTest {

    @Mock
    private AlbumService albumService;
    @Mock
    private PhotoService photoService;

    private AlbumController albumController;
    private MockMvc mockMvc;
    private List<Album> albums;
    private Album album;
    private List<Photo> photos;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        albumController = new AlbumController(albumService, photoService);
        mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();

        albums = new ArrayList<>();
        albums.add(Album.builder().id(1L).build());
        albums.add(Album.builder().id(2L).build());

        album = Album.builder().id(3L).name("Madera").tripDate(LocalDate.of(2020, 1, 10)).build();

        photos = new ArrayList<>();
        photos.add(Photo.builder().build());
        photos.add(Photo.builder().build());
    }

    @Test
    void home() throws Exception {
        when(albumService.findAllAlbums()).thenReturn(albums);

        mockMvc.perform(get(Mappings.HOME))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.INDEX))
                .andExpect(model().attribute(AttributeNames.ALBUMS, hasSize(2)));

        verify(albumService, times(1)).findAllAlbums();
    }

    @Test
    void showFirstPhoto() throws Exception {
        String s = "fake image text";
        Photo photo = Photo.builder().id(1L).image(s.getBytes()).build();
        List<Photo> dummyPhotos = new ArrayList<>();
        dummyPhotos.add(photo);

        when(photoService.findAllPhotos(anyLong())).thenReturn(dummyPhotos);

        MockHttpServletResponse response = mockMvc.perform(get(Mappings.SHOW + album.getId()))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseByte = response.getContentAsByteArray();

        assertEquals(s.getBytes().length, responseByte.length);
        verify(photoService, times(1)).findAllPhotos(anyLong());
    }

    @Test
    void displayPhotos() throws Exception {
        String s = "fake image content";
        Photo photo = Photo.builder().id(1L).image(s.getBytes()).album(album).build();

        when(photoService.findPhotoById(anyLong())).thenReturn(photo);

        MockHttpServletResponse response = mockMvc.perform(get("/" + Mappings.DISPLAY_PHOTOS + "/" +
                album.getId() + "/" + photo.getId() + "/" + Mappings.DISPLAY))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(photo.getImage().length, responseBytes.length);
    }

    @Test
    void displayForm() throws Exception {
        Album albumToCreate = Album.builder().build();
        mockMvc.perform(get(Mappings.ADD_ALBUM))
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ALBUM, albumToCreate))
                .andExpect(view().name(ViewNames.ADD_ALBUM));
    }

    @Test
    void createAlbum() throws Exception {
        String name = "Madera";
        Album unSavedAlbum = Album.builder().name(name).tripDate(LocalDate.of(2020, 1, 10)).build();
        Album savedAlbum = Album.builder().id(2L).name(name).tripDate(LocalDate.of(2020, 1, 10)).build();

        when(albumService.saveAlbum(unSavedAlbum)).thenReturn(savedAlbum);

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", unSavedAlbum.getName())
                .param("tripDate", unSavedAlbum.getTripDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.ADD_PHOTO + savedAlbum.getId()));

        verify(albumService, atLeastOnce()).saveAlbum(any());
    }

    @Test
    void createAlbumNameTooShort() throws Exception {
        String name = "Ma";
        Album badAlbum = Album.builder().name(name).tripDate(LocalDate.of(2020, 1, 10)).build();

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", badAlbum.getName())
                .param("tripDate", badAlbum.getTripDate().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ADD_ALBUM));

        verify(albumService, never()).saveAlbum(any());
    }

    @Test
    void createAlbumNameTooLong() throws Exception {
        String name = "Maaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaadera";
        Album badAlbum = Album.builder().name(name).tripDate(LocalDate.of(2020, 1, 10)).build();

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", badAlbum.getName())
                .param("tripDate", badAlbum.getTripDate().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ADD_ALBUM));

        verify(albumService, never()).saveAlbum(any());
    }

    @Test
    void createAlbumNameThreeSpaces() throws Exception {
        String name = "   ";
        Album badAlbum = Album.builder().name(name).tripDate(LocalDate.of(2020, 1, 10)).build();

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", badAlbum.getName())
                .param("tripDate", badAlbum.getTripDate().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ADD_ALBUM));

        verify(albumService, never()).saveAlbum(any());
    }

    @Test
    void createAlbumAtLeastOneNonWhiteSpace() throws Exception {
        String name = "  a";
        Album unSavedAlbum = Album.builder().name(name).tripDate(LocalDate.of(2020, 1, 10)).build();
        Album savedAlbum = Album.builder().id(2L).name(name).tripDate(LocalDate.of(2020, 1, 10)).build();

        when(albumService.saveAlbum(unSavedAlbum)).thenReturn(savedAlbum);

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", unSavedAlbum.getName())
                .param("tripDate", unSavedAlbum.getTripDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.ADD_PHOTO + savedAlbum.getId()));

        verify(albumService, atLeastOnce()).saveAlbum(any());
    }

    @Test
    void createAlbumTripDateNotPast() throws Exception {
        String name = "Madera";
        Album badAlbum = Album.builder().name(name).tripDate(LocalDate.now()).build();

        mockMvc.perform(post(Mappings.ADD_ALBUM)
                .param("name", badAlbum.getName())
                .param("tripDate", badAlbum.getTripDate().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.ADD_ALBUM));

        verify(albumService, never()).saveAlbum(any());
    }

    @Test
    void displayExistingAlbums() throws Exception {
        when(albumService.findAllAlbums()).thenReturn(albums);

        mockMvc.perform(get(Mappings.REMOVE_ALBUM))
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ALBUMS, albums))
                .andExpect(view().name(ViewNames.REMOVE_ALBUM));

        verify(albumService, times(1)).findAllAlbums();
    }

    @Test
    void removeAlbum() throws Exception {
        mockMvc.perform(get("/remove_album/" + album.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.REMOVE_ALBUM));

        verify(albumService, atLeastOnce()).deleteAlbumById(anyLong());
    }

    @Test
    void editAlbum() throws Exception {
        when(albumService.findAlbumById(anyLong())).thenReturn(album);
        when(photoService.findAllPhotos(anyLong())).thenReturn(photos);

        mockMvc.perform(get(Mappings.EDIT_ALBUM + album.getId()))
                .andExpect(model().attribute(AttributeNames.ALBUM, album))
                .andExpect(model().attribute(AttributeNames.PHOTOS, photos))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.EDIT_ALBUM));

        verify(albumService, times(1)).findAlbumById(anyLong());
        verify(photoService, times(1)).findAllPhotos(anyLong());
    }

    @Test
    void submitUpdatedData() throws Exception {
        mockMvc.perform(post(Mappings.EDIT_ALBUM + album.getId() + Mappings.UPDATE_DATA)
                .param("id", album.getId().toString())
                .param("Name", album.getName())
                .param("tripDate", album.getTripDate().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.EDIT_ALBUM + album.getId()));

        verify(albumService, times(1)).saveAlbum(any());
    }

    @Test
    void submitNewPhoto() throws Exception {
        String fileName = "imagefile";
        MockMultipartFile file = new MockMultipartFile(fileName, "testing.txt", AttributeNames.CONTENT_TYPE,
                fileName.getBytes());

        mockMvc.perform(multipart(Mappings.EDIT_ALBUM + album.getId() + Mappings.UPDATE_PHOTO)
                .file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/edit_album/3"))
                .andExpect(redirectedUrl(Mappings.EDIT_ALBUM + album.getId()));

        verify(photoService, times(1)).savePhoto(anyLong(), any());
    }

    @Test
    void deletePhoto() throws Exception {
        Photo photo = Photo.builder().id(1L).build();

        mockMvc.perform(get(Mappings.EDIT_ALBUM + album.getId() + "/" + photo.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(Mappings.EDIT_ALBUM + album.getId()))
                .andExpect(header().string("Location", "/edit_album/3"));

        verify(photoService, times(1)).deletePhotoById(anyLong());
    }

    @Test
    void showAlbum() throws Exception {
        when(albumService.findAlbumById(anyLong())).thenReturn(album);
        when(photoService.findAllPhotos(anyLong())).thenReturn(photos);

        mockMvc.perform(get(Mappings.SHOW_ALBUM + album.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute(AttributeNames.ALBUM, album))
                .andExpect(model().attribute(AttributeNames.PHOTOS, photos))
                .andExpect(view().name(ViewNames.SHOW_ALBUM));

        verify(albumService, times(1)).findAlbumById(anyLong());
        verify(photoService, times(1)).findAllPhotos(anyLong());
    }
}