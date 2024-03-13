package com.example.demo.dto.user;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Geo(String lat, String lng) implements Serializable {

}
