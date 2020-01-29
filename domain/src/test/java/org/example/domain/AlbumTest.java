package org.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    private Album album;
    private Album blankAlbum;
    private final Long id = 1L;
    private final String name = "Madera";
    private final LocalDate date = LocalDate.of(2020, 1, 28);
    private final List<Photo> photos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        album = new Album();
        blankAlbum = new Album();
        photos.add(new Photo());

        album.setId(id);
        album.setName(name);
        album.setTripDate(date);
        album.setPhotos(photos);
    }

    @Test
    void getId() {
        Long idValue = 1L;

        assertEquals(idValue, album.getId());
    }

    @Test
    void nullWhenNoId() {
        assertNull(blankAlbum.getId());
    }

    @Test
    void getName() {
        String albumsName = "Madera";

        assertEquals(albumsName, album.getName());
    }

    @Test
    void nullWhenNoName() {
        assertNull(blankAlbum.getName());
    }

    @Test
    void getTripDate() {
        LocalDate tripDate = LocalDate.of(2020, 1, 28);

        assertEquals(tripDate, album.getTripDate());
    }

    @Test
    void nullWhenNoTripDate() {
        assertNull(blankAlbum.getTripDate());
    }

    @Test
    void getPhotos() {
        List<Photo> albumsPhotos = new ArrayList<>();
        albumsPhotos.add(new Photo());

        assertEquals(albumsPhotos, album.getPhotos());
    }

    @Test
    void zeroWhenNoPhotos() {
        assertEquals(0, blankAlbum.getPhotos().size());
    }
}