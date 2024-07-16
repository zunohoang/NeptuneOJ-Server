package org.example.neptuneojserver.repositorys;

import jakarta.annotation.Nullable;
import org.example.neptuneojserver.dto.submission.SubmissionResponseDTO;
import org.example.neptuneojserver.models.Submission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>{

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.title, s.user.fullName, s.result, s.status, s.createdAt) FROM Submission s WHERE s.id = :id")
    SubmissionResponseDTO findBySubmissionId(Long id);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.title, s.user.fullName, s.result, s.status, s.createdAt) FROM Submission s ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findAllPaged(Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.title, s.user.fullName, s.result, s.status, s.createdAt) FROM Submission s WHERE s.user.username = :username ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByUserUsername(String username, Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.title, s.user.fullName, s.result, s.status, s.createdAt) FROM Submission s WHERE s.problem.id = :problemId ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByProblemId(Long problemId, Pageable pageable);

    @Query("SELECT new org.example.neptuneojserver.dto.submission.SubmissionResponseDTO(s.id, s.problem.title, s.user.fullName, s.result, s.status, s.createdAt) FROM Submission s WHERE s.problem.id = :problemId AND s.user.username = :username ORDER BY s.createdAt DESC")
    Page<SubmissionResponseDTO> findByProblemIdAndUserUsername(Long problemId, String username, Pageable pageable);


}
