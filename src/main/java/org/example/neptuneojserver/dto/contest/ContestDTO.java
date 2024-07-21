package org.example.neptuneojserver.dto.contest;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ContestDTO {

    private Long id;
    private String title;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private Long numberOfParticipants;

    public ContestDTO(Long id, String title, ZonedDateTime startTime, ZonedDateTime endTime, Long numberOfParticipants) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numberOfParticipants = numberOfParticipants;
    }
}
