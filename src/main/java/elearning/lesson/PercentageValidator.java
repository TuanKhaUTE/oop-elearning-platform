package elearning.lesson;

final class PercentageValidator {

    private PercentageValidator() {
    }

    static void requireValid(double value, String fieldName) {
        if (Double.isNaN(value) || value < 0 || value > 100) {
            throw new IllegalArgumentException(
                    fieldName + " must be between 0 and 100.");
        }
    }
}