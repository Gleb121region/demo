package com.example.demo.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    USER_POSTS_READ("user_posts:read"),
    USER_POSTS_UPDATE("user_posts:update"),
    USER_POSTS_CREATE("user_posts:create"),
    USER_POSTS_DELETE("user_posts:delete"),

    USER_USERS_READ("user_users:read"),
    USER_USERS_UPDATE("user_users:update"),
    USER_USERS_CREATE("user_users:create"),
    USER_USERS_DELETE("user_users:delete"),

    USER_ALBUMS_READ("user_albums:read"),
    USER_ALBUMS_UPDATE("user_albums:update"),
    USER_ALBUMS_CREATE("user_albums:create"),
    USER_ALBUMS_DELETE("user_albums:delete"),
    ;

    @Getter
    private final String permission;
}
