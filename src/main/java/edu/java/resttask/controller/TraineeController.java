package edu.java.resttask.controller;

import edu.java.resttask.authbean.AuthBean;
import edu.java.resttask.authbean.LoginException;
import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.dto.TraineeTrainingDto;
import edu.java.resttask.dto.TrainerDtoForTrainee;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.exception.InvalidDataException;
import edu.java.resttask.exception.NoResourcePresentException;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import edu.java.resttask.utility.MappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.java.resttask.utility.MappingUtils.mapToTrainee;
import static edu.java.resttask.utility.MappingUtils.mapToTraineeDto;
import static edu.java.resttask.utility.Validation.*;

@Controller
@RequestMapping("/trainee")
public class TraineeController {
    private TraineeService traineeService;
    private AuthBean authBean;

    public TraineeController(TraineeService traineeService, AuthBean authBean) {
        this.traineeService = traineeService;
        this.authBean = authBean;
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "Get trainee profile by Username")
    public TraineeDto findByUsername(@RequestParam("username") String username) throws ServiceException, LoginException, NoResourcePresentException, InvalidDataException {
        Optional<Trainee> trainee;
        if (validateLogin(username) && authBean.getUser() != null && authBean.getUser().getUsername().equals(username)) {
            trainee = traineeService.findByUsername(username);
            if (trainee.isPresent()) {
                return mapToTraineeDto(trainee.get());
            } else {
                throw new NoResourcePresentException("Cannot find trainee with username " + username);
            }
        } else {
            throw new LoginException("Login error");
        }
    }

    @GetMapping("/trainers-no-assigned")
    @ResponseBody
    @Operation(summary = "Get not assigned on trainee active trainers")
    public List<TrainerDtoForTrainee> findNoAssignedActiveTrainers(@RequestParam("username") String username) throws ServiceException, LoginException, InvalidDataException {
        if (validateLogin(username) && authBean.getUser() != null && authBean.getUser().getUsername().equals(username)) {
            return traineeService.getNotAssignedOnTraineeTrainersByTraineeUsername(username).stream()
                    .map(MappingUtils::mapToTrainerDtoForTrainee)
                    .collect(Collectors.toList());
        } else {
            throw new LoginException("Login error");
        }
    }

    @GetMapping("/trainings")
    @ResponseBody
    @Operation(summary = "Get Trainee Trainings List")
    public List<TraineeTrainingDto> findTrainings(@RequestParam("username") String username,
                                                  @RequestParam(value = "fromDate", required = false) String fromDateParameter,
                                                  @RequestParam(value = "toDate", required = false) String toDateParameter,
                                                  @RequestParam(value = "trainerName", required = false) String trainerName,
                                                  @RequestParam(value = "trainingType", required = false) String trainingType) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(username) && authBean.getUser() != null && authBean.getUser().getUsername().equals(username)) {

            Date fromDate = null;
            Date toDate = null;

            if (fromDateParameter != null) {
                if (validateDate(fromDateParameter)) {
                    fromDate = Date.valueOf(LocalDate.parse(fromDateParameter));
                }
            }

            if (toDateParameter != null) {
                if (validateDate(toDateParameter)) {
                    toDate = Date.valueOf(LocalDate.parse(toDateParameter));
                }
            }

            if (trainerName != null) {
                validateName(trainerName);
            }

            if (trainingType != null) {
                validateName(trainingType);
            }

            return traineeService.getTrainings(username, fromDate, toDate, trainerName, trainingType).stream()
                    .map(MappingUtils::mapToTraineeTrainingDto)
                    .collect(Collectors.toList());
        } else {
            throw new LoginException("Login error");
        }
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "New Trainee registration")
    public TraineeDto create(@RequestBody TraineeDto traineeDto) throws InvalidDataException {
        validateName(traineeDto.getFirstname());
        validateName(traineeDto.getLastname());
        return mapToTraineeDto(traineeService.save(mapToTrainee(traineeDto)));
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Update Trainee Profile")
    public TraineeDto update(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(traineeDto.getUsername()) && authBean.getUser() != null && authBean.getUser().getUsername().equals(traineeDto.getUsername())) {
            validateName(traineeDto.getFirstname());
            validateName(traineeDto.getLastname());
            Trainee trainee = mapToTrainee(traineeDto);
            trainee.setId(id);
            return mapToTraineeDto(traineeService.update(trainee).orElseThrow(() -> new ServiceException("Update failed")));
        } else {
            throw new LoginException("Login error");
        }
    }

    @PutMapping("/{id}/trainers")
    @ResponseBody
    @Operation(summary = "Update Trainee's Trainer List")
    public List<TrainerDtoForTrainee> updateTrainerList(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(traineeDto.getUsername()) && authBean.getUser() != null && authBean.getUser().getUsername().equals(traineeDto.getUsername())) {
            for(TrainerDtoForTrainee trainer : traineeDto.getTrainers()){
                validateLogin(trainer.getUsername());
            }
            Trainee trainee = mapToTrainee(traineeDto);
            trainee.setId(id);
            return traineeService.updateTrainersList(trainee).stream().map(MappingUtils::mapToTrainerDtoForTrainee).collect(Collectors.toList());
        } else {
            throw new LoginException("Login error");
        }
    }

    @PatchMapping("/{id}/status")
    @ResponseBody
    @Operation(summary = "Activate/De-Activate Trainee")
    public TraineeDto changeStatus(@PathVariable Long id, @RequestBody TraineeDto traineeDto) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(traineeDto.getUsername()) && authBean.getUser() != null && authBean.getUser().getUsername().equals(traineeDto.getUsername())) {
            Trainee trainee = mapToTrainee(traineeDto);
            trainee.setId(id);
            return mapToTraineeDto(traineeService.changeStatus(trainee).orElseThrow(() -> new ServiceException("Cannot change status")));
        } else {
            throw new LoginException("Login error");
        }
    }

    @DeleteMapping
    @ResponseBody
    @Operation(summary = "Delete Trainee Profile")
    public void delete(@RequestParam("username") String username) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(username) && authBean.getUser() != null && authBean.getUser().getUsername().equals(username)) {
            traineeService.deleteByUsername(username);
        } else {
            throw new LoginException("Login error");
        }
    }
}
