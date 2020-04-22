package com.a6raywa1cher.muchelpspring.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Subject {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany
    private List<User> userList;
}
