package ru.practicum.stat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDTO {
    private String app;    // Название приложения
    private String uri;    // URI, по которому запрашивается статистика
    private long hits;     // Количество запросов на данный URI
}
