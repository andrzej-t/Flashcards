package flashcards;

public class Main {
    public static void main(String[] args) {

        flashcards.Game game = new flashcards.Game();
        String exportPath = "";
        String importPath;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importPath = args[i + 1];
                game.importFile(importPath);
            } else if (args[i].equals("-export")) {
                exportPath = args[i + 1];
                game.exportFile(exportPath);
            }
        }
        game.play(exportPath);
    }
}

