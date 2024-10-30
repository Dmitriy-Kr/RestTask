package edu.java.resttask.controller;

import edu.java.resttask.dto.TraineeDto;
import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.User;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/trainee")
public class TraineeController {
    private TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostMapping
    @ResponseBody
    public Trainee create(@RequestBody TraineeDto traineeDto){
        System.out.println(traineeDto);
        Trainee trainee = new Trainee();
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());

        User user = new User();
        user.setFirstname(traineeDto.getFirstname());
        user.setLastname(traineeDto.getLastname());
        trainee.setUser(user);
        return traineeService.save(trainee);
    }

    @GetMapping
    @ResponseBody
    public TraineeDto findByUsername(@RequestParam("username") String username) throws ServiceException {
        return new TraineeDto(traineeService.findByUsername(username).get());
    }
}
