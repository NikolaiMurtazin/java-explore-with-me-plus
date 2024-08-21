package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT hit FROM EndpointHit e" +
            "WHERE e.timestamp > ?1 AND e.timestamp < ?2 AND " +
            "WHERE e.id IN (" +
            "SELECT MIN(id)" +
            "FROM ENDPOINTHIT" +
            "GROUP BY ip)")
        //надо доделать
    List<EndpointHit> findAllUniq(LocalDateTime start, LocalDateTime end);
}
