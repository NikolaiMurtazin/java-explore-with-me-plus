package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.stat.ViewStatsDTO;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT app, uri,  COUNT(DISTINCT ip) as hits FROM endpointhit e " +
            "WHERE e.timestamp between ?1 AND ?2 " +
            "GROUP BY app, uri", nativeQuery = true)
    List<ViewStatsDTO> findAllByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT app, uri,  COUNT(ip) as hits FROM endpointhit e " +
            "WHERE e.timestamp between ?1 AND ?2 " +
            "GROUP BY app, uri", nativeQuery = true)
    List<ViewStatsDTO> findAllByNotUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stat.ViewStatsDTO(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp between :start AND :end AND e.uri IN :uris " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDTO> findByUniqueIp(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);


    @Query("SELECT new ru.practicum.stat.ViewStatsDTO(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE (e.timestamp between ?1 AND ?2) AND e.uri IN ?3 " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDTO> findByNotUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uri);
}
