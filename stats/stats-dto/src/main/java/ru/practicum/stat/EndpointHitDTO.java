package ru.practicum.stat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointHitDTO {
    @NotEmpty(message = "App name can't be empty")
    private String app;

    @NotEmpty(message = "URI can't be empty")
    private String uri;

    @NotEmpty(message = "IP can't be empty")
    @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$", message = "Invalid IP address format")
    private String ip;

    @NotEmpty(message = "Timestamp can't be empty")
    private String timestamp;
}
