package edu.java.resttask;

import edu.java.resttask.config.SpringConfig;
import edu.java.resttask.entity.*;
import edu.java.resttask.service.ServiceException;
import edu.java.resttask.service.TraineeService;
import edu.java.resttask.service.TrainerService;
import edu.java.resttask.service.TrainingService;
import edu.java.resttask.utility.PasswordGenerator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);

        TrainerService trainerService = context.getBean("trainerServiceImpl", TrainerService.class);

        System.out.println("\n----------------- Create Trainer ------------------------------------------------\n");

        Trainer trainer = new Trainer();
        trainer.setSpecialization(new TrainingType(2L, "fitness"));
        User user = new User();
        user.setFirstname("Fred");
        user.setLastname("Mercury");
        user.setIsActive(true);
        trainer.setUser(user);

        try {
            trainerService.save(trainer);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("trainer id = " + trainer.getId());
        System.out.println("trainer user id = " + trainer.getUser().getId());

        System.out.println("\n----------------------- Create Trainee ----------------------------------------------------\n");

        TraineeService traineeService = context.getBean("traineeService", TraineeService.class);

        Trainee trainee = new Trainee();
        trainee.setAddress("Black street 28/36");
        trainee.setDateOfBirth(Date.valueOf(LocalDate.parse("2000-12-06")));

        user = new User();
        user.setFirstname("Fred");
        user.setLastname("Mercury");
        user.setIsActive(true);
        trainee.setUser(user);

        traineeService.save(trainee);

        System.out.println("trainee id = " + trainee.getId());
        System.out.println("trainee user id = " + trainee.getUser().getId());

        System.out.println("\n--------------------------- Find trainee by username ------------------------------------------------\n");
        try {
            System.out.println("Found trainee: " + (traineeService.findByUsername("Mari.Doyle").get()));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n------------------------------- Trainee username and password matching --------------------------------------------\n");
        Optional<Trainee> optionalTrainee = null;
        try {
            optionalTrainee = traineeService.usernameAndPasswordMatching("Mari.Doyle", "1753799703");
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Match trainee username and password for username " + (optionalTrainee.isPresent() ? optionalTrainee.get().getUser().getUsername() : "No matching!!!"));

        System.out.println("\n----------------------------------- Find trainer by username ----------------------------------------\n");
        try {
            System.out.println("Find trainer " + (trainerService.getTrainerByUserName("Coleman.Yates").get()));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n--------------------------------- Trainer username and password matching ------------------------------------------\n");
        Optional<Trainer> optionalTrainer = null;
        try {
            optionalTrainer = trainerService.usernameAndPasswordMatching("Coleman.Yates", "4415125129");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println("Match trainer userName and password for username  " + (optionalTrainer.isPresent() ? optionalTrainer.get().getUser().getUsername() : "No matching!!!"));

        System.out.println("\n----------------------------------Trainee Change password-----------------------------------------\n");

        try {
            trainee = traineeService.findByUsername("Mari.Doyle").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainee);
        String password = PasswordGenerator.generatePassword();
        System.out.println("New password for " + trainee.getUser().getUsername() + "   " + password);
        trainee.getUser().setPassword(password);
        try {
            System.out.println(traineeService.changePassword(trainee));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n----------------------------------Trainer Change password-----------------------------------------\n");

        try {
            trainer = trainerService.getTrainerByUserName("Coleman.Yates").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainer);
        password = PasswordGenerator.generatePassword();
        System.out.println("New password for " + trainer.getUser().getUsername() + "   " + password);
        trainer.getUser().setPassword(password);
        System.out.println(trainerService.changePassword(trainer).get());

        System.out.println("\n----------------------------------Trainer Change password-----------------------------------------\n");

        try {
            trainer = trainerService.getTrainerByUserName("Coleman.Yates").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainer);
        password = PasswordGenerator.generatePassword();
        System.out.println("New password for " + trainer.getUser().getUsername() + "   " + password);
        trainer.getUser().setPassword(password);
        System.out.println(trainerService.changePassword(trainer).get());

        System.out.println("\n----------------------------------Trainer update-----------------------------------------\n");

        try {
            trainer = trainerService.getTrainerByUserName("Coleman.Yates").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainer);

        trainer.getUser().setFirstname("Dasha");
        trainer.getUser().setLastname("Flower");
        trainer.getUser().setUsername("Bird");
        trainer.getSpecialization().setTrainingType("resistance");

        try {
            System.out.println(trainerService.update(trainer).get());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n---------------------------------- Trainee update -----------------------------------------\n");

        try {
            trainee = traineeService.findByUsername("Mari.Doyle").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainee);

        trainee.getUser().setFirstname("Irina");
        trainee.getUser().setLastname("Ortega");

        trainee.setDateOfBirth(Date.valueOf(LocalDate.parse("2005-10-16")));
        trainee.setAddress("Serova st, 256, ap 45");

        try {
            System.out.println(traineeService.update(trainee).get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------------- Trainee change status  -----------------------------------------\n");

        try {
            trainee = traineeService.findByUsername("Dave.Batista").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainee);

        try {
            System.out.println(traineeService.changeStatus(trainee));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        try {
            trainee = traineeService.findByUsername("Dave.Batista").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainee);

        System.out.println("\n---------------------------------- Trainer change status  -----------------------------------------\n");

        try {
            trainer = trainerService.getTrainerByUserName("Kathleen.Carr").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainer);

        try {
            System.out.println(trainerService.changeStatus(trainer));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            trainer = trainerService.getTrainerByUserName("Kathleen.Carr").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainer);


        System.out.println("\n---------------------------------- Delete Trainee  -----------------------------------------\n");

        try {
            trainee = traineeService.findByUsername("Dave.Batista").get();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        System.out.println(trainee);

        try {
            traineeService.deleteByUsername("Dave.Batista");
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            trainee = traineeService.findByUsername("Dave.Batista").get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n---------------------------------- Get Trainee Trainings List  -----------------------------------------\n");

        try {
            System.out.println(traineeService.getTrainings("Shannon.Velazquez",
                    Date.valueOf(LocalDate.parse("2024-10-21")),
                    Date.valueOf(LocalDate.parse("2024-11-21")),
                    "Ward",
                     "yoga"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n---------------------------------- Get Trainer Trainings List  -----------------------------------------\n");

        try {
            System.out.println(trainerService.getTrainings("Ward.Mejia",
                    Date.valueOf(LocalDate.parse("2024-09-29")),
                    Date.valueOf(LocalDate.parse("2024-11-22")),
                    "Shannon"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n---------------------------------- Add Training  -----------------------------------------\n");

        TrainingService trainingService = context.getBean("trainingServiceImpl", TrainingService.class);

        Training training = new Training();
        training.setTrainingDay(Date.valueOf(LocalDate.parse("2024-10-25")));
        training.setTrainingName("dynamic stretching");
        training.setTrainingDuration(45);
        training.setTrainingType(new TrainingType("stretching"));

        user = new User();
        user.setUsername("Igor.Gura");
        trainee = new Trainee();
        trainee.setUser(user);
        training.setTrainee(trainee);

        user = new User();
        user.setUsername("Frazier.Richards");
        trainer = new Trainer();
        trainer.setUser(user);
        training.setTrainer(trainer);

        try {
            System.out.println(trainingService.create(training).get());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n--------------- Get trainers list that not assigned on trainee by trainee's username----------\n");

        try {
            System.out.println(trainerService.getNotAssignedOnTraineeTrainersByTraineeUsername("Shannon.Velazquez"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        System.out.println("\n--------------- Update Tranee's trainers list -------------\n");

        List<Trainer> trainerList = new ArrayList<>();

        user = new User();
        user.setUsername("Ward.Mejia");
        trainer = new Trainer();
        trainer.setUser(user);

        trainerList.add(trainer);

        try {
            System.out.println(traineeService.getTrainerList("Igor.Gura"));

            trainee = traineeService.findByUsername("Igor.Gura").get();
            System.out.println("Current trainee list: " + traineeService.getTrainerList("Igor.Gura"));
            trainee.setTrainers(trainerList);

            System.out.println("Updated trainee list: " + traineeService.updateTrainersList(trainee));
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }
}
