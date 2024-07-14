package org.example.neptuneojserver.repositorys;

import org.example.neptuneojserver.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>{
}
