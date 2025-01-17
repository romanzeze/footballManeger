package com.example.footballManeger.service;

import com.example.footballManeger.exception.NullEntityReferenceException;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.PlayerRepository;
import com.example.footballManeger.repository.TeamRepository;
import com.example.footballManeger.service.impl.PlayerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player;
    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        player = new Player();
        player.setId(1L);
        player.setName("John Doe");
        player.setAge(25);
        player.setExperienceMonths(60);

        team = new Team();
        team.setId(1L);
        team.setName("Team A");
        team.setBalance(1_000_000.0);
        team.setCommission(10.0);

        player.setTeam(team);
    }

    @Test
    void create_ValidPlayer_ReturnsSavedPlayer() {
        when(playerRepository.save(player)).thenReturn(player);

        Player result = playerService.create(player);

        assertNotNull(result);
        assertEquals(player.getId(), result.getId());
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void create_NullPlayer_ThrowsException() {
        NullEntityReferenceException exception = assertThrows(NullEntityReferenceException.class, () -> playerService.create(null));
        assertEquals("Task cannot be 'null'", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void update_ValidPlayer_ReturnsUpdatedPlayer() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);

        Player result = playerService.update(player);

        assertNotNull(result);
        assertEquals(player.getId(), result.getId());
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void update_NullPlayer_ThrowsException() {
        NullEntityReferenceException exception = assertThrows(NullEntityReferenceException.class, () -> playerService.update(null));
        assertEquals("Task cannot be 'null'", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void delete_ValidId_DeletesPlayer() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        playerService.delete(player.getId());

        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    void delete_InvalidId_ThrowsException() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> playerService.delete(player.getId()));
        assertEquals("Task with id 1 not found", exception.getMessage());
        verify(playerRepository, never()).delete(any());
    }




    @Test
    void transferPlayer_PlayerNotFound_ThrowsException() {
        when(playerRepository.findById(player.getId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> playerService.transferPlayer(player.getId(), team.getId()));
        assertEquals("Player not found with ID: 1", exception.getMessage());
    }

    @Test
    void transferPlayer_InsufficientBalance_ThrowsException() {
        team.setBalance(100.0);

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> playerService.transferPlayer(player.getId(), team.getId()));
        assertEquals("Insufficient balance in target team's account.", exception.getMessage());
    }
}
