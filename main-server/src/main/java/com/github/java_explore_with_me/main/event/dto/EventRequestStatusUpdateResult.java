package com.github.java_explore_with_me.main.event.dto;

import com.github.java_explore_with_me.main.requests.dto.ParticipationRequestDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private final List<ParticipationRequestDto> confirmedRequests;
    private final List<ParticipationRequestDto> rejectedRequests;
}
