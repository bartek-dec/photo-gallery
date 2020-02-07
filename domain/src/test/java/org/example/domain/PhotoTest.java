package org.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    private Photo photo;
    private Photo blankPhoto;
    private final Long id = 1L;
    private final byte[] image = new byte[1];
    private final LocalDate date = LocalDate.of(2020, 1, 28);
    private final Album album = Album.builder().name("Madera").tripDate(date).build();

    @BeforeEach
    void setUp() {
        photo = Photo.builder().id(id).image(image).album(album).build();
        blankPhoto = Photo.builder().build();
    }

    @Test
    void getId() {
        Long photoId = 1L;

        assertEquals(photoId, photo.getId());
    }

    @Test
    void nullWhenNoId() {
        assertNull(blankPhoto.getId());
    }

    @Test
    void getImage() {
        byte[] array = new byte[1];

        assertArrayEquals(array, photo.getImage());
    }

    @Test
    void nullWhenNoImage() {
        assertNull(blankPhoto.getImage());
    }

    @Test
    void getAlbum() {
        LocalDate tripDate = LocalDate.of(2020, 1, 28);
        Album photosAlbum = Album.builder().name("Madera").tripDate(tripDate).build();

        assertEquals(photosAlbum, photo.getAlbum());
    }

    @Test
    void nullWhenNoAlbum() {
        assertNull(blankPhoto.getAlbum());
    }
}