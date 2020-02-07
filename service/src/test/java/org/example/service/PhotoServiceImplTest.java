package org.example.service;

import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.exception.NotFoundException;
import org.example.repository.PhotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceImplTest {

    private PhotoService photoService;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private AlbumService albumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        photoService = new PhotoServiceImpl(photoRepository, albumService);
    }

    @Test
    void findAllPhotos() {
        Album album = Album.builder().photos(new ArrayList<>()).build();

        when(albumService.findAlbumById(anyLong())).thenReturn(album);

        List<Photo> photos = photoService.findAllPhotos(anyLong());

        assertNotNull(photos);
        verify(albumService, times(1)).findAlbumById(anyLong());
        verify(albumService, never()).findAllAlbums();
    }

    @Test
    void findAllPhotosNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> photoService.findAllPhotos(anyLong()));
    }

    @Test
    void savePhoto() {
        String name = "file";
        MultipartFile multipartFile = new MockMultipartFile(name, name.getBytes());
        Album album = Album.builder().photos(new ArrayList<>()).build();
        Photo photo = Photo.builder().image(name.getBytes()).album(album).build();

        when(albumService.findAlbumById(anyLong())).thenReturn(album);
        when(photoRepository.save(photo)).thenReturn(photo);

        Photo savedPhoto = photoService.savePhoto(anyLong(), multipartFile);

        assertNotNull(savedPhoto);
    }

    @Test
    void savePhotoWhenAlbumNotFound() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        Assertions.assertThrows(NotFoundException.class, () -> photoService.savePhoto(anyLong(), multipartFile));
    }

    @Test
    void savePhotoWhenImageIsEmpty() {
        MultipartFile file = new MockMultipartFile("file", new byte[0]);
        Album album = Album.builder().build();

        when(albumService.findAlbumById(anyLong())).thenReturn(album);

        Photo savedPhoto = photoService.savePhoto(anyLong(), file);

        assertNull(savedPhoto);
    }

    @Test
    void findPhotoById() {
        Photo photo = Photo.builder().build();
        Optional<Photo> photoOptional = Optional.of(photo);

        when(photoRepository.findById(anyLong())).thenReturn(photoOptional);

        Photo foundPhoto = photoService.findPhotoById(anyLong());

        assertEquals(photo, foundPhoto);
        verify(photoRepository, times(1)).findById(anyLong());
        verify(photoRepository, never()).findAll();
    }

    @Test
    void findPhotoByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> photoService.findPhotoById(anyLong()));
    }

    @Test
    void deletePhotoById() {
        photoService.deletePhotoById(anyLong());

        verify(photoRepository, times(1)).deleteById(anyLong());
    }
}