package com.example.footballManeger.mapping;

import com.example.footballManeger.dto.player.PlayerRequestDto;
import com.example.footballManeger.dto.player.PlayerRequestUpdateDto;
import com.example.footballManeger.dto.player.PlayerResponseDto;
import com.example.footballManeger.model.Player;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerMapperTest {

    private PlayerMapper playerMapper;
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository = Mockito.mock(TeamRepository.class);
        playerMapper = new PlayerMapper(teamRepository);
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        PlayerRequestDto dto = new PlayerRequestDto();
        dto.setName("John Doe");
        dto.setExperienceMonths(24);
        dto.setAge(25);
        dto.setTeamId(1L);

        Team team = new Team();
        team.setId(1L);
        team.setName("Team A");

        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Player player = playerMapper.toEntity(dto);

        assertNotNull(player);
        assertEquals("John Doe", player.getName());
        assertEquals(24, player.getExperienceMonths());
        assertEquals(25, player.getAge());
        assertEquals(1L, player.getTeam().getId());
        assertEquals("Team A", player.getTeam().getName());
    }

    @Test
    void toEntity_ShouldThrowExceptionWhenTeamNotFound() {
        PlayerRequestDto dto = new PlayerRequestDto();
        dto.setTeamId(99L);

        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> playerMapper.toEntity(dto));
        assertEquals("Team not found with ID: 99", exception.getMessage());
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        Team team = new Team();
        team.setId(1L);
        team.setName("Team A");

        Player player = new Player();
        player.setId(100L);
        player.setName("John Doe");
        player.setExperienceMonths(24);
        player.setAge(25);
        player.setTeam(team);

        PlayerResponseDto dto = playerMapper.toDto(player);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals(24, dto.getExperienceMonths());
        assertEquals(25, dto.getAge());
        assertEquals(1L, dto.getTeamId());
        assertEquals("Team A", dto.getTeamName());
    }

    @Test
    void updateEntity_ShouldUpdateEntityFields() {
        PlayerRequestUpdateDto dto = new PlayerRequestUpdateDto();
        dto.setName("Jane Doe");
        dto.setExperienceMonths(36);
        dto.setAge(30);

        Player player = new Player();
        player.setName("Old Name");
        player.setExperienceMonths(12);
        player.setAge(20);

        playerMapper.updateEntity(dto, player);

        assertEquals("Jane Doe", player.getName());
        assertEquals(36, player.getExperienceMonths());
        assertEquals(30, player.getAge());
    }

    @Test
    void updateEntity_ShouldNotUpdateFieldsWithInvalidValues() {
        PlayerRequestUpdateDto dto = new PlayerRequestUpdateDto();
        dto.setName(null);
        dto.setExperienceMonths(-5);
        dto.setAge(17);

        Player player = new Player();
        player.setName("Valid Name");
        player.setExperienceMonths(12);
        player.setAge(20);

        playerMapper.updateEntity(dto, player);

        assertEquals("Valid Name", player.getName());
        assertEquals(12, player.getExperienceMonths());
        assertEquals(20, player.getAge());
    }
}
