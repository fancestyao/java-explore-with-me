package com.github.java_explore_with_me.stats.repository;

import com.github.java_explore_with_me.stats.model.Hit;
import com.github.java_explore_with_me.stats.output_dto.OutputDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("select new com.github.java_explore_with_me.stats.output_dto.OutputDTO(h.app,h.uri, COUNT (h.ip)) "
            + "from Hit as h "
            + "where h.timestamp >= :start and h.timestamp <= :end and h.uri IN :uris "
            + "group by h.app, h.uri "
            + "order by COUNT (h.ip) desc ")
    List<OutputDTO> getStatsByUrisAndTimestamps(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end,
                                               @Param("uris") List<String> uris);

    @Query("select new com.github.java_explore_with_me.stats.output_dto.OutputDTO(h.app,h.uri,  COUNT (distinct h.ip)) "
            + "from Hit as h "
            + "where h.timestamp >= :start and h.timestamp <= :end and h.uri IN :uris "
            + "group by h.app, h.uri "
            + "order by COUNT (distinct h.ip) desc ")
    List<OutputDTO> getUniqueStatsByUrisAndTimestamps(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end,
                                                     @Param("uris") List<String> uris);

    @Query("select new com.github.java_explore_with_me.stats.output_dto.OutputDTO(h.app,h.uri,  COUNT (h.ip)) "
            + "from Hit as h "
            + "where h.timestamp >= :start and h.timestamp <= :end "
            + "group by h.app, h.uri "
            + "order by COUNT (h.ip) desc ")
    List<OutputDTO> getAllStats(@Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("select new com.github.java_explore_with_me.stats.output_dto.OutputDTO(h.app,h.uri,  COUNT (distinct h.ip)) "
            + "from Hit as h "
            + "where h.timestamp >= :start and h.timestamp <= :end "
            + "group by h.app, h.uri "
            + "order by COUNT (distinct h.ip) desc ")
    List<OutputDTO> getAllUniqueStats(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);
}
