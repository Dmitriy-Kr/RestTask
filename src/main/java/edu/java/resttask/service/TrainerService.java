package edu.java.resttask.service;

import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.Training;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
        Optional<Trainer> save(Trainer trainer) throws ServiceException;
        Optional<Trainer> usernameAndPasswordMatching(String userName, String password) throws ServiceException;
        Optional<Trainer> getTrainerByUserName(String userName) throws ServiceException;
        Optional<Trainer> changePassword(Trainer trainer);
        Optional<Trainer> update(Trainer trainer) throws ServiceException;
        boolean changeStatus(Trainer trainer) throws ServiceException;
        List<Training> getTrainings(String trainerUsername, Date fromDate, Date toDate, String traineeName) throws ServiceException;
        List<Trainer> getNotAssignedOnTraineeTrainersByTraineeUsername(String traineeUsername) throws ServiceException;
}
