package org.example.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhotoTest {

    private Photo photo;
    private Photo blankPhoto;
    private final Long id = 1L;
    private final byte[] image = new byte[1];
    private final Album album = new Album();

    @BeforeEach
    void setUp() {
        photo = new Photo();
        blankPhoto = new Photo();

        photo.setId(id);
        photo.setImage(image);
        photo.setAlbum(album);
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
        Album photosAlbum = new Album();

        assertEquals(photosAlbum, photo.getAlbum());
    }

    @Test
    void nullWhenNoAlbum() {
        assertNull(blankPhoto.getAlbum());
    }
}