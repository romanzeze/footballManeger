package com.example.footballManeger.controller;

import com.example.footballManeger.dto.player.PlayerRequestDto;
import com.example.footballManeger.dto.player.PlayerRequestUpdateDto;
import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.mapping.PlayerMapper;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PlayerControllerTest {

    private final PlayerService playerService = Mockito.mock(PlayerService.class);
    private final PlayerMapper playerMapper = Mockito.mock(PlayerMapper.class);
    private final PlayerController playerController = new PlayerController(playerService, playerMapper);

    @Test
    void createPlayer_ShouldReturnCreatedPlayer() {
        PlayerRequestDto requestDto = new PlayerRequestDto();
        requestDto.setName("John Doe");

        Player player = new Player();
        player.setName("John Doe");

        PlayerResponseDto responseDto = new PlayerResponseDto();
        responseDto.setName("John Doe");

        when(playerMapper.toEntity(requestDto)).thenReturn(player);
        when(playerService.create(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(responseDto);

        ResponseEntity<PlayerResponseDto> response = playerController.createPlayer(requestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void updatePlayer_ShouldReturnUpdatedPlayer() {
        Long id = 1L;
        PlayerRequestUpdateDto updateDto = new PlayerRequestUpdateDto();
        updateDto.setName("Jane Doe");

        Player player = new Player();
        player.setName("Old Name");

        Player updatedPlayer = new Player();
        updatedPlayer.setName("Jane Doe");

        PlayerResponseDto responseDto = new PlayerResponseDto();
        responseDto.setName("Jane Doe");

        when(playerService.findById(id)).thenReturn(player);
        doNothing().when(playerMapper).updateEntity(updateDto, player);
        when(playerService.update(player)).thenReturn(updatedPlayer);
        when(playerMapper.toDto(updatedPlayer)).thenReturn(responseDto);

        ResponseEntity<PlayerResponseDto> response = playerController.updatePlayer(id, updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Jane Doe", response.getBody().getName());
    }

    @Test
    void deletePlayer_ShouldReturnNoContent() {
        Long id = 1L;

        doNothing().when(playerService).delete(id);

        ResponseEntity<Void> response = playerController.deletePlayer(id);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getPlayerById_ShouldReturnPlayer() {
        Long id = 1L;
        Player player = new Player();
        player.setName("John Doe");

        PlayerResponseDto responseDto = new PlayerResponseDto();
        responseDto.setName("John Doe");

        when(playerService.findById(id)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(responseDto);

        ResponseEntity<PlayerResponseDto> response = playerController.getPlayerById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void getAllPlayers_ShouldReturnListOfPlayers() {
        Player player = new Player();
        player.setName("John Doe");

        PlayerResponseDto responseDto = new PlayerResponseDto();
        responseDto.setName("John Doe");

        when(playerService.getAll()).thenReturn(Collections.singletonList(player));
        when(playerMapper.toDto(player)).thenReturn(responseDto);

        ResponseEntity<List<PlayerResponseDto>> response = playerController.getAllPlayers();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void transferPlayer_ShouldReturnTransferredPlayer() {
        Long playerId = 1L;
        Long targetTeamId = 2L;

        Player player = new Player();
        player.setName("John Doe");

        PlayerResponseDto responseDto = new PlayerResponseDto();
        responseDto.setName("John Doe");

        when(playerService.transferPlayer(playerId, targetTeamId)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(responseDto);

        ResponseEntity<PlayerResponseDto> response = playerController.transferPlayer(playerId, targetTeamId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }
}
