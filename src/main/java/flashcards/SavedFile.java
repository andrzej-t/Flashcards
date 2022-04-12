package flashcards;

import java.io.Serializable;
import java.util.Map;

public class SavedFile implements Serializable {

    Map<String, String> savedFlashcards;
    Map<String, Integer> savedHardCards;

    public SavedFile(Map<String, String> savedFlashcards, Map<String, Integer> savedHardCards) {
        this.savedFlashcards = savedFlashcards;
        this.savedHardCards = savedHardCards;
    }

    public Map<String, String> getSavedFlashcards() {
        return savedFlashcards;
    }

    public Map<String, Integer> getSavedHardCards() {
        return savedHardCards;
    }
}

