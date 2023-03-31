package lesson03.password.validator;

import java.util.List;

public class PasswordValidatorTest {

    public static void main(String[] args) {
        final List<String> logins = List.of("login_1", "login-2", "login_more_than_twenty_chars");
        final List<String> passwords = List.of("pass_1", "pass-2", "pass_more_than_twenty_chars");
        for (String login : logins) {
            for (String password : passwords) {
                for (String confirm : passwords) {
                    System.out.printf("%nResult of validate: %s, %s, %s%n", login, password, confirm);
                    System.out.println(PasswordValidator.validate(login, password, confirm));
                }
            }
        }
    }
}
