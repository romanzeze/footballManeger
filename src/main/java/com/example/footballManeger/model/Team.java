package com.example.footballManeger.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;


@Entity
@Table
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double balance;

    private Double commission;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Player> players = new ArrayList<>();

}