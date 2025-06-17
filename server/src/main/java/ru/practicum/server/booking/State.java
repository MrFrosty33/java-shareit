package ru.practicum.server.booking;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.server.exception.BadRequestParamException;

@Slf4j
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
                log.info("Попытка получить State.enum из неконвертируемого значения: {}", string);
                throw new BadRequestParamException("");
            }
        }
    }
}
