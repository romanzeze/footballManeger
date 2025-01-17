package com.example.footballManeger.service;

import com.example.footballManeger.model.Team;

import java.util.List;

public interface TeamService {
    Team create(Team team);
    Team update(Team team);
    void delete(long id);
    Team findById(long id);
    List<Team> getAll();

}
