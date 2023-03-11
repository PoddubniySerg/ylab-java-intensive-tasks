package task2.snils.validator;

public class SnilsValidatorImpl implements SnilsValidator {

    @Override
    public boolean validate(String snils) {
        final int acceptableWidth = 11;
        final int controlWidth = 2;
        boolean result;
        if (
                snils != null
                        && snils.length() == acceptableWidth
                        && snils.matches("\\d{11}")
        ) {
            final var numberLength = snils.length() - controlWidth;
            int sum = 0;
            for (int i = 0; i < numberLength; i++) {
                sum += Character.digit(snils.charAt(i), 10) * (numberLength - i);
            }
            int control = 0;
            if (sum < 100) {
                control = sum;
            } else if (sum > 100) {
                control = sum % 101;
                if (control == 100) {
                    control = 0;
                }
            }
            final int lastNumber = Integer.parseInt(snils.substring(numberLength));
            result = control == lastNumber;
        } else {
            result = false;
        }
        return result;
    }
}
