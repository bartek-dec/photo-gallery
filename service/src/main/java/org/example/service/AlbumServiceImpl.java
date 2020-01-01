package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Album;
import org.example.domain.Photo;
import org.example.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public List<Album> findAllAlbums() {
        log.debug("I am in the AlbumServiceImpl findAllAlbums()");

        List<Album> albums = new ArrayList<>();
        albumRepository.findAll().iterator().forEachRemaining(albums::add);

        return albums;
    }

    @Override
    public Album saveAlbum(Album album) {
        log.debug("I am in the AlbumServiceImpl saveAlbum()");

        Album savedAlbum = albumRepository.save(album);

        return savedAlbum;
    }

    @Override
    public Album findAlbumById(Long albumId) {
        log.debug("I am in the AlbumServiceImpl findAlbumById()");

        Optional<Album> albumOptional = albumRepository.findById(albumId);

        if (!albumOptional.isPresent()) {
            return null;
        }

        return albumOptional.get();
    }

    @Override
    public void deleteAlbumById(Long albumId) {
        log.debug("I am in the AlbumServiceImpl deleteAlbumById()");

        albumRepository.deleteById(albumId);
    }

    @Override
    public List<Photo> getPhotos(Long albumId) {
        log.debug("I am in the AlbumServiceImpl getPhotos()");

        List<Photo> photos = new ArrayList<>();

        if (findAlbumById(albumId).equals(null)) {
            return null;
        } else {
            photos.addAll(findAlbumById(albumId).getPhotos());
        }

        return photos;
    }
}
