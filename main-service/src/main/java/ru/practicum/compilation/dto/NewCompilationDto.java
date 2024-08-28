package ru.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewCompilationDto {
    private List<Long> events;

    private boolean pinned = false;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 1, max = 50, message = "The length of the title must be at least 1 characters and no more than " +
            "50 characters")
    private String title;
}
