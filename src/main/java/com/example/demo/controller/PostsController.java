package com.example.demo.controller;

import com.example.demo.dto.Comment;
import com.example.demo.dto.Post;
import com.example.demo.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostsController {

    private final PostService postService;

    @GetMapping("/posts")
    public List<Post> getAll() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/posts/{id}/comments")
    public List<Comment> getCommentsByPostId(@PathVariable Long id) {
        return postService.getCommentsByPostId(id);
    }

    @GetMapping("/comments")
    public List<Comment> getCommentsByPostIdQueryParam(@RequestParam Long postId) {
        return postService.getCommentsByPostIdQueryParam(postId);
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post).getBody();
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post).getBody();
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

}
