package flashcards;

import java.io.*;
import java.util.*;

public class Game {

    Scanner scanner = new Scanner(System.in);
    private final Map<String, String> flashcards = new LinkedHashMap<>();
    private final Map<String, Integer> hardCardMap = new LinkedHashMap<>();
    private final List<String> logList = new ArrayList<>();
    boolean end = false;
    String menu = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
    String exportPath = "";

    public void play(String exportingPath) {

        exportPath = exportingPath;

        while (!end) {
            System.out.println(menu);
            logList.add(menu);
            logList.add(menu);
            String action = scanner.nextLine();
            logList.add(action);
            switch (action) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    importCards();
                    break;
                case "export":
                    exportCards();
                    break;
                case "ask":
                    ask();
                    break;
                case "exit":
                    exit();
                    break;
                case "log":
                    saveLog(logList);
                    break;
                case "hardest card":
                    hardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    System.out.println("Invalid option");
                    logList.add("Invalid option");
                    play(exportPath);
                    break;
            }
        }
    }

    private void add() {
        System.out.println("The card:");
        logList.add("The card:");
        String cardNewTerm = scanner.nextLine();
        logList.add(cardNewTerm);
        if (flashcards.containsKey(cardNewTerm)) {
            System.out.println("The card \"" + cardNewTerm + "\" already exists.");
            logList.add("The card \"" + cardNewTerm + "\" already exists.");
            play(exportPath);
        }
        System.out.println("The definition of the card:");
        logList.add("The definition of the card:");
        String cardNewDefinition = scanner.nextLine();
        logList.add(cardNewDefinition);
        if (flashcards.containsValue(cardNewDefinition)) {
            System.out.println("The definition \"" + cardNewDefinition + "\" already exists.");
            logList.add("The definition \"" + cardNewDefinition + "\" already exists.");
            play(exportPath);
        }
        flashcards.put(cardNewTerm, cardNewDefinition);
        hardCardMap.put(cardNewTerm, 0);
        System.out.println("The pair (\"" + cardNewTerm + "\":\"" + cardNewDefinition + "\") has been added.");
        logList.add("The pair (\"" + cardNewTerm + "\":\"" + cardNewDefinition + "\") has been added.");
    }

    private void remove() {
        System.out.println("Which card?");
        logList.add("Which card?");
        String cardToRemove = scanner.nextLine();
        logList.add(cardToRemove);
        if (flashcards.containsKey(cardToRemove)) {
            flashcards.remove(cardToRemove);
            System.out.println("The card has been removed.");
            logList.add("The card has been removed.");
        } else {
            System.out.println("Can't remove \"" + cardToRemove + "\": there is no such card.");
            logList.add("Can't remove \"" + cardToRemove + "\": there is no such card.");
        }
    }

    private void importCards() {

        int i = 0;
        System.out.println("File name:");
        logList.add("File name:");
        String importedFileName = scanner.nextLine();
        logList.add(importedFileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(importedFileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            SavedFile savedFile = (SavedFile) objectInputStream.readObject();
            for (var entry : savedFile.getSavedFlashcards().keySet()) {
                if (!flashcards.containsKey(entry)) {
                    flashcards.put(entry, savedFile.savedFlashcards.get(entry));
                } else if (flashcards.containsKey(entry) && !flashcards.get(entry).equals(savedFile.savedFlashcards.get(entry))) {
                    flashcards.replace(entry,savedFile.savedFlashcards.get(entry));
                }
                i++;
            }

            for (var entry : savedFile.getSavedHardCards().keySet()) {
                if (!hardCardMap.containsKey(entry)) {
                    hardCardMap.put(entry, savedFile.savedHardCards.get(entry));
                } else {
                    hardCardMap.replace(entry, savedFile.savedHardCards.get(entry));
                }
            }

            System.out.println(i + " cards have been loaded.");
            logList.add(i + " cards have been loaded.");
        } catch (Exception e) {
            System.out.println("File not found.");
            logList.add("File not found.");
        }
    }

    public void importFile(String path) {
        int i = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            SavedFile savedFile = (SavedFile) objectInputStream.readObject();
            for (var entry : savedFile.getSavedFlashcards().keySet()) {
                if (!flashcards.containsKey(entry)) {
                    flashcards.put(entry, savedFile.savedFlashcards.get(entry));
                } else if (flashcards.containsKey(entry) && !flashcards.get(entry).equals(savedFile.savedFlashcards.get(entry))) {
                    flashcards.replace(entry,savedFile.savedFlashcards.get(entry));
                }
                i++;
            }

            for (var entry : savedFile.getSavedHardCards().keySet()) {
                if (!hardCardMap.containsKey(entry)) {
                    hardCardMap.put(entry, savedFile.savedHardCards.get(entry));
                } else {
                    hardCardMap.replace(entry, savedFile.savedHardCards.get(entry));
                }
            }

            if (i > 0) {
                System.out.println(i + " cards have been loaded.");
                logList.add(i + " cards have been loaded.");
            }

        } catch (Exception ignored) {

        }
    }

    private void exportCards() {
        System.out.println("File name:");
        logList.add("File name:");
        String exportedFileName = scanner.nextLine();
        logList.add(exportedFileName);

        try {
            SavedFile savedFile = new SavedFile(flashcards, hardCardMap);
            FileOutputStream fileOutputStream = new FileOutputStream(exportedFileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(flashcards.size() + " cards have been saved.");
        logList.add(flashcards.size() + " cards have been saved.");
    }

    public void exportFile(String path) {
        try {
            SavedFile savedFile = new SavedFile(flashcards, hardCardMap);
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(savedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(flashcards.size() + " cards have been saved.");
        logList.add(flashcards.size() + " cards have been saved.");
    }

    private void ask() {
        System.out.println("How many times to ask?");
        logList.add("How many times to ask?");
        String s = scanner.nextLine();
        logList.add(s);
        int numberOfTimes = Math.min(Integer.parseInt(s), flashcards.size());

        String question = "";
        String correctAnswer = "";

        for (int i = 0; i < numberOfTimes; i++) {
            List<String> questionList = new ArrayList<>(flashcards.keySet());

            List<String> answerList = new ArrayList<>(flashcards.values());

            for (int j = i; j < numberOfTimes; j++) {
                question = questionList.get(i);
                correctAnswer = answerList.get(i);
            }

            System.out.println("Print the definition of \"" + question + "\":");
            logList.add("Print the definition of \"" + question + "\":");
            String answer = scanner.nextLine();
            logList.add(answer);

            if (answer.equals(correctAnswer)) {
                System.out.println("Correct!");
                logList.add("Correct!");
            } else if (flashcards.containsValue(answer)) {
                String otherAnswer = "";
                for (String key : flashcards.keySet()) {
                    if (answer.equals(flashcards.get(key))) {
                        otherAnswer = key;
                    }
                }
                hardCardMap.replace(question, (hardCardMap.get(question) + 1));
                System.out.println("Wrong. The right answer is \"" + correctAnswer + "\", but your definition is correct for \"" + otherAnswer + "\".");
                logList.add("Wrong. The right answer is \"" + correctAnswer + "\", but your definition is correct for \"" + otherAnswer + "\".");
            } else {
                hardCardMap.replace(question, (hardCardMap.get(question) + 1));
                System.out.println("Wrong. The right answer is \"" + correctAnswer + "\".");
                logList.add("Wrong. The right answer is \"" + correctAnswer + "\".");
            }
        }
    }

    private void exit() {
        System.out.println("Bye bye!");
        logList.add("Bye bye!");
        exportFile(exportPath);
        System.exit(1);
    }

    private void saveLog(List<String> logList) {
        System.out.println("File name:");
        logList.add("File name:");
        String pathToLogList = scanner.nextLine();
        logList.add(pathToLogList);
        File fileWithLogs = new File(pathToLogList);

        try (FileWriter fileWriter = new FileWriter(fileWithLogs, false)){
            for (String s : logList) {
                fileWriter.write(s + "\n");
            }
            System.out.println("The log has been saved.");
            logList.add("The log has been saved.");

        } catch (IOException e) {
            System.out.println("Exception occurred: " + e.getMessage());
            logList.add("Exception occurred: " + e.getMessage());
        }
        play(exportPath);
    }

    private void hardestCard() {
        int hardCardCounter = 0;
        int maxErrorCounter = 0;

        for (String s : hardCardMap.keySet()) {
            if (hardCardMap.get(s) > maxErrorCounter) {
                maxErrorCounter = hardCardMap.get(s);
            }
        }

        for (String s : hardCardMap.keySet()) {
            if (hardCardMap.get(s) == maxErrorCounter && maxErrorCounter > 0) {
                hardCardCounter++;
            }
        }

        if (hardCardCounter == 1) {
            String term = null;
            for (String s : hardCardMap.keySet()) {
                if (Integer.valueOf(maxErrorCounter).equals(hardCardMap.get(s))) {
                    term = s;
                }
            }
            System.out.println("The hardest card is \"" + term + "\". You have " + maxErrorCounter + " errors answering it");
            logList.add("The hardest card is \"" + term + "\". You have " + maxErrorCounter + " errors answering it");
        } else if (hardCardCounter > 1) {
            System.out.print("The hardest cards are \"");
            logList.add("The hardest cards are \"");
            StringBuilder stringBuilder = new StringBuilder("The hardest cards are \"");
            for (String s : hardCardMap.keySet()) {
                if (hardCardCounter > 1) {
                    System.out.print(s + "\", \"");
                    logList.add(s + "\", \"");
                    stringBuilder.append(s).append("\", \"");
                } else {
                    System.out.print(s);
                    logList.add(s);
                    stringBuilder.append(s);
                }
                hardCardCounter--;
            }
            System.out.println("\". You have " + maxErrorCounter + " errors answering them.");
            logList.add("\". You have " + maxErrorCounter + " errors answering them.");
            stringBuilder.append("\". You have ").append(maxErrorCounter).append("errors answering them.");
            String result = stringBuilder.toString();
            logList.add(result);
        } else {
            System.out.println("There are no cards with errors.");
            logList.add("There are no cards with errors.");
        }
        play(exportPath);
    }

    private void resetStats() {
        try {
            for (String s : hardCardMap.keySet()) {
                hardCardMap.replace(s, 0);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            logList.add("Exception occurred: " + e.getMessage());
        }
        System.out.println("Card statistics have been reset.");
        logList.add("Card statistics have been reset.");
        play(exportPath);
    }
}

