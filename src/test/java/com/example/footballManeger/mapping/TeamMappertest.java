package com.example.footballManeger.mapping;


import com.example.footballManeger.dto.team.TeamRequestDto;
import com.example.footballManeger.dto.team.TeamResponseDto;
import com.example.footballManeger.model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class TeamMappertest {
    private final TeamMapper teamMapper = new TeamMapper();

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        TeamRequestDto dto = new TeamRequestDto();
        dto.setName("Team A");
        dto.setBalance(1000.0);
        dto.setCommission(0.1);

        Team team = teamMapper.toEntity(dto);

        assertNotNull(team);
        assertEquals("Team A", team.getName());
        assertEquals(1000.0, team.getBalance());
        assertEquals(0.1, team.getCommission());
    }

    @Test
    void toEntity_ShouldReturnNullWhenDtoIsNull() {
        Team team = teamMapper.toEntity(null);
        assertNull(team);
    }

    @Test
    void toResponseDto_ShouldMapEntityToDto() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Team A");
        team.setBalance(1000.0);
        team.setCommission(0.1);

        TeamResponseDto dto = teamMapper.toResponseDto(team);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Team A", dto.getName());
        assertEquals(1000.0, dto.getBalance());
        assertEquals(0.1, dto.getCommission());
    }

    @Test
    void toResponseDto_ShouldReturnNullWhenEntityIsNull() {
        TeamResponseDto dto = teamMapper.toResponseDto(null);
        assertNull(dto);
    }
}
