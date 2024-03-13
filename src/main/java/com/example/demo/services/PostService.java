package com.example.demo.services;

import com.example.demo.component.RedisCacheManager;
import com.example.demo.dto.Comment;
import com.example.demo.dto.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    @Value("${api.url}")
    private String API_URL;

    private final RestTemplate restTemplate;
    private final RedisCacheManager redisCacheManager;

    public List<Post> getAllPosts() {
        String cacheKey = "allPosts";
        List<Post> cachedPosts = (List<Post>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedPosts.isEmpty()) {
            return cachedPosts;
        }
        String url = API_URL + "/posts";
        Post[] posts = restTemplate.getForObject(url, Post[].class);
        List<Post> result = Optional.ofNullable(posts).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public List<Post> getAllPostsByUser(Long userId) {
        String cacheKey = "allPosts_" + userId;
        List<Post> cachedPosts = (List<Post>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedPosts.isEmpty()) {
            return cachedPosts;
        }
        String url = API_URL + "/posts?userId={userId}";
        Post[] posts = restTemplate.getForObject(url, Post[].class, userId);
        List<Post> result = Optional.ofNullable(posts).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public Post getPost(Long id) {
        String cacheKey = getCacheKey(id);
        Post cachedPosts = (Post) redisCacheManager.get(cacheKey).orElse(null);
        if (cachedPosts != null) {
            return cachedPosts;
        }
        String url = API_URL + "/posts/" + id;
        Post post = restTemplate.getForObject(url, Post.class);
        if (post != null) {
            redisCacheManager.put(cacheKey, post);
        }
        return post;
    }

    public List<Comment> getCommentsByPostId(Long id) {
        String cacheKey = "CommentsByPostId_" + id;
        List<Comment> cachedComments = (List<Comment>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedComments.isEmpty()) {
            return cachedComments;
        }
        String url = String.format("%s/posts/%d/comments", API_URL, id);
        Comment[] comments = restTemplate.getForObject(url, Comment[].class);
        List<Comment> result = Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public List<Comment> getCommentsByPostIdQueryParam(Long postId) {
        String cacheKey = "CommentsByPostIdQueryParam_" + postId;
        List<Comment> cachedComments = (List<Comment>) redisCacheManager.get(cacheKey).orElse(Collections.emptyList());
        if (!cachedComments.isEmpty()) {
            return cachedComments;
        }
        String url = API_URL + "/comments?postId={postId}";
        Comment[] comments = restTemplate.getForObject(url, Comment[].class, postId);
        List<Comment> result = Optional.ofNullable(comments).map(Arrays::asList).orElse(Collections.emptyList());
        redisCacheManager.put(cacheKey, result);
        return result;
    }

    public Post createPost(Post post) {
        String cacheKey = getCacheKey(post.getId());
        redisCacheManager.put(cacheKey, post);
        List<Post> allPosts = getAllPosts();
        allPosts.add(post);
        redisCacheManager.put("allPosts", allPosts);
        String url = API_URL + "/posts";
        return restTemplate.postForEntity(url, post, Post.class).getBody();
    }

    public Post updatePost(Long id, Post post) {
        String cacheKey = getCacheKey(id);
        redisCacheManager.put(cacheKey, post);
        List<Post> allPosts = getAllPosts();
        for (Post existingPost : allPosts) {
            if (existingPost.getId().equals(id)) {
                existingPost.setId(post.getId());
                existingPost.setBody(post.getBody());
                existingPost.setTitle(post.getTitle());
                existingPost.setUserId(post.getUserId());
                break;
            }
        }
        redisCacheManager.put("allPosts", allPosts);
        String url = API_URL + "/posts/{id}";
        Map<String, Long> params = Collections.singletonMap("id", id);
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(post), Post.class, params).getBody();
    }

    public void deletePost(Long id) {
        String cacheKey = getCacheKey(id);
        redisCacheManager.evict(cacheKey);
        List<Post> allPosts = new ArrayList<>(getAllPosts());
        Iterator<Post> iterator = allPosts.iterator();
        while (iterator.hasNext()){
            Post existingPost = iterator.next();
            if ( existingPost.getId().equals(id)){
                iterator.remove();
                break;
            }
        }
        redisCacheManager.put("allPosts", allPosts);
        String url = API_URL + "/posts/{id}";
        restTemplate.delete(url, id);
    }

    private static String getCacheKey(Long id) {
        return "Post_" + id;
    }
}
