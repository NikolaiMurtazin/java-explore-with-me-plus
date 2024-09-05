package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.RequestStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}