package com.example.demo.services;

import com.example.demo.component.RedisCacheManager;
import com.example.demo.dto.Album;
import com.example.demo.dto.Post;
import com.example.demo.dto.Todos;
import com.example.demo.dto.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    @Value("${api.url}")
    private String API_URL;

    private final RestTemplate restTemplate;
    private final RedisCacheManager redisCacheManager;

    public List<User> getAllUsers() {
        String cacheKey = "allUsers_";
        List<User> cachedUsers = (List<User>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedUsers.isEmpty()) {
            return cachedUsers;
        }
        String url = API_URL + "/users";
        User[] users = restTemplate.getForObject(url, User[].class);
        List<User> result = Optional.ofNullable(users).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, Arrays.asList(result.toArray()));
        return result;
    }

    public User getUser(Long id) {
        String cacheKey = getCacheKey(id);
        User cachedUsers = (User) redisCacheManager.get(cacheKey).orElse(null);
        if (cachedUsers != null) {
            return cachedUsers;
        }
        String url = API_URL + "/users/" + id;
        User user = restTemplate.getForObject(url, User.class);
        if (user != null) {
            redisCacheManager.put(cacheKey, user);
        }
        return user;
    }

    public List<Post> getPostsByUserId(Long userId) {
        String cacheKey = "PostsByUserId_" + userId;
        List<Post> cachedPosts = (List<Post>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedPosts.isEmpty()) {
            return cachedPosts;
        }
        String url = API_URL + "/posts?userId={id}";
        Post[] posts = restTemplate.getForObject(url, Post[].class, userId);
        List<Post> result = Optional.ofNullable(posts).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public List<Todos> getTodosByUserId(Long userId) {
        String cacheKey = "TodosByUserId_" + userId;
        List<Todos> cachedTodos = (List<Todos>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedTodos.isEmpty()) {
            return cachedTodos;
        }
        String url = String.format("%s/users/%d/todos", API_URL, userId);
        Todos[] todos = restTemplate.getForObject(url, Todos[].class);
        List<Todos> result = Optional.ofNullable(todos).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public List<Album> getAlbumsByUserId(Long userId) {
        String cacheKey = "AlbumsByUserId_" + userId;
        List<Album> cachedAlbums = (List<Album>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedAlbums.isEmpty()) {
            return cachedAlbums;
        }
        String url = String.format("%s/users/%d/albums", API_URL, userId);
        Album[] albums = restTemplate.getForObject(url, Album[].class);
        List<Album> result = Optional.ofNullable(albums).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public User createUser(User user) {
        String cacheKey = getCacheKey(user.getId());
        redisCacheManager.put(cacheKey, user);
        List<User> allUser = getAllUsers();
        allUser.add(user);
        redisCacheManager.put("allUsers_", allUser);
        String url = API_URL + "/users";
        return restTemplate.postForEntity(url, user, User.class).getBody();
    }

    public User updateUser(Long id, User user) {
        String cacheKey = getCacheKey(user.getId());
        redisCacheManager.put(cacheKey, user);
        List<User> allUsers = getAllUsers();
        for (User existingUser : allUsers) {
            if (existingUser.getId().equals(id)) {
                existingUser.setId(user.getId());
                existingUser.setName(user.getName());
                existingUser.setPhone(user.getPhone());
                existingUser.setEmail(existingUser.getEmail());
                existingUser.setCompany(existingUser.getCompany());
                existingUser.setAddress(existingUser.getAddress());
                existingUser.setWebsite(existingUser.getWebsite());
                break;
            }
        }
        redisCacheManager.put("allUsers_", allUsers);
        String url = API_URL + "/users/{id}";
        Map<String, Long> params = Collections.singletonMap("id", id);
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(user), User.class, params).getBody();
    }

    public void deleteUser(Long id) {
        String cacheKey = getCacheKey(id);
        redisCacheManager.evict(cacheKey);
        List<User> allUsers = new ArrayList<>(getAllUsers());
        Iterator<User> iterator = allUsers.iterator();
        while (iterator.hasNext()) {
            User existingUser = iterator.next();
            if (existingUser.getId().equals(id)) {
                iterator.remove();
                break;
            }
        }
        redisCacheManager.put("allUsers_", allUsers);
        String url = API_URL + "/users/{id}";
        restTemplate.delete(url, id);
    }

    private static String getCacheKey(Long id) {
        return "User_" + id;
    }

}
