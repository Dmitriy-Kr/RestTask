package edu.java.resttask.repository.impl;

import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.Training;
import edu.java.resttask.entity.TrainingType;
import edu.java.resttask.repository.DBException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Repository
//@Transactional
//public class TraineeRepositoryImpl implements TraineeRepository {
public class TraineeRepositoryImpl {
    @PersistenceContext(name = "hibernate")
    protected EntityManager entityManager;

    private static Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);

//    @Override
    public Optional<Trainee> create(Trainee trainee) {
        try {

            entityManager.persist(trainee);

            return Optional.of(trainee);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

//    @Override
    public Optional<Trainee> getTraineeByUserName(String username) throws DBException {
//        Query query = entityManager.createQuery("SELECT t FROM Trainee as t JOIN FETCH t.trainers WHERE t.user.username = :username", Trainee.class);
        Query query = entityManager.createQuery("SELECT t FROM Trainee as t WHERE t.user.username = :username", Trainee.class);
        query.setParameter("username", username);
        Trainee trainee = null;
        try {
            trainee = (Trainee) query.getSingleResult();
        } catch (NoResultException e) {
            logger.error("No such Trainee present in the database with userName {}", username);
            throw new DBException("No such Trainee present in the database with userName " + username, e);
        }
        return trainee != null ? Optional.of(trainee) : Optional.empty();
    }

//    @Override
    public Optional<Trainee> changePassword(Trainee trainee) {
        Trainee traineeFromDB = entityManager.find(Trainee.class, trainee.getId());
        if (traineeFromDB != null) {
            traineeFromDB.getUser().setPassword(trainee.getUser().getPassword());
            trainee = entityManager.merge(traineeFromDB);
        }

        return Optional.ofNullable(trainee);
    }

//    @Override
    public Optional<Trainee> update(Trainee trainee) {
        Trainee traineeFromDB = entityManager.find(Trainee.class, trainee.getId());
        if (traineeFromDB != null) {

            traineeFromDB.getUser().setFirstname(trainee.getUser().getFirstname());
            traineeFromDB.getUser().setLastname(trainee.getUser().getLastname());
            traineeFromDB.getUser().setUsername(trainee.getUser().getUsername());

            traineeFromDB.setDateOfBirth(trainee.getDateOfBirth());
            traineeFromDB.setAddress(trainee.getAddress());

            trainee = entityManager.merge(traineeFromDB);
        } else {
            return Optional.empty();
        }

        return Optional.ofNullable(trainee);
    }

//    @Override
    public boolean changeStatus(Trainee trainee) throws DBException {
        Trainee traineeFromDB = entityManager.find(Trainee.class, trainee.getId());
        if (traineeFromDB != null) {

            traineeFromDB.getUser().setIsActive(!traineeFromDB.getUser().isActive());

            trainee = entityManager.merge(traineeFromDB);

        } else {
            logger.error("No such Trainee present in the database with id {}", trainee.getId());
            throw new DBException("No such Trainee present in the database with id " + trainee.getId());
        }

        return trainee.getUser().isActive();
    }

//    @Override
    public void delete(Trainee trainee) throws DBException {

        Trainee traineeFromDB = entityManager.find(Trainee.class, trainee.getId());

        if (traineeFromDB == null) {
            logger.error("Fail to delete, no trainee with userName {} in DB ", trainee.getUser().getUsername());
            throw new DBException("Fail to delete, no trainee in DB with userName  " + trainee.getUser().getUsername());
        }

        try {
            entityManager.remove(traineeFromDB);
        } catch (Exception e) {
            logger.error("No such entity Trainee managed by entityManager, id {}", trainee.getId());
            throw new DBException("No such entity Trainee managed by entityManager, id " + trainee.getId(), e);
        }
    }

//    @Override
    public List<Training> getTrainings(String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) throws DBException {
        Query query = entityManager.createQuery("SELECT t FROM Trainee as t JOIN FETCH t.trainings WHERE t.user.username = :username", Trainee.class);
        query.setParameter("username", traineeUsername);

        try {
            Trainee trainee = (Trainee) query.getSingleResult();

            return trainee.getTrainings().stream()
                    .filter(t -> t.getTrainingDay().compareTo(fromDate) >= 0 && t.getTrainingDay().compareTo(toDate) <= 0)
                    .filter(t -> t.getTrainer().getUser().getFirstname().equals(trainerName))
                    .filter(t -> t.getTrainingType().equals(trainingType))
                    .collect(Collectors.toList());

        } catch (NoResultException e) {
            logger.error("No such Trainee present in the database with username {}", traineeUsername);
            throw new DBException("No such Trainee present in the database with username " + traineeUsername, e);
        }
    }

//    @Override
    public Optional<Trainee> updateTrainersList(Trainee trainee, List<Trainer> trainersList) {

        Trainee traineeFromDB = entityManager.find(Trainee.class, trainee.getId());

        if (traineeFromDB != null) {

            traineeFromDB.setTrainers(trainersList);

            traineeFromDB = entityManager.merge(traineeFromDB);

        } else {
            logger.info("No such Trainee present in the database with id {}", trainee.getId());
            return Optional.empty();
        }

        return Optional.ofNullable(traineeFromDB);
    }
}
