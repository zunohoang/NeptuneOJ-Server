package org.example.neptuneojserver.repositories;

import org.example.neptuneojserver.dto.contest.ContestDTO;
import org.example.neptuneojserver.models.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContestRepository extends JpaRepository<Contest, Long> {

    // get contest by time
    @Query("SELECT  new org.example.neptuneojserver.dto.contest.ContestDTO(c.id, c.title, c.startTime, c.endTime, c.numberOfParticipants) FROM Contest c WHERE c.startTime <= CURRENT_TIMESTAMP AND c.endTime >= CURRENT_TIMESTAMP")
    Page<ContestDTO> findByCurrent(Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.contest.ContestDTO(c.id, c.title, c.startTime, c.endTime, c.numberOfParticipants) FROM Contest c WHERE c.startTime > CURRENT_TIMESTAMP")
    Page<ContestDTO> findByFuture(Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.contest.ContestDTO(c.id, c.title, c.startTime, c.endTime, c.numberOfParticipants) FROM Contest c WHERE c.endTime < CURRENT_TIMESTAMP")
    Page<ContestDTO> findByPast(Pageable pageable);

}
