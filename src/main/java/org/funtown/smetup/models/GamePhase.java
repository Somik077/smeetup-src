package org.funtown.smetup.models;

public enum GamePhase {
    WAITING("Ожидание"),
    PREPARATION("Подготовка"),
    KIT_DISTRIBUTION("Выдача китов"),
    GAME("Игра"),
    ENDING("Завершение");

    private final String displayName;

    GamePhase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GamePhase getNext() {
        switch (this) {
            case WAITING:
                return PREPARATION;
            case PREPARATION:
                return KIT_DISTRIBUTION;
            case KIT_DISTRIBUTION:
                return GAME;
            case GAME:
                return ENDING;
            case ENDING:
                return WAITING;
            default:
                return WAITING;
        }
    }
}