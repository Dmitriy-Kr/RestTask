package edu.java.resttask.service.impl;

import edu.java.resttask.entity.Training;
import edu.java.resttask.repository.DBException;
import edu.java.resttask.repository.TrainingRepository;
import edu.java.resttask.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepository;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingTypeService trainingTypeService;

    private static Logger logger = LoggerFactory.getLogger( TrainingServiceImpl.class);

    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeService traineeService, TrainerService trainerService, TrainingTypeService trainingTypeService) {
        this.trainingRepository = trainingRepository;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeService = trainingTypeService;
    }

    @Override
    public Optional<Training> create(Training training) throws ServiceException {
        try {

            training.setTrainingType(trainingTypeService
                    .findByTrainingType(training.getTrainingType().getTrainingType())
                    .orElseThrow(() -> new ServiceException("Fail to create training. No such training type present in DB")));

            training.setTrainee(traineeService
                    .getTraineeByUsername(training.getTrainee().getUser().getUsername())
                    .orElseThrow(() -> new ServiceException("Fail to create training. No such trainee present in DB")));

            training.setTrainer(trainerService
                    .getTrainerByUserName(training.getTrainer().getUser().getUsername())
                    .orElseThrow(() -> new ServiceException("Fail to create training. No such trainer present in DB")));

            return trainingRepository.create(training);

        } catch (DBException e) {
            logger.error("Fail to create training with trainingName {} ", training.getTrainingName());
            throw new ServiceException("Fail to create training with trainingName " + training.getTrainingName(), e);
        }
    }
}
