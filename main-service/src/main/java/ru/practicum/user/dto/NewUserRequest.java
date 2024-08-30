package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email can't be blank")
    @Size(min = 6, max = 254, message = "The length of the user's email must be at least 6 characters and no more " +
            "than 254 characters")
    private String email;

    @NotBlank(message = "User name can't be blank")
    @Size(min = 2, max = 250, message = "The length of the user name must be at least 2 characters and no more than " +
            "250 characters")
    private String name;
}
