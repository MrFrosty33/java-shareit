package ru.practicum.models.booking;

public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static String stringValue(State state) {
        return state.toString();
    }

    public static State stateValue(String string) {
        switch (string.toUpperCase()) {
            case "ALL" -> {
                return State.ALL;
            }
            case "CURRENT" -> {
                return State.CURRENT;
            }
            case "PAST" -> {
                return State.PAST;
            }
            case "FUTURE" -> {
                return State.FUTURE;
            }
            case "WAITING" -> {
                return State.WAITING;
            }
            case "REJECTED" -> {
                return State.REJECTED;
            }
            default -> {
                return null;
            }
        }
    }
}
