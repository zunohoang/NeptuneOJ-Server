package org.example.neptuneojserver.repositorys;

import jdk.jfr.Registered;
import org.example.neptuneojserver.models.JudgeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgeRepository extends JpaRepository<JudgeStatus, Long>{

}
