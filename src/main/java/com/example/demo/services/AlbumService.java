package com.example.demo.services;

import com.example.demo.component.RedisCacheManager;
import com.example.demo.dto.Album;
import com.example.demo.dto.Photos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AlbumService {

    @Value("${api.url}")
    private String API_URL;

    private final RestTemplate restTemplate;
    private final RedisCacheManager redisCacheManager;

    public List<Album> getAllAlbums() {
        String cacheKey = "allAlbums";
        List<Album> cachedAlbums = (List<Album>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedAlbums.isEmpty()) {
            return cachedAlbums;
        }
        String url = API_URL + "/albums";
        Album[] albums = restTemplate.getForObject(url, Album[].class);
        List<Album> result = Optional.ofNullable(albums).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public List<Album> getAllAlbumsByUser(Long userId) {
        String cacheKey = "allAlbums_" + userId;
        List<Album> cachedAlbums = (List<Album>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedAlbums.isEmpty()) {
            return cachedAlbums;
        }
        String url = API_URL + "/albums?userId={userId}";
        Album[] albums = restTemplate.getForObject(url, Album[].class, userId);
        List<Album> result = Optional.ofNullable(albums).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public Album getAlbum(Long id) {
        String cacheKey = getCacheKey(id);
        Album cachedAlbum = (Album) redisCacheManager.get(cacheKey).orElse(null);
        if (cachedAlbum != null) {
            return cachedAlbum;
        }
        String url = API_URL + "/albums/" + id;
        Album album = restTemplate.getForObject(url, Album.class);
        if (album != null) {
            redisCacheManager.put(cacheKey, album);
        }
        return album;
    }

    public List<Photos> getPhotosByAlbumId(Long albumId) {
        String cacheKey = "PhotosByAlbumId_" + albumId;
        List<Photos> cachedPhotos = (List<Photos>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedPhotos.isEmpty()) {
            return cachedPhotos;
        }
        String url = String.format("%s/albums/%d/photos", API_URL, albumId);
        Photos[] photos = restTemplate.getForObject(url, Photos[].class);
        List<Photos> result = Optional.ofNullable(photos).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public Album createAlbum(Album album) {
        String cacheKey = getCacheKey(album.getId());
        redisCacheManager.put(cacheKey, album);
        List<Album> allAlbums = getAllAlbums();
        allAlbums.add(album);
        redisCacheManager.put("allAlbums", allAlbums);
        String url = API_URL + "/albums";
        return restTemplate.postForEntity(url, album, Album.class).getBody();
    }

    public Album updateAlbum(Long id, Album album) {
        String cacheKey = getCacheKey(id);
        redisCacheManager.put(cacheKey, album);
        List<Album> allAlbums = getAllAlbums();
        for (Album existingAlbum : allAlbums) {
            if (existingAlbum.getId().equals(id)) {
                existingAlbum.setId(album.getId());
                existingAlbum.setTitle(album.getTitle());
                existingAlbum.setUserId(album.getUserId());
                break;
            }
        }
        redisCacheManager.put("allAlbums", allAlbums);
        String url = API_URL + "/albums/{id}";
        Map<String, Long> params = Collections.singletonMap("id", id);
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(album), Album.class, params).getBody();
    }

    public void deleteAlbum(Long id) {
        String cacheKey = getCacheKey(id);
        redisCacheManager.evict(cacheKey);
        List<Album> allAlbums = new ArrayList<>(getAllAlbums());
        Iterator<Album> iterator = allAlbums.iterator();
        while (iterator.hasNext()) {
            Album existingAlbum = iterator.next();
            if (existingAlbum.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        redisCacheManager.put("allAlbums", allAlbums);
        String url = API_URL + "/albums/{id}";
        restTemplate.delete(url, id);
    }

    private static String getCacheKey(Long id) {
        return "Album_" + id;
    }

}
