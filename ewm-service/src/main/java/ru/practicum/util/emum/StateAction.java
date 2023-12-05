package ru.practicum.util.emum;

import ru.practicum.util.exception.ValidationException;

public enum StateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    CANCEL_REVIEW,
    SEND_TO_REVIEW;

    public static StateAction getStateActionValue(String stateAction) {
        try {
            return StateAction.valueOf(stateAction);
        } catch (Exception e) {
            throw new ValidationException("Неизвестный статус действия: " + stateAction);
        }
    }
}
