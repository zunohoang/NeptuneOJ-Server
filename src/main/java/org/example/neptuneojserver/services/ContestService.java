package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.contest.ContestDTO;
import org.example.neptuneojserver.models.Contest;
import org.example.neptuneojserver.repositories.ContestRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContestService {

    private final ContestRepository contestRepository;

    public List<ContestDTO> getContests(String type, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if(type.equals("current")) {
            return contestRepository.findByCurrent(pageable).getContent();
        } else if(type.equals("future")) {
            return contestRepository.findByFuture(pageable).getContent();
        } else if(type.equals("past")) {
            return contestRepository.findByPast(pageable).getContent();
        } else return null;
    }

    private ContestDTO createContest(Contest contest) {

    }
}
