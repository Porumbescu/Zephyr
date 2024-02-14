package com.porumb.zephyr.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    private String role;
    private int streak;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastQuizDate;
}
