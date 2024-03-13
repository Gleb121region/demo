package com.example.demo.controller;

import com.example.demo.dto.Album;
import com.example.demo.dto.Photos;
import com.example.demo.services.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlbumsController {

    private final AlbumService albumService;

    @GetMapping("/albums")
    public List<Album> getAll() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/albums/{id}")
    public Album getAlbum(@PathVariable Long id) {
        return albumService.getAlbum(id);
    }

    @GetMapping("/albums/{id}/photos")
    public List<Photos> getPhotosByAlbumId(@PathVariable("id") Long albumId) {
        return albumService.getPhotosByAlbumId(albumId);
    }

    @PostMapping("/albums")
    public Album createAlbum(@RequestBody Album album) {
        return albumService.createAlbum(album);
    }

    @PutMapping("/albums/{id}")
    public Album updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        return albumService.updateAlbum(id, album);
    }

    @DeleteMapping("/albums/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }
}
