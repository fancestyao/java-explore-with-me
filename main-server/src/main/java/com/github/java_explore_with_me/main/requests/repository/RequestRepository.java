package com.github.java_explore_with_me.main.requests.repository;

import com.github.java_explore_with_me.main.requests.dto.ParticipationRequestDto;
import com.github.java_explore_with_me.main.requests.model.Request;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select new com.github.java_explore_with_me.main.requests.dto.ParticipationRequestDto(r.id, r.created, r.event.id,"
            + " r.requester.id, r.status) "
            + "from Request as r "
            + "where r.event.initiator.id = :initiatorId and r.event.id = :eventId")
    List<ParticipationRequestDto> findAllRequestForEvent(Long initiatorId, Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByIdInAndEventInitiatorIdAndEventId(List<Long> ids, Long userId, Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);
}
