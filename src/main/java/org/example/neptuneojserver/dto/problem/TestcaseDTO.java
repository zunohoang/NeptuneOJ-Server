package org.example.neptuneojserver.dto.problem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestcaseDTO {
    private Long id;
    private String input;
    private String output;
    private Long indexInProblem;
}
