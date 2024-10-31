package edu.java.resttask.utility;

import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.dto.TrainerDtoForTrainee;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.Trainer;
import edu.java.resttask.entity.User;

import java.util.stream.Collectors;

public class MappingUtils {
    private MappingUtils() {
    }

    public static TraineeDto mapToTraineeDto(Trainee trainee){
        TraineeDto dto = new TraineeDto();
        dto.setId(trainee.getId());
        dto.setFirstname(trainee.getUser().getFirstname());
        dto.setLastname(trainee.getUser().getLastname());
        dto.setUsername(trainee.getUser().getUsername());
        dto.setPassword(trainee.getUser().getPassword());
        dto.setActive(trainee.getUser().isActive());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setTrainers(trainee.getTrainers().stream().map(MappingUtils::mapToTrainerDtoForTrainee).collect(Collectors.toList()));
        return dto;
    }

    public static Trainee mapToTrainee(TraineeDto traineeDto){
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());

        User user = new User();
        user.setFirstname(traineeDto.getFirstname());
        user.setLastname(traineeDto.getLastname());
        user.setIsActive(traineeDto.getActive());
        trainee.setUser(user);
        return trainee;
    }

    public static TrainerDtoForTrainee mapToTrainerDtoForTrainee(Trainer trainer){
        TrainerDtoForTrainee dto = new TrainerDtoForTrainee();
        dto.setFirstname(trainer.getUser().getFirstname());
        dto.setLastname(trainer.getUser().getLastname());
        dto.setUsername(trainer.getUser().getUsername());
        dto.setTrainingType(trainer.getSpecialization().getTrainingType());
        return dto;
    }
}
