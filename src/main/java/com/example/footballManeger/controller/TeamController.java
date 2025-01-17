package com.example.footballManeger.controller;

import com.example.footballManeger.constant.HttpStatuses;
import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.dto.team.TeamRequestDto;
import com.example.footballManeger.dto.team.TeamResponseDto;
import com.example.footballManeger.mapping.PlayerMapper;
import com.example.footballManeger.mapping.TeamMapper;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.service.TeamService;
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
import java.util.stream.Collectors;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final PlayerMapper playerMapper;

    @Operation(summary = "Create new Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequestDto teamDto) {
        try {
            Team team = teamMapper.toEntity(teamDto);
            Team createdTeam = teamService.create(team);
            return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toResponseDto(createdTeam));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Update Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TeamRequestDto teamDto) {
        try {
            Team team = teamMapper.toEntity(teamDto);
            team.setId(id);
            Team updatedTeam = teamService.update(team);
            return ResponseEntity.ok(teamMapper.toResponseDto(updatedTeam));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = HttpStatuses.NO_CONTENT),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            teamService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @Operation(summary = "Find all team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping
    public ResponseEntity<?> getAllTeams() {
        try {
            List<TeamResponseDto> teams = teamService.getAll().stream()
                    .map(teamMapper::toResponseDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Find team by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Team team = teamService.findById(id);
            return ResponseEntity.ok(teamMapper.toResponseDto(team));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Find all players in team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}/players")
    public ResponseEntity<?> getAllPlayersByTeamId(@PathVariable Long id) {
        try {
            Team team = teamService.findById(id);
            if (team == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
            }
            List<PlayerResponseDto> players = team.getPlayers().stream()
                    .map(playerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(players);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
