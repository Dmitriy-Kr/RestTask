package edu.java.resttask.utility;


import java.util.regex.Pattern;

public class Validation {
    private final static String REGEX_PASSWORD = "^([\\w\\W]{10,})$";
    private final static String REGEX_NAME = "^[a-zA-Zа-яА-Яє-їЄ-Ї\\s]{2,20}$";
    private final static String REGEX_LOGIN = "^[A-Za-z0-9]+([A-Za-z0-9]*|[._-]?[A-Za-z0-9]+)*$";
    private final static String REGEX_DATE = "^[1-9][0-9][0-9]{2}-([0][1-9]|[1][0-2])-([1-2][0-9]|[0][1-9]|[3][0-1])";

    private Validation() {
    }

    public static boolean validatePassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    public static boolean validateName(String name) {
        return Pattern.matches(REGEX_NAME, name);
    }
    
    public static boolean validateLogin(String login) {
        return Pattern.matches(REGEX_LOGIN, login);
    }

    public static boolean validateDate(String date) {
        return Pattern.matches(REGEX_DATE, date);
    }
}
