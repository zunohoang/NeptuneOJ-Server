package org.example.neptuneojserver.dto.contest;

import lombok.Data;
import org.example.neptuneojserver.models.Contest;
import org.example.neptuneojserver.models.User;

import java.util.List;

@Data
public class UserContestDTO {
    private Long rank;
    private User user;
    private Contest contest;
    private List<SubmissionContestDTO> submissions;
    private List<ProblemPoint> points;
    private Float point;
    private Float time;
    private String status;
}
