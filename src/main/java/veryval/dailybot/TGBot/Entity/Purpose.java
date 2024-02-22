package veryval.dailybot.TGBot.Entity;

public enum Purpose { FOR_SELF("Для себя"),
    FOR_WORK("Для работы и бизнеса"),
    FOR_TRAVEL("Для путешествий") ,
    FOR_SCHOOL("Для школы"),
    FOR_EXAM("Для подготовки к экзамену"),
    OTHER("Другое");
    private final String displayName;

    Purpose(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
