package com.example.footballManeger.service.impl;


import com.example.footballManeger.exception.NullEntityReferenceException;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.PlayerRepository;
import com.example.footballManeger.repository.TeamRepository;
import com.example.footballManeger.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Override
    public Player create(Player player) {
        if (player != null) {
            return playerRepository.save(player);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }

    @Override
    public Player update(Player player) {
        if (player != null) {
            findById(player.getId());
            return playerRepository.save(player);
        }
        throw new NullEntityReferenceException("Task cannot be 'null'");
    }


    @Override
    public void delete(long id) {
        Player player = findById(id);
        playerRepository.delete(player);
    }

    @Override
    public Player findById(long id) {
        EntityNotFoundException exception = new EntityNotFoundException("Task with id " + id + " not found");
        logger.error(exception.getMessage(), exception);
        return playerRepository.findById(id).orElseThrow(
                () -> exception);

    }

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player transferPlayer(Long playerId, Long targetTeamId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        Team targetTeam = teamRepository.findById(targetTeamId)
                .orElseThrow(() -> new RuntimeException("Target team not found with ID: " + targetTeamId));

        Team currentTeam = player.getTeam();
        if (currentTeam == null) {
            throw new RuntimeException("Player does not belong to any team.");
        }

        double transferValue = (player.getExperienceMonths() * 100_000.0) / player.getAge();
        double commission = transferValue * (currentTeam.getCommission() / 100.0);
        double totalCost = transferValue + commission;

        if (targetTeam.getBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance in target team's account.");
        }

        targetTeam.setBalance(targetTeam.getBalance() - totalCost);
        currentTeam.setBalance(currentTeam.getBalance() + totalCost);

        player.setTeam(targetTeam);

        teamRepository.save(currentTeam);
        teamRepository.save(targetTeam);
        return playerRepository.save(player);
    }
}
