package lesson02.complex.numbers;

public class ComplexNumbersCalculator {

    public ComplexNumber sum(ComplexNumber number1, ComplexNumber number2) {
        final double real = number1.getReal() + number2.getReal();
        final double imaginary = number1.getImaginary() + number2.getImaginary();
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber sub(ComplexNumber number1, ComplexNumber number2) {
        final double real = number1.getReal() - number2.getReal();
        final double imaginary = number1.getImaginary() - number2.getImaginary();
        return new ComplexNumber(real, imaginary);
    }

    public ComplexNumber multiplication(ComplexNumber number1, ComplexNumber number2) {
        double real = number1.getReal() * number2.getReal() - number1.getImaginary() * number2.getImaginary();
        double imaginary = number1.getReal() * number2.getImaginary() + number2.getReal() * number1.getImaginary();
        return new ComplexNumber(real, imaginary);
    }

    public double getModule(ComplexNumber number) {
        return Math.sqrt(Math.pow(number.getReal(), 2) + Math.pow(number.getImaginary(), 2));
    }
}
