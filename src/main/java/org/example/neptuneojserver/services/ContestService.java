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

    public ContestDTO createContest(Contest contest) {
        contestRepository.save(contest);
        return new ContestDTO(contest.getId(), contest.getTitle(), contest.getStartTime(), contest.getEndTime(), contest.getNumberOfParticipants());
    }

    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }

    public void updateContest(Contest contest) {
        contestRepository.save(contest);
    }

    public ContestDTO getContest(Long id) {
        Contest contest = contestRepository.findById(id).orElse(null);
        if(contest == null) return null;
        return new ContestDTO(contest.getId(), contest.getTitle(), contest.getStartTime(), contest.getEndTime(), contest.getNumberOfParticipants());
    }

    private List<ContestDTO> getContestsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return contestRepository.findByTitle(title, pageable);
    }
}
