package veryval.dailybot.TGBot.Entity;

public enum Language { ENGLISH("Английский") ,
    DEUTCH("Немецкий"),
    SPANISH("Испанский"),
    CHINEESE("Китайский"),
    FRENCH("Французский");
    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
