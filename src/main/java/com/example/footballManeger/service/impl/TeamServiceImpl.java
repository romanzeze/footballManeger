package com.example.footballManeger.service.impl;

import com.example.footballManeger.exception.NullEntityReferenceException;
import com.example.footballManeger.exception.ResourceNotFoundException;
import com.example.footballManeger.model.Team;
import com.example.footballManeger.repository.TeamRepository;
import com.example.footballManeger.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team create(Team team) {
        if (team != null) {
            return teamRepository.save(team);
        }
        throw new NullEntityReferenceException("Team cannot be 'null'");
    }

    @Override
    public Team findById(long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Team with id " + id + " not found"));
    }

    @Override
    public Team update(Team team) {
        if (team != null) {
            findById(team.getId());
            return teamRepository.save(team);
        }
        throw new NullEntityReferenceException("Team cannot be 'null' ");
    }

    @Override
    public void delete(long id) {
        Team team = findById(id);
        teamRepository.delete(team);

    }

    @Override
    public List<Team> getAll() {
        return teamRepository.findAll();
    }
}
