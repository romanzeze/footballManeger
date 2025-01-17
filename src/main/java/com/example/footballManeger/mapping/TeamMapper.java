package com.example.footballManeger.mapping;

import com.example.footballManeger.dto.team.TeamRequestDto;
import com.example.footballManeger.dto.team.TeamResponseDto;
import com.example.footballManeger.model.Team;
import org.springframework.stereotype.Component;


@Component
public class TeamMapper {
    public Team toEntity(TeamRequestDto teamDto) {
        if (teamDto == null) {
            return null;
        }
        Team team = new Team();
        team.setName(teamDto.getName());
        team.setBalance(teamDto.getBalance());
        team.setCommission(teamDto.getCommission());
        return team;
    }

    public TeamResponseDto toResponseDto(Team team) {
        if (team == null) {
            return null;
        }
        return TeamResponseDto.builder()
                .id(team.getId())
                .name(team.getName())
                .balance(team.getBalance())
                .commission(team.getCommission())
                .build();
    }
}
