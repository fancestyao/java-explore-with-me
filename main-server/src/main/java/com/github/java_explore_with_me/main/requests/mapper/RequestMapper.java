package com.github.java_explore_with_me.main.requests.mapper;

import com.github.java_explore_with_me.main.requests.dto.ParticipationRequestDto;
import com.github.java_explore_with_me.main.requests.model.Request;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "event", source = "request.event.id")
    @Mapping(target = "requester", source = "request.requester.id")
    ParticipationRequestDto requestToParticipationRequestDto(Request request);

    List<ParticipationRequestDto> requestListToParticipationRequestDtoList(List<Request> userRequests);
}
