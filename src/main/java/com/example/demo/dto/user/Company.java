package com.example.demo.dto.user;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Company(String name, String catchPhrase, String bs) implements Serializable {

}
