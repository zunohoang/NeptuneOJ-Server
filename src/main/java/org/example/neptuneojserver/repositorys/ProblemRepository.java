package org.example.neptuneojserver.repositorys;

import org.example.neptuneojserver.models.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("SELECT p FROM Problem p WHERE p.hidden = false ORDER BY p.createdAt DESC")
    Page<Problem> findAllPaged(Pageable pageable);

    @Query("SELECT p FROM Problem p, Submission s WHERE s.user.username = :username AND p.id = s.problem.id AND s.result = 'Completed' ORDER BY p.createdAt DESC")
    List<Problem> findProblemsAcceptedByUsername(String username);

    @Query("SELECT p FROM Problem p WHERE p.title LIKE %:search%")
    Page<Problem> findByTitleContaining(String search, Pageable pageable);

    @Query("SELECT p FROM Problem p JOIN p.problemTags pt JOIN pt.tag t WHERE t.title = :tagName")
    Page<Problem> findByTagName(String tagName, Pageable pageable);
}
