package ru.practicum.requests.service.interfaces;

import ru.practicum.requests.dto.ParticipationRequestDto;
import java.util.List;

public interface RequestService {
    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getUserRequests(Long userId);
}
