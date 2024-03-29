package com.example.demo.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String accessToken, String refreshToken) {

}
