package com.example.footballManeger.controller;

import com.example.footballManeger.constant.HttpStatuses;
import com.example.footballManeger.dto.player.PlayerRequestDto;
import com.example.footballManeger.dto.player.PlayerRequestUpdateDto;
import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.service.PlayerService;
import com.example.footballManeger.mapping.PlayerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
@Validated
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    @Operation(summary = "Create new Player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<PlayerResponseDto> createPlayer(@Valid @RequestBody PlayerRequestDto playerRequestDto) {
        try {
            Player player = playerMapper.toEntity(playerRequestDto);
            Player createdPlayer = playerService.create(player);
            PlayerResponseDto responseDto = playerMapper.toDto(createdPlayer);
            return ResponseEntity.status(201).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @Operation(summary = "Update Player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequestUpdateDto playerRequestUpdateDto) {
        try {
            Player existingPlayer = playerService.findById(id);
            playerMapper.updateEntity(playerRequestUpdateDto, existingPlayer);
            Player updatedPlayer = playerService.update(existingPlayer);
            PlayerResponseDto responseDto = playerMapper.toDto(updatedPlayer);
            return ResponseEntity.ok(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @Operation(summary = "Delete Player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = HttpStatuses.NO_CONTENT),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        try {
            playerService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Operation(summary = "Find player by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDto> getPlayerById(@PathVariable Long id) {
        try {
            Player player = playerService.findById(id);
            PlayerResponseDto responseDto = playerMapper.toDto(player);
            return ResponseEntity.ok(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @Operation(summary = "Find all players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping
    public ResponseEntity<List<PlayerResponseDto>> getAllPlayers() {
        try {
            List<PlayerResponseDto> players = playerService.getAll().stream()
                    .map(playerMapper::toDto)
                    .toList();
            return ResponseEntity.ok(players);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @Operation(summary = "Transfer player")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/{playerId}/transfer/{targetTeamId}")
    public ResponseEntity<PlayerResponseDto> transferPlayer(@PathVariable Long playerId, @PathVariable Long targetTeamId) {
        try {
            Player transferredPlayer = playerService.transferPlayer(playerId, targetTeamId);
            PlayerResponseDto responseDto = playerMapper.toDto(transferredPlayer);
            return ResponseEntity.ok(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
