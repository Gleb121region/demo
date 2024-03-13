package com.example.demo.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.entity.user.Permission.*;


@RequiredArgsConstructor
public enum Role {

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE
            )
    ),
    POSTS(
            Set.of(
                    USER_POSTS_READ,
                    USER_POSTS_UPDATE,
                    USER_POSTS_DELETE,
                    USER_POSTS_CREATE
            )
    ),
    USERS(
            Set.of(
                    USER_USERS_READ,
                    USER_USERS_UPDATE,
                    USER_USERS_DELETE,
                    USER_USERS_CREATE
            )
    ),
    ALBUMS(
            Set.of(
                    USER_ALBUMS_READ,
                    USER_ALBUMS_UPDATE,
                    USER_ALBUMS_DELETE,
                    USER_ALBUMS_CREATE
            )
    );


    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
