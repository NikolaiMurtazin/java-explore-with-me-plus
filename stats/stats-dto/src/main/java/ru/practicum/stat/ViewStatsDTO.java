package ru.practicum.stat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStatsDTO {
    private String app;    // Название приложения
    private String uri;    // URI, по которому запрашивается статистика
    private long hits;     // Количество запросов на данный URI
}
