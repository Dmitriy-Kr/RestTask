package edu.java.resttask.service;

import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.Training;
import edu.java.resttask.entity.TrainingType;
import edu.java.resttask.repository.DBException;
import edu.java.resttask.repository.TraineeRepository;
import edu.java.resttask.repository.TrainerRepository;
import edu.java.resttask.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static edu.java.resttask.utility.PasswordGenerator.generatePassword;

@Service
@Transactional(readOnly = true)
public class TraineeService {

    private TraineeRepository traineeRepository;
    private UserRepository userRepository;
    private TrainerRepository trainerRepository;
    private static Logger logger = LoggerFactory.getLogger(edu.java.resttask.service.TraineeService.class);

    public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository, TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
    }

    @Transactional
    public Trainee save(Trainee trainee) {
        trainee.getUser().setUsername(createValidUserName(trainee));
        trainee.getUser().setPassword(generatePassword());
        return traineeRepository.save(trainee);
    }

    public Optional<Trainee> usernameAndPasswordMatching(String username, String password) throws ServiceException {
        Optional<Trainee> trainee = getTraineeByUsername(username);
        if (trainee.isPresent()) {
            if (password.equals(trainee.get().getUser().getPassword())) {
                return trainee;
            }
        }
        return Optional.empty();
    }

    public Optional<Trainee> getTraineeByUsername(String username) throws ServiceException {
        try {
            return traineeRepository.findByUsername(username);
        } catch (Exception e) {
            logger.error("Fail to get trainee with userName {}  from DB", username);
            throw new ServiceException("Fail to get from DB trainee with userName " + username, e);
        }
    }

    public Optional<Trainee> changePassword(Trainee trainee) {
        return traineeRepository.changePassword(trainee);
    }

    public Optional<Trainee> update(Trainee trainee) {
        return traineeRepository.update(trainee);
    }

    public boolean changeStatus(Trainee trainee) throws ServiceException {
        try {
            return traineeRepository.changeStatus(trainee);
        } catch (DBException e) {
            logger.error("Fail to change status trainee with userName {}  ", trainee.getUser().getUsername());
            throw new ServiceException("Fail to change trainee status", e);
        }
    }

    public void deleteByUsername(String username) throws ServiceException {
        Optional<Trainee> traineeFromDB = getTraineeByUsername(username);

        if (traineeFromDB.isEmpty()) {
            logger.error("Fail to delete, no trainee with userName {} in DB ", username);
            throw new ServiceException("Fail to delete, no trainee with userName {} in DB " + username);
        }

        try {
            traineeRepository.delete(traineeFromDB.get());
        } catch (DBException e) {
            logger.error("Fail to delete trainee with userName {} from DB ", username);
            throw new ServiceException("Fail to delete trainee from DB with userName" + username, e);
        }
    }

    public List<Training> getTrainings(String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) throws ServiceException {

        try {
            return traineeRepository.getTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
        } catch (DBException e) {
            logger.error("Fail to get trainee trainings from DB - userName {} ", traineeUsername);
            throw new ServiceException("Fail to get trainee trainings from DB - userName " + traineeUsername, e);
        }
    }

    public Optional<Trainee> updateTrainersList(String traineeUsername, List<Trainer> trainersList) throws ServiceException {
        Optional<Trainee> traineeFromDB = getTraineeByUsername(traineeUsername);

        if (traineeFromDB.isPresent()) {
            List<Trainer> traineeTrainersList = traineeFromDB.get().getTrainers();

            for (Trainer trainer : trainersList) {

                Optional<Trainer> trainerFromDB = trainerService.getTrainerByUserName(trainer.getUser().getUsername());

                if(trainerFromDB.isPresent()) {

                    if(!traineeTrainersList.contains(trainerFromDB.get())){

                        traineeTrainersList.add(trainerFromDB.get());
                    }
                }
            }

            try {

                return traineeRepository.updateTrainersList(traineeFromDB.get(), traineeTrainersList);

            } catch (DBException e) {
                logger.error("Fail to update trainee trainers list - trainee id {} ", traineeFromDB.get().getId());
                throw new ServiceException("Fail to update trainee trainers list - trainee id " + traineeFromDB.get().getId(), e);
            }
        }

        return Optional.empty();
    }

    private String createValidUserName(Trainee trainee) {

        String username = trainee.getUser().getFirstname() + "." + trainee.getUser().getLastname();

        if (userRepository.findByUsername(username).isEmpty()) {
            return username;
        }

        for (long i = 0; i < Long.MAX_VALUE; i++) {
            StringBuilder newUsername = new StringBuilder(username + i);
            if (userRepository.findByUsername(newUsername.toString()).isEmpty()) {
                return newUsername.toString();
            }
        }

        return username;
    }
}
