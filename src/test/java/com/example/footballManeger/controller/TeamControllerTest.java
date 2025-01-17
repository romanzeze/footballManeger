package com.example.footballManeger.controller;

import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.dto.team.TeamRequestDto;
import com.example.footballManeger.dto.team.TeamResponseDto;
import com.example.footballManeger.mapping.PlayerMapper;
import com.example.footballManeger.mapping.TeamMapper;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private TeamController teamController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTeam_Success() {
        TeamRequestDto teamRequestDto = new TeamRequestDto();
        Team team = new Team();
        Team createdTeam = new Team();
        TeamResponseDto teamResponseDto = new TeamResponseDto();

        when(teamMapper.toEntity(teamRequestDto)).thenReturn(team);
        when(teamService.create(team)).thenReturn(createdTeam);
        when(teamMapper.toResponseDto(createdTeam)).thenReturn(teamResponseDto);

        ResponseEntity<?> response = teamController.createTeam(teamRequestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(teamResponseDto, response.getBody());
    }

    @Test
    void createTeam_BadRequest() {
        TeamRequestDto teamRequestDto = new TeamRequestDto();

        when(teamMapper.toEntity(teamRequestDto)).thenThrow(new RuntimeException("Invalid data"));

        ResponseEntity<?> response = teamController.createTeam(teamRequestDto);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void updateTeam_Success() {
        Long id = 1L;
        TeamRequestDto teamRequestDto = new TeamRequestDto();
        Team team = new Team();
        Team updatedTeam = new Team();
        TeamResponseDto teamResponseDto = new TeamResponseDto();

        when(teamMapper.toEntity(teamRequestDto)).thenReturn(team);
        when(teamService.update(any(Team.class))).thenReturn(updatedTeam);
        when(teamMapper.toResponseDto(updatedTeam)).thenReturn(teamResponseDto);

        ResponseEntity<?> response = teamController.update(id, teamRequestDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teamResponseDto, response.getBody());
    }

    @Test
    void deleteTeam_Success() {
        Long id = 1L;

        doNothing().when(teamService).delete(id);

        ResponseEntity<?> response = teamController.delete(id);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getAllTeams_Success() {
        TeamResponseDto teamResponseDto = new TeamResponseDto();
        when(teamService.getAll()).thenReturn(Collections.singletonList(new Team()));
        when(teamMapper.toResponseDto(any(Team.class))).thenReturn(teamResponseDto);

        ResponseEntity<?> response = teamController.getAllTeams();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(((List<?>) response.getBody()).contains(teamResponseDto));
    }

    @Test
    void getById_Success() {
        Long id = 1L;
        Team team = new Team();
        TeamResponseDto teamResponseDto = new TeamResponseDto();

        when(teamService.findById(id)).thenReturn(team);
        when(teamMapper.toResponseDto(team)).thenReturn(teamResponseDto);

        ResponseEntity<?> response = teamController.getById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teamResponseDto, response.getBody());
    }

    @Test
    void getAllPlayersByTeamId_Success() {
        Long id = 1L;
        Team team = new Team();
        PlayerResponseDto playerResponseDto = new PlayerResponseDto();

        when(teamService.findById(id)).thenReturn(team);
        when(playerMapper.toDto(any())).thenReturn(playerResponseDto);

        ResponseEntity<?> response = teamController.getAllPlayersByTeamId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }
}
