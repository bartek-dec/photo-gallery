package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Album;
import org.example.exception.AlreadyExistException;
import org.example.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        Collections.sort(albums);

        return albums;
    }

    @Override
    public Album saveAlbum(Album album) {
        log.debug("I am in the AlbumServiceImpl saveAlbum()");

        Album savedAlbum;
        List<Album> albums = findAllAlbums();

        if (albums.size() == 0) {
            savedAlbum = albumRepository.save(album);
        } else {
            Optional<Album> albumOptional = albums
                    .stream()
                    .filter(a -> a.equals(album))
                    .findFirst();

            if (!albumOptional.isPresent()) {
                savedAlbum = albumRepository.save(album);
            } else {
                throw new AlreadyExistException("Album already exists");
            }
        }

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
}
