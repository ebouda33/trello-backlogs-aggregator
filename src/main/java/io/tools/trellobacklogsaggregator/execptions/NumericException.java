package io.tools.trellobacklogsaggregator.execptions;

public class NumericException extends RuntimeException {

    public NumericException(String message) {
        super(message);
    }
    public static class MonthValueException extends NumericException {
        public MonthValueException() {
            super("Un mois doit etre compris en 1 et 12");
        }
    }

    public static class WeekValueException extends NumericException {
        public WeekValueException(int max) {
            super(String.format("Une semaine est comprise entre 0 et %s maximum",max));
        }
    }
}

