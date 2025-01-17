package com.example.footballManeger.service;

import com.example.footballManeger.exception.NullEntityReferenceException;
import com.example.footballManeger.exception.ResourceNotFoundException;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.TeamRepository;
import com.example.footballManeger.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        team = new Team();
        team.setId(1L);
        team.setName("Team A");
        team.setBalance(1_000_000.0);
        team.setCommission(10.0);
    }

    @Test
    void create_ValidTeam_ReturnsSavedTeam() {
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.create(team);

        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void create_NullTeam_ThrowsException() {
        NullEntityReferenceException exception = assertThrows(NullEntityReferenceException.class, () -> teamService.create(null));
        assertEquals("Team cannot be 'null'", exception.getMessage());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void findById_ValidId_ReturnsTeam() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        Team result = teamService.findById(team.getId());

        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        verify(teamRepository, times(1)).findById(team.getId());
    }

    @Test
    void findById_InvalidId_ThrowsException() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> teamService.findById(team.getId()));
        assertEquals("Team with id 1 not found", exception.getMessage());
        verify(teamRepository, times(1)).findById(team.getId());
    }

    @Test
    void update_ValidTeam_ReturnsUpdatedTeam() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));
        when(teamRepository.save(team)).thenReturn(team);

        Team result = teamService.update(team);

        assertNotNull(result);
        assertEquals(team.getId(), result.getId());
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void update_NullTeam_ThrowsException() {
        NullEntityReferenceException exception = assertThrows(NullEntityReferenceException.class, () -> teamService.update(null));
        assertEquals("Team cannot be 'null' ", exception.getMessage());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void delete_ValidId_DeletesTeam() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.of(team));

        teamService.delete(team.getId());

        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    void delete_InvalidId_ThrowsException() {
        when(teamRepository.findById(team.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> teamService.delete(team.getId()));
        assertEquals("Team with id 1 not found", exception.getMessage());
        verify(teamRepository, never()).delete(any());
    }

    @Test
    void getAll_ReturnsListOfTeams() {
        List<Team> teams = Arrays.asList(team, new Team());
        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teamRepository, times(1)).findAll();
    }
}
