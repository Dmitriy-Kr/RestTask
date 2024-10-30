package edu.java.resttask.dto;

public interface UserDto {
    Long getId();
    String getFirstname();
    String getLastname();
    String getUsername();
    String getPassword();
    Boolean isActive();
}
