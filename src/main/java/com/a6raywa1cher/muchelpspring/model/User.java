package com.a6raywa1cher.muchelpspring.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, length = 1024)
    private String googleId;

    @Column(unique = true, length = 1024)
    private String vkId;

    @Column(unique = true, nullable = false, length = 1024)
    private String email;

    @Column
    private String name;

    @ManyToMany(mappedBy = "userList")
    private List<Subject> mySubjects;

    @OneToOne
    private Ticket lastTicket;

    @Column
    private String picture;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastVisit;
}
