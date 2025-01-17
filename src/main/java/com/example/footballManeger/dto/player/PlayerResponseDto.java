package com.example.footballManeger.dto.player;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerResponseDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @PositiveOrZero
    private int experienceMonths;
    @PositiveOrZero
    private int age;

    private Long teamId;
    private String teamName;
}