package edu.java.resttask.controller;

import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.dto.TrainerDtoForTrainee;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import edu.java.resttask.utility.MappingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return mapToTraineeDto(traineeService.findByUsername(username).get());
    }

    @GetMapping("/trainers-no-assigned")
    @ResponseBody
    public List<TrainerDtoForTrainee> findNoAssignedActiveTrainers(@RequestParam("username") String username) throws ServiceException {
        return traineeService.getNotAssignedOnTraineeTrainersByTraineeUsername(username).stream()
                .map(MappingUtils::mapToTrainerDtoForTrainee)
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

    @DeleteMapping
    @ResponseBody
    public void delete(@RequestParam("username") String username) throws ServiceException {
        traineeService.deleteByUsername(username);
    }


}
