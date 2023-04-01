package ru.ylab.practice.lesson03.password.validator;

import ru.ylab.practice.lesson03.password.validator.exceptions.WrongLoginException;
import ru.ylab.practice.lesson03.password.validator.exceptions.WrongPasswordException;

public class PasswordValidator {

    private static final String LOGIN_REGEX = "\\w+";
    private static final String PASSWORD_REGEX = "\\w+";
    private static final int LOGIN_MAX_SIZE = 20;
    private static final int PASSWORD_MAX_SIZE = 20;

    public static boolean validate(String login, String password, String confirmPassword) {
        try {
            if (login.length() >= LOGIN_MAX_SIZE) {
                throw new WrongLoginException("Логин слишком длинный");
            } else if (!login.matches(LOGIN_REGEX)) {
                throw new WrongLoginException("Логин содержит недопустимые символы");
            } else if (password.length() >= PASSWORD_MAX_SIZE) {
                throw new WrongPasswordException("“Пароль слишком длинный");
            } else if (!password.matches(PASSWORD_REGEX)) {
                throw new WrongPasswordException("Пароль содержит недопустимые символы");
            } else if (!password.equals(confirmPassword)) {
                throw new WrongPasswordException("“Пароль и подтверждение не совпадают");
            } else {
                return true;
            }
        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
