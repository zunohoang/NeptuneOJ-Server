package org.example.neptuneojserver.repositories;

import org.example.neptuneojserver.dto.submission.SubmissionResponseDTO;
import org.example.neptuneojserver.models.Submission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>{

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.id, s.problem.title, s.user.username, s.user.fullName, s.status, s.result, s.timeRun, s.memoryRun, s.testAccept, s.numberTest, s.createdAt, s.judgeStatuses) " +
            "FROM Submission s WHERE s.id = :id")
    SubmissionResponseDTO findBySubmissionId(@Param("id") Long id);


    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.id, s.problem.title, s.user.username, s.user.fullName, s.status, s.result, s.timeRun, s.memoryRun, s.testAccept, s.numberTest, s.createdAt, s.judgeStatuses) FROM Submission s ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findAllPaged(Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.id, s.problem.title, s.user.username, s.user.fullName, s.status, s.result, s.timeRun, s.memoryRun, s.testAccept, s.numberTest, s.createdAt, s.judgeStatuses) FROM Submission s WHERE s.user.username = :username ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByUserUsername(String username, Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.id, s.problem.title, s.user.username, s.user.fullName, s.status, s.result, s.timeRun, s.memoryRun, s.testAccept, s.numberTest, s.createdAt, s.judgeStatuses) FROM Submission s WHERE s.problem.id = :problemId ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByProblemId(Long problemId, Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.id, s.problem.title, s.user.username, s.user.fullName, s.status, s.result, s.timeRun, s.memoryRun, s.testAccept, s.numberTest, s.createdAt, s.judgeStatuses) FROM Submission s WHERE s.problem.id = :problemId AND s.user.username = :username ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByProblemIdAndUserUsername(Long problemId, String username, Pageable pageable);


}
