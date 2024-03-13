package com.example.demo.dto.user;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record Address(String street, String suite, String city, String zipcode, Geo geo) implements Serializable {

}
