package com.example.footballManeger.dto.team;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TeamResponseDto {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @PositiveOrZero
    private Double balance;
    @PositiveOrZero
    private Double commission;

}

