package com.example.resturant_management_system.model.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Instant expiryDate;

    public ResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = Instant.now().plus(1, ChronoUnit.HOURS); // Token valid for 1 hour
    }

    public ResetToken() {}
}
