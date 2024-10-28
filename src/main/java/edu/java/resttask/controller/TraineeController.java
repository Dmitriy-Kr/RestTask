package edu.java.resttask.controller;

import edu.java.resttask.entity.Trainee;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/trainee")
public class TraineeController {
    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    @ResponseBody
    public Trainee create(Trainee trainee){
        return traineeService.save(trainee);
    }

    @GetMapping
    @ResponseBody
    public Trainee findByUsername(@RequestParam("username") String username) throws ServiceException {
        return traineeService.getTraineeByUsername(username).get();
    }
}
