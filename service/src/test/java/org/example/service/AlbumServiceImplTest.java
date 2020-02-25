package org.example.service;

import org.example.domain.Album;
import org.example.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    private AlbumServiceImpl albumService;

    @Mock
    private AlbumRepository albumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        albumService = new AlbumServiceImpl(albumRepository);
    }

    @Test
    void findAllAlbums() {
        Album album = Album.builder().build();
        List<Album> albums = new ArrayList<>();
        albums.add(album);

        when(albumRepository.findAll()).thenReturn(albums);

        List<Album> foundAlbums = albumService.findAllAlbums();

        assertEquals(1, foundAlbums.size());
        verify(albumRepository, times(1)).findAll();
        verify(albumRepository, never()).findById(anyLong());
    }

    @Test
    void whenNoAlbumsThenEmptyList() {
        when(albumRepository.findAll()).thenReturn(new ArrayList<>());

        List<Album> albums = albumService.findAllAlbums();

        assertEquals(0, albums.size());
    }

    @Test
    void saveAlbumNoOtherAlbumsInDatabase() {
        Album album = Album.builder().id(1L).build();
        List<Album> albums = new ArrayList<>();

        when(albumRepository.findAll()).thenReturn(albums);
        when(albumRepository.save(album)).thenReturn(album);

        Album savedAlbum = albumService.saveAlbum(album);

        assertEquals(album, savedAlbum);
        assertEquals(album.getId(), savedAlbum.getId());
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    void saveAlbumOtherAlbumAlreadyExistInDatabase() {
        Album album = Album.builder().id(1L).name("Madera")
                .tripDate(LocalDate.of(2020, 1, 12)).build();

        Album albumToSave=Album.builder().name("England")
                .tripDate(LocalDate.of(2020,1,1)).build();

        List<Album> albums = new ArrayList<>();
        albums.add(album);

        when(albumRepository.findAll()).thenReturn(albums);
        when(albumRepository.save(albumToSave)).thenReturn(albumToSave);

        Album savedAlbum = albumService.saveAlbum(albumToSave);

        assertEquals(albumToSave, savedAlbum);
        verify(albumRepository, times(1)).save(albumToSave);
    }

    @Test
    void saveAlbumTheSameAlbumAlreadyExistInDatabase() {
        Album album = Album.builder().id(1L).name("Madera")
                .tripDate(LocalDate.of(2020, 1, 12)).build();

        Album albumToSave=Album.builder().name("Madera")
                .tripDate(LocalDate.of(2020,1,12)).build();

        List<Album> albums = new ArrayList<>();
        albums.add(album);

        when(albumRepository.findAll()).thenReturn(albums);

        Album savedAlbum = albumService.saveAlbum(albumToSave);

        assertNull(savedAlbum);
    }

    @Test
    void whenSaveNullThenExpectNull() {
        List<Album> albums = new ArrayList<>();

        when(albumRepository.findAll()).thenReturn(albums);
        when(albumRepository.save(null)).thenReturn(null);

        Album album = albumService.saveAlbum(null);

        assertNull(album);
    }

    @Test
    void findAlbumById() {
        Album album = Album.builder().id(1L).build();
        Optional<Album> albumOptional = Optional.of(album);

        when(albumRepository.findById(anyLong())).thenReturn(albumOptional);

        Album foundAlbum = albumService.findAlbumById(1L);

        assertNotNull(foundAlbum);
        assertEquals(album, foundAlbum);
        verify(albumRepository, times(1)).findById(anyLong());
        verify(albumRepository, never()).findAll();
    }

    @Test
    void findAlbumByIdNotFound() {
        Album foundAlbum = albumService.findAlbumById(anyLong());

        assertNull(foundAlbum);
    }

    @Test
    void deleteAlbumById() {
        albumService.deleteAlbumById(anyLong());

        verify(albumRepository, times(1)).deleteById(anyLong());
    }
}