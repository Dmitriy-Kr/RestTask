package edu.java.resttask.controller;

import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.dto.TraineeTrainingDto;
import edu.java.resttask.dto.TrainerDtoForTrainee;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.TrainingType;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import edu.java.resttask.utility.MappingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import static edu.java.resttask.utility.MappingUtils.*;

@Controller
@RequestMapping("/trainee")
public class TraineeController {
    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @GetMapping
    @ResponseBody
    public TraineeDto findByUsername(@RequestParam("username") String username) throws ServiceException {
        //Optional - exception if find nothing!!!
        return mapToTraineeDto(traineeService.findByUsername(username).get());
    }

    @GetMapping("/trainers-no-assigned")
    @ResponseBody
    public List<TrainerDtoForTrainee> findNoAssignedActiveTrainers(@RequestParam("username") String username) throws ServiceException {
        return traineeService.getNotAssignedOnTraineeTrainersByTraineeUsername(username).stream()
                .map(MappingUtils::mapToTrainerDtoForTrainee)
                .collect(Collectors.toList());
    }

    @GetMapping("/trainings")
    @ResponseBody
    public List<TraineeTrainingDto> findTrainings(@RequestParam("username") String username,
                                                  @RequestParam(value = "fromDate", required = false) Date fromDate,
                                                  @RequestParam(value = "toDate", required = false) Date toDate,
                                                  @RequestParam(value = "trainerName", required = false)String trainerName,
                                                  @RequestParam(value = "trainingType", required = false) String trainingType) throws ServiceException {
        return traineeService.getTrainings(username, fromDate, toDate, trainerName, trainingType).stream()
                .map(MappingUtils::mapToTraineeTrainingDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseBody
    public TraineeDto create(@RequestBody TraineeDto traineeDto) {
        return mapToTraineeDto(traineeService.save(mapToTrainee(traineeDto)));
    }

    @PutMapping("/{id}")
    @ResponseBody
    public TraineeDto update(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException {
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return mapToTraineeDto(traineeService.update(trainee).orElseThrow(() -> new ServiceException("Update failed")));
    }

    @PutMapping("/{id}/trainers")
    @ResponseBody
    public List<TrainerDtoForTrainee> updateTrainerList(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException {
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return traineeService.updateTrainersList(trainee).stream().map(MappingUtils::mapToTrainerDtoForTrainee).collect(Collectors.toList());
    }

    @PatchMapping("/{id}/status")
    @ResponseBody
    public TraineeDto changeStatus(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException {
        Trainee trainee = mapToTrainee(traineeDto);
        trainee.setId(id);
        return mapToTraineeDto(traineeService.changeStatus(trainee).orElseThrow(() -> new ServiceException("Cannot change status")));
    }

    @DeleteMapping
    @ResponseBody
    public void delete(@RequestParam("username") String username) throws ServiceException {
        traineeService.deleteByUsername(username);
    }


}
