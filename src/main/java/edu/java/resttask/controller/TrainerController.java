package edu.java.resttask.controller;

import edu.java.resttask.authbean.AuthBean;
import edu.java.resttask.authbean.LoginException;
import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.dto.TraineeTrainingDto;
import edu.java.resttask.dto.TrainerDto;
import edu.java.resttask.dto.TrainerTrainingDto;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.Trainer;
import edu.java.resttask.exception.InvalidDataException;
import edu.java.resttask.exception.NoResourcePresentException;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TrainerService;
import edu.java.resttask.utility.MappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static edu.java.resttask.utility.MappingUtils.*;
import static edu.java.resttask.utility.Validation.*;
import static edu.java.resttask.utility.Validation.validateDate;

@Controller
@RequestMapping("/trainer")
public class TrainerController {
    private TrainerService trainerService;
    private AuthBean authBean;

    public TrainerController(TrainerService trainerService, AuthBean authBean) {
        this.trainerService = trainerService;
        this.authBean = authBean;
    }

    @GetMapping
    @ResponseBody
    @Operation(summary = "Get trainer profile by Username")
    public TrainerDto findByUsername(@RequestParam("username") String username) throws ServiceException, LoginException, NoResourcePresentException, InvalidDataException {
        Optional<Trainer> trainer;
        if (validateLogin(username) && authBean.getUser() != null && authBean.getUser().getUsername().equals(username)) {
            trainer = trainerService.findByUsername(username);
            if (trainer.isPresent()) {
                return mapToTrainerDto(trainer.get());
            } else {
                throw new NoResourcePresentException("Cannot find trainer with username " + username);
            }
        } else {
            throw new LoginException("Login error");
        }
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "New Trainer registration")
    public TrainerDto create(@RequestBody TrainerDto trainerDto) throws InvalidDataException, ServiceException {
        validateName(trainerDto.getFirstname());
        validateName(trainerDto.getLastname());
        return mapToTrainerDto(trainerService.save(mapToTrainer(trainerDto)));
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Update Trainer Profile")
    public TrainerDto update(@PathVariable Long id, @RequestBody TrainerDto trainerDto) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(trainerDto.getUsername()) && authBean.getUser() != null && authBean.getUser().getUsername().equals(trainerDto.getUsername())) {
            validateName(trainerDto.getFirstname());
            validateName(trainerDto.getLastname());
            Trainer trainer = mapToTrainer(trainerDto);
            trainer.setId(id);
            return mapToTrainerDto(trainerService.update(trainer).orElseThrow(() -> new ServiceException("Update trainer failed")));
        } else {
            throw new LoginException("Login error");
        }
    }

    @PatchMapping("/{id}/status")
    @ResponseBody
    @Operation(summary = "Activate/De-Activate Trainer")
    public void changeStatus(@PathVariable Long id, @RequestBody TrainerDto trainerDto) throws ServiceException, LoginException, InvalidDataException {

        if (validateLogin(trainerDto.getUsername()) && authBean.getUser() != null && authBean.getUser().getUsername().equals(trainerDto.getUsername())) {
            Trainer trainer = mapToTrainer(trainerDto);
            trainer.setId(id);
            trainerService.changeStatus(trainer).orElseThrow(() -> new ServiceException("Cannot change trainer status"));
        } else {
            throw new LoginException("Login error");
        }
    }

    @GetMapping("/trainings")
    @ResponseBody
    @Operation(summary = "Get Trainer Trainings List")
    public List<TrainerTrainingDto> findTrainings(@RequestParam("username") String username,
                                                  @RequestParam(value = "fromDate", required = false) String fromDateParameter,
                                                  @RequestParam(value = "toDate", required = false) String toDateParameter,
                                                  @RequestParam(value = "traineeName", required = false) String traineeName) throws ServiceException, LoginException, InvalidDataException {

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

            if (traineeName != null) {
                validateName(traineeName);
            }

            return trainerService.getTrainings(username, fromDate, toDate, traineeName).stream()
                    .map(MappingUtils::mapToTrainerTrainingDto)
                    .collect(Collectors.toList());
        } else {
            throw new LoginException("Login error");
        }
    }
}
