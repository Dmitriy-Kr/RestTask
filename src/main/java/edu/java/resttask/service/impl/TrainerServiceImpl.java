package edu.java.resttask.service.impl;

import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.Training;
import edu.java.resttask.entity.TrainingType;
import edu.java.resttask.repository.DBException;
import edu.java.resttask.repository.TrainerRepository;
import edu.java.resttask.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static edu.java.resttask.utility.PasswordGenerator.generatePassword;

@Service
public class TrainerServiceImpl implements TrainerService {

    private TrainerRepository trainerRepository;
    private edu.java.resttask.service.UserService userService;
    private TrainingTypeService trainingTypeService;
    private TraineeService traineeService;

    private static Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              UserService userService,
                              TrainingTypeService trainingTypeService,
                              @Lazy TraineeService traineeService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
        this.traineeService = traineeService;
    }

    @Override
    public Optional<Trainer> save(Trainer trainer) throws ServiceException {
        trainer.getUser().setUsername(createValidUserName(trainer));
        trainer.getUser().setPassword(generatePassword());
        try {
            return trainerRepository.create(trainer);
        } catch (DBException e) {
            logger.error("Error saving Trainer in the database with username {}", trainer.getUser().getUsername());
            throw new ServiceException("Error saving Trainer in the database", e);
        }
    }

    @Override
    public Optional<Trainer> usernameAndPasswordMatching(String userName, String password) throws ServiceException {
        Optional<Trainer> trainer = getTrainerByUserName(userName);
        if(trainer.isPresent()){
            if(password.equals(trainer.get().getUser().getPassword())){
                return trainer;
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Trainer> getTrainerByUserName(String username) throws ServiceException {
        try {
            return trainerRepository.getTrainerByUserName(username);
        } catch (DBException e) {
            logger.error("No such Trainer present in the database with userName {}", username);
            throw new ServiceException("No such Trainer present in the database with userName " + username, e);
        }
    }

    @Override
    public Optional<Trainer> changePassword(Trainer trainer) {
        return trainerRepository.changePassword(trainer);
    }

    @Override
    public Optional<Trainer> update(Trainer trainer) throws ServiceException {
        Optional<TrainingType> checkedTrainingType = trainingTypeService.findByTrainingType(trainer.getSpecialization().getTrainingType());

        if(checkedTrainingType.isEmpty()){
            logger.error("No such training type in DB {}", trainer.getSpecialization().getTrainingType());
            throw new ServiceException("No such training type in DB");
        }

        trainer.setSpecialization(checkedTrainingType.get());

        return trainerRepository.update(trainer);
    }

    @Override
    public boolean changeStatus(Trainer trainer) throws ServiceException {
        try {
            return trainerRepository.changeStatus(trainer);
        } catch (DBException e) {
            throw new ServiceException("Fail to change trainer status", e);
        }
    }

    @Override
    public List<Training> getTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeName) throws ServiceException {
        try {
            return trainerRepository.getTrainings(trainerUsername, fromDate, toDate, traineeName);
        } catch (DBException e) {
            logger.error("Fail to get trainer trainings from DB - userName {} ", trainerUsername);
            throw new ServiceException("Fail to get trainer trainings from DB - userName " + trainerUsername, e);
        }
    }

    @Override
    public List<Trainer> getNotAssignedOnTraineeTrainersByTraineeUsername(String traineeUsername) throws ServiceException {
        Optional<Trainee> traineeFromDB = traineeService.getTraineeByUsername(traineeUsername);

        List<Trainer> traineeTrainers = null;
        List<Trainer> trainers = null;

        if(traineeFromDB.isPresent()){
            traineeTrainers = traineeFromDB.get().getTrainers();
        } else {
            throw new ServiceException("Fail to get trainers list by trainee name from DB");
        }

        try {
            trainers = trainerRepository.getAll();
        } catch (DBException e) {
            logger.error("Fail to get trainers from DB");
            throw new ServiceException("Fail to get trainers from DB", e);
        }

        trainers.removeAll(traineeTrainers);

        return trainers;
    }

    private String createValidUserName(Trainer trainer) {

        String userName = trainer.getUser().getFirstname() + "." + trainer.getUser().getLastname();

        if (userService.findByUserName(userName).isEmpty()) {
            return userName;
        }

        for (long i = 0; i < Long.MAX_VALUE; i++) {
            StringBuilder newUserName = new StringBuilder(userName + i);
            if (userService.findByUserName(newUserName.toString()).isEmpty()) {
                return newUserName.toString();
            }
        }

        return userName;
    }
}
