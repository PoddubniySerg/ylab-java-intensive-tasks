package task3.password.validator.exceptions;

public class WrongPasswordException extends Exception {

    public WrongPasswordException() {
        super();
    }

    public WrongPasswordException(String message) {
        super(message);
    }
}
