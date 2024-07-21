package org.example.neptuneojserver.repositories;

import org.example.neptuneojserver.models.JudgeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeRepository extends JpaRepository<JudgeStatus, Long>{

}
