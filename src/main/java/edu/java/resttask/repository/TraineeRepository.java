package edu.java.resttask.repository;

import edu.java.resttask.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
//    Optional<Trainee> findByUsername(String userName);
}
