package ru.practicum.requests.repository;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.model.Request;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.requests.status.Status;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select new ru.practicum.requests.dto.ParticipationRequestDto(r.id, r.created, r.event.id,"
            + " r.requester.id, r.status) "
            + "from Request as r "
            + "where r.event.initiator.id = :initiatorId and r.event.id = :eventId")
    List<ParticipationRequestDto> findAllRequestForEvent(Long initiatorId, Long eventId);

    List<Request> findByEventIdAndStatus(Long eventId, Status status);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByIdInAndEventInitiatorIdAndEventId(List<Long> ids, Long userId, Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);
}
