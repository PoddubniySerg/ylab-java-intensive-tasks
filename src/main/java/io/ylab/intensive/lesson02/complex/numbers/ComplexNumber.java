package io.ylab.intensive.lesson02.complex.numbers;

public class ComplexNumber {

    private final double real;
    private final double imaginary;

    public ComplexNumber(double realPart) {
        this.real = realPart;
        this.imaginary = 0;
    }

    public ComplexNumber(double realPart, double imaginaryPart) {
        this.real = realPart;
        this.imaginary = imaginaryPart;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    @Override
    public String toString() {
        if (real == 0 && imaginary != 0) {
            return String.format("%si", imaginary);
        } else if (real == 0 || imaginary == 0) {
            return String.format("%.3f", real);
        } else {
            if (imaginary < 0) {
                return String.format("%.3f - %.3fi", real, Math.abs(imaginary));
            } else {
                return String.format("%.3f + %.3fi", real, imaginary);
            }
        }
    }
}
