package com.example.footballManeger.repository;

import com.example.footballManeger.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
