package edu.java.resttask.controller;

import edu.java.resttask.authbean.AuthBean;
import edu.java.resttask.authbean.LoginException;
import edu.java.resttask.dto.TrainingDto;
import edu.java.resttask.exception.InvalidDataException;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static edu.java.resttask.utility.Validation.*;

@Controller
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;
    private AuthBean authBean;

    public TrainingController(TrainingService trainingService, AuthBean authBean) {
        this.trainingService = trainingService;
        this.authBean = authBean;
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "Create new Training")
    public void create(@RequestBody TrainingDto trainingDto) throws InvalidDataException, ServiceException, LoginException {
        if (validateLogin(trainingDto.getTraineeUsername())
                && validateLogin(trainingDto.getTrainerUsername())
                && authBean.getUser() != null
                && (authBean.getUser().getUsername().equals(trainingDto.getTraineeUsername())
                    || (authBean.getUser().getUsername().equals(trainingDto.getTrainerUsername())))) {

            validateName(trainingDto.getTrainingName());
            validateDate(trainingDto.getTrainingDay());
            trainingService.create(trainingDto);

        } else {
            throw new LoginException("Login error");
        }
    }

}
