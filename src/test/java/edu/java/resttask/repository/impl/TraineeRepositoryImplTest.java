package edu.java.resttask.repository.impl;

import edu.java.resttask.entity.Trainee;
import edu.java.resttask.entity.User;
import edu.java.resttask.repository.DBException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TraineeRepositoryImplTest {
    private static TraineeRepositoryImpl traineeRepository;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void before() {
        traineeRepository = new TraineeRepositoryImpl();
        entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-test");
        traineeRepository.entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void after(){
        traineeRepository.entityManager.clear();
        traineeRepository.entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    void createTest() {
        Trainee trainee = new Trainee();
        trainee.setAddress("Black street 28/36");
        trainee.setDateOfBirth(Date.valueOf(LocalDate.parse("2000-12-06")));

        User user = new User();
        user.setFirstname("Pavlo");
        user.setLastname("Mercury");
        user.setUsername("Pavlo.Mercury");
        user.setPassword("1234567892");
        user.setIsActive(true);
        trainee.setUser(user);

        Optional<Trainee> traineeFromDB = traineeRepository.create(trainee);
        assertEquals(trainee, traineeFromDB.get());

    }

    @Test
    void getTraineeByUserNameTestPositive() throws DBException {
        Optional<Trainee> traineeFromDB = traineeRepository.getTraineeByUserName("Shannon.Velazquez");
        assertEquals("1788799703", traineeFromDB.get().getUser().getPassword());
    }

    @Test
    void getTraineeByUserNameTestThrowDBException() {
        assertThrows(DBException.class, () -> traineeRepository.getTraineeByUserName("Shannon.Velazquez25"));
    }

    @Test
    void changePassword() {
    }

    @Test
    void update() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void delete() {
    }

    @Test
    void getTrainings() {
    }

    @Test
    void updateTrainersList() {
    }
}