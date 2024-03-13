package com.example.demo.dto.auth;

import com.example.demo.entity.user.Role;
import lombok.Builder;

@Builder
public record RegisterRequest(String firstname, String lastname, String email, String password, Role role) {

}
