package com.example.footballManeger.dto.player;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlayerRequestUpdateDto {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Min(value = 0, message = "Experience months must be 0 or greater")
    private int experienceMonths;

    @Min(value = 18, message = "Age must be 18 or greater")
    private int age;
}
