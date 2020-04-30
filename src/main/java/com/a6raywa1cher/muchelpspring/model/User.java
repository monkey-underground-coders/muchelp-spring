package com.a6raywa1cher.muchelpspring.model;

import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@ToString(exclude = {"mySubjects", "lastTicket"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @GeneratedValue
    @JsonView(Views.Public.class)
    private Long id;

    @Column(unique = true, length = 1024)
    @JsonView(Views.Internal.class)
    private String googleId;

    @Column(unique = true, length = 1024)
    @JsonView(Views.Internal.class)
    private String vkId;

    @Column(unique = true, nullable = false, length = 1024)
    @JsonView(Views.Internal.class)
    private String email;

    @Column
    @JsonView(Views.Public.class)
    private String name;

    @ManyToMany(mappedBy = "userList")
    @JsonView(Views.Public.class)
    private List<Subject> mySubjects;

    @OneToOne
    @JsonView(Views.Public.class)
    private Ticket lastTicket;

    @ElementCollection
    @MapKeyClass(TicketStatus.class)
    @JsonView(Views.Public.class)
    private Map<TicketStatus, Integer> statistics;

    @Column
    @JsonView(Views.Public.class)
    private String picture;

    @Column
    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @Column
    @JsonView(Views.Public.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastVisit;
}
