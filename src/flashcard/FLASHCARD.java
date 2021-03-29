
package flashcard;
import static flashcard.FlashCardService.exportToFile;
import static flashcard.FlashCardService.getScanner;
import static flashcard.FlashCardService.importFromFile;
import static flashcard.FlashCardService.outputMsg;
import static flashcard.Menu.getCommand;
public class FLASHCARD {
 public static final String IMPORT = "-import";
    public static final String EXPORT = "-export";
    public static final String EXIT = "exit";
    
    public static void main(String[] args) {
        
    
 if (args.length == 4) {
            if (args[0].equals(IMPORT)) {
                importFromFile(args[1]);
                runApp();
                exportToFile(args[3]);
            } else if (args[2].equals(IMPORT)) {
                importFromFile(args[3]);
                runApp();
                exportToFile(args[1]);
            } else if (args[0].equals(EXPORT)) {
                importFromFile(args[3]);
                runApp();
                exportToFile(args[1]);
            } else if (args[2].equals(EXPORT)) {
                importFromFile(args[1]);
                runApp();
                exportToFile(args[3]);
            }
        } else if (args.length == 2) {
            if (args[0].equals(IMPORT)) {
                importFromFile(args[1]);
                runApp();
            } else if (args[0].equals(EXPORT)) {
                runApp();
                exportToFile(args[1]);
            }
        } else {
            runApp();
        }
    }

    private static void runApp() {
        String command;
        do {
            outputMsg(Menu.INTRODUCTION);
            command = getScanner().nextLine();
            getCommand(command);
        } while (!command.equals(EXIT));
    }
}