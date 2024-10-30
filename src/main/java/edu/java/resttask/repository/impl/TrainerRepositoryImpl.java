package edu.java.resttask.repository.impl;

import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.Training;
import edu.java.resttask.repository.DBException;
import edu.java.resttask.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
//@Transactional
public class TrainerRepositoryImpl implements TrainerRepository {
    @PersistenceContext(name = "hibernate")
    private EntityManager entityManager;

    private static Logger logger = LoggerFactory.getLogger(TrainerRepositoryImpl.class);

    @Override
    public Optional<Trainer> create(Trainer trainer) throws DBException {
        try {

            entityManager.persist(trainer);

            return Optional.of(trainer);

        } catch (Exception e) {
            logger.error("Error saving Trainer in the database", e);
            throw new DBException("Error saving Trainer in the database ");
        }
    }

    @Override
    public Optional<Trainer> getTrainerByUserName(String username) throws DBException {
        Query query = entityManager.createQuery("SELECT t FROM Trainer as t WHERE t.user.username = :username", Trainer.class);
        query.setParameter("username", username);
        Trainer trainer = null;
        try {
            trainer = (Trainer) query.getSingleResult();
        } catch (NoResultException e){
            logger.error("No such Trainer present in the database with username {}", username);
            throw new DBException("No such Trainer present in the database with userName " + username);
        }
        return trainer != null ? Optional.of(trainer) : Optional.empty();
    }

    @Override
    public Optional<Trainer> changePassword(Trainer trainer) {
        Trainer trainerFromDB = entityManager.find(Trainer.class, trainer.getId());
        if (trainerFromDB != null) {
            trainerFromDB.getUser().setPassword(trainer.getUser().getPassword());
            trainer = entityManager.merge(trainerFromDB);
        }

        return Optional.ofNullable(trainer);
    }

    @Override
    public Optional<Trainer> update(Trainer trainer) {
        Trainer trainerFromDB = entityManager.find(Trainer.class, trainer.getId());
        if (trainerFromDB != null) {

            trainerFromDB.getUser().setFirstname(trainer.getUser().getFirstname());
            trainerFromDB.getUser().setLastname(trainer.getUser().getLastname());
            trainerFromDB.getUser().setUsername(trainer.getUser().getUsername());

            trainerFromDB.setSpecialization(trainer.getSpecialization());

            trainer = entityManager.merge(trainerFromDB);
        }else{
            return Optional.empty();
        }

        return Optional.ofNullable(trainer);
    }

    @Override
    public boolean changeStatus(Trainer trainer) throws DBException {
        Trainer trainerFromDB = entityManager.find(Trainer.class, trainer.getId());
        if (trainerFromDB != null) {

            trainerFromDB.getUser().setIsActive(!trainerFromDB.getUser().isActive());

            trainer = entityManager.merge(trainerFromDB);

        } else {
            logger.error("No such Trainer present in the database with id {}", trainer.getId());
            throw new DBException("No such Trainer present in the database with id " + trainer.getId());
        }

        return trainer.getUser().isActive();
    }

    @Override
    public List<Training> getTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeName) throws DBException {
        Query query = entityManager.createQuery("SELECT t FROM Trainer as t JOIN FETCH t.trainings WHERE t.user.username = :trainerUsername", Trainer.class);
        query.setParameter("trainerUsername", trainerUsername);

        try {
            Trainer trainer = (Trainer) query.getSingleResult();

            return trainer.getTrainings().stream()
                    .filter(t -> t.getTrainingDay().compareTo(fromDate) >= 0 && t.getTrainingDay().compareTo(toDate) <= 0)
                    .filter(t -> t.getTrainee().getUser().getFirstname().equals(traineeName))
                    .collect(Collectors.toList());

        } catch (NoResultException e) {
            logger.error("No such Trainer present in the database with username {}", trainerUsername);
            throw new DBException("No such Trainer present in the database with userName " + trainerUsername, e);
        }
    }

    @Override
    public List<Trainer> getAll() throws DBException {
        Query query = entityManager.createQuery("SELECT t FROM Trainer as t", Trainer.class);

        try {
            List<Trainer> result = query.getResultList();

            return result;

        } catch (Exception e) {
            logger.error("Cannot get trainers from the database");
            throw new DBException("Cannot get trainers from the database", e);
        }
    }

    @Override
    public List<Trainer> getNotAssignedOnTraineeTrainersByTraineeUsername(String traineeUsername) {
        return null;
    }
}
