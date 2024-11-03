package edu.java.resttask.controller;

import edu.java.resttask.authbean.AuthBean;
import edu.java.resttask.dto.TrainerDto;
import edu.java.resttask.exception.InvalidDataException;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static edu.java.resttask.utility.MappingUtils.mapToTrainer;
import static edu.java.resttask.utility.MappingUtils.mapToTrainerDto;
import static edu.java.resttask.utility.Validation.validateName;

@Controller
@RequestMapping("/trainer")
public class TrainerController {
    private TrainerService trainerService;
    private AuthBean authBean;

    public TrainerController(TrainerService trainerService, AuthBean authBean) {
        this.trainerService = trainerService;
        this.authBean = authBean;
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "New Trainer registration")
    public TrainerDto create(@RequestBody TrainerDto trainerDto) throws InvalidDataException, ServiceException {
        validateName(trainerDto.getFirstname());
        validateName(trainerDto.getLastname());
        return mapToTrainerDto(trainerService.save(mapToTrainer(trainerDto)));
    }
}
