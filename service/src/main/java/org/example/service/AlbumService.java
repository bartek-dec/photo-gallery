package org.example.service;

import javassist.NotFoundException;
import org.example.domain.Album;
import org.example.domain.Photo;

import java.util.List;

public interface AlbumService {

    List<Album> findAllAlbums();

    void saveAlbum(Album album);

    Album findAlbumById(Long albumId);

    void deleteAlbumById(Long albumId);

    List<Photo> getPhotos(Long albumId);
}
