package edu.java.resttask.dto;

import edu.java.resttask.entity.Trainee;
import jakarta.persistence.Column;

import java.sql.Date;

public class TraineeDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private Boolean isActive;
    private Date dateOfBirth;
    private String address;

    public TraineeDto() {
    }

    public TraineeDto(Trainee trainee) {
        this.id = trainee.getId();
        this.firstname = trainee.getUser().getFirstname();
        this.lastname = trainee.getUser().getLastname();
        this.username = trainee.getUser().getUsername();
        this.password = trainee.getUser().getPassword();
        this.isActive = trainee.getUser().isActive();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
