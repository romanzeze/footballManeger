package com.example.footballManeger.dto.player;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class PlayerRequestDto {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Min(value = 1, message = "Experience in months must be at least 1")
    private int experienceMonths;

    @Min(value = 1, message = "Age must be at least 1")
    private int age;

    @NotNull(message = "Team ID is mandatory")
    private Long teamId;
}

