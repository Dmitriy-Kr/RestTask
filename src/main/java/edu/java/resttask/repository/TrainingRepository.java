package edu.java.resttask.repository;

import edu.java.resttask.entity.Training;

import java.util.Optional;

public interface TrainingRepository {
    Optional<Training> create(Training training) throws DBException;
}
