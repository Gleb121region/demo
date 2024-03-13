package com.example.demo.controller;

import com.example.demo.dto.Album;
import com.example.demo.dto.Post;
import com.example.demo.dto.Todos;
import com.example.demo.dto.user.User;
import com.example.demo.services.AlbumService;
import com.example.demo.services.PostService;
import com.example.demo.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UsersController {

    private final UsersService userService;
    private final AlbumService albumService;
    private final PostService postService;

    @GetMapping("/users")
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> getPostsByUserId(@PathVariable("id") Long userId) {
        return userService.getPostsByUserId(userId);
    }

    @GetMapping("/users/{id}/todos")
    public List<Todos> getTodosByUserId(@PathVariable("id") Long userId) {
        return userService.getTodosByUserId(userId);
    }

    @GetMapping("/users/{id}/albums")
    public List<Album> getAlbumsByUserId(@PathVariable("id") Long userId) {
        return userService.getAlbumsByUserId(userId);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user).getBody();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user).getBody();
    }

    @DeleteMapping("/users/{id}")
    public void updateUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/users/{userId}/albums")
    public List<Album> getAlbumsByUser(@PathVariable Long userId) {
        return albumService.getAllAlbumsByUser(userId);
    }

    @GetMapping("/users/{userId}/posts")
    public List<Post> getPostsByUser(@PathVariable Long userId) {
        return postService.getAllPostsByUser(userId);
    }

}
