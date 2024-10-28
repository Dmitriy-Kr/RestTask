package edu.java.resttask.repository.impl;

import edu.java.resttask.entity.Training;
import edu.java.resttask.repository.DBException;
import edu.java.resttask.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static Logger logger = LoggerFactory.getLogger(TrainingRepositoryImpl.class);

    @Override
    public Optional<Training> create(Training training) throws DBException {
        try {

            entityManager.persist(training);

            return Optional.of(training);

        } catch (Exception e) {
            logger.error("Error saving Training in the database", e);
            throw new DBException("Error saving Training in the database ");
        }
    }
}
