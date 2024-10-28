package edu.java.resttask.service;

import edu.java.resttask.entity.Training;

import java.util.Optional;

public interface TrainingService {
    Optional<Training> create(Training training) throws ServiceException;
}
