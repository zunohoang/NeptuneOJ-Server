package org.example.neptuneojserver.repositories;

import org.example.neptuneojserver.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);

    @Query("SELECT u FROM User u order by u.point DESC")
    Page<User> findAllPaged(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.point >= :oldPoint AND u.point <= :newPoint")
    List<User> findByPointBetween(Float oldPoint, Float newPoint);
}
