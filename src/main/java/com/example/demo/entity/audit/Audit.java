package com.example.demo.entity.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String email;

    @Column(length = 1000)
    private String roles;
    private String action;

    @Column(columnDefinition = "TEXT")
    private String requestParams;
    private boolean success;
}