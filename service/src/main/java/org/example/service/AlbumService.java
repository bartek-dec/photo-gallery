package org.example.service;

import org.example.domain.Album;
import org.example.domain.Photo;

import java.util.List;

public interface AlbumService {

    List<Album> findAllAlbums();

    Album saveAlbum(Album album);

    Album findAlbumById(Long albumId);

    void deleteAlbumById(Long albumId);

    List<Photo> getPhotos(Long albumId);
}
