package com.github.java_explore_with_me.main.requests.service.interfaces;

import com.github.java_explore_with_me.main.requests.dto.ParticipationRequestDto;
import java.util.List;

public interface RequestService {
    ParticipationRequestDto saveRequest(Long userId, Long eventId);

    ParticipationRequestDto updateRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getUserRequests(Long userId);
}
