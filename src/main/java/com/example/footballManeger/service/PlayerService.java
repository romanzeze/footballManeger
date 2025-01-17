package com.example.footballManeger.service;

import com.example.footballManeger.model.Player;


import java.util.List;

public interface PlayerService {
    Player create(Player player);
    Player update(Player team);
    void delete(long id);
    Player findById(long id);
    List<Player> getAll();
    Player transferPlayer(Long playerId, Long targetTeamId);

}
