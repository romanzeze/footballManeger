package com.example.footballManeger.mapping;

import com.example.footballManeger.dto.player.PlayerRequestDto;
import com.example.footballManeger.dto.player.PlayerRequestUpdateDto;
import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    private final TeamRepository teamRepository;

    public PlayerMapper(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Player toEntity(PlayerRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Player player = new Player();
        player.setName(dto.getName());
        player.setExperienceMonths(dto.getExperienceMonths());
        player.setAge(dto.getAge());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with ID: " + dto.getTeamId()));
            player.setTeam(team);
        }

        return player;
    }


    public PlayerResponseDto toDto(Player player) {
        if (player == null) {
            return null;
        }
        return PlayerResponseDto.builder()
                .id(player.getId())
                .name(player.getName())
                .experienceMonths(player.getExperienceMonths())
                .age(player.getAge())
                .teamId(player.getTeam() != null ? player.getTeam().getId() : null)
                .teamName(player.getTeam() != null ? player.getTeam().getName() : null)
                .build();
    }

    public void updateEntity(PlayerRequestUpdateDto dto, Player player) {
        if (dto.getName() != null) {
            player.setName(dto.getName());
        }
        if (dto.getExperienceMonths() > 0) {
            player.setExperienceMonths(dto.getExperienceMonths());
        }
        if (dto.getAge() >= 18) { // Перевірка, щоб уникнути некоректних значень
            player.setAge(dto.getAge());
        }
    }
}

