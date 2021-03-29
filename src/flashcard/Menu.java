
package flashcard;

import static flashcard.FlashCardService.logToFile;
import static flashcard.FlashCardService.printHardestCards;
import static flashcard.FlashCardService.resetStats;
import static flashcard.FlashCardService.addCard;
import static flashcard.FlashCardService.askCard;
import static flashcard.FlashCardService.exportToFile;
import static flashcard.FlashCardService.importFromFile;
import static flashcard.FlashCardService.logToFile;
import static flashcard.FlashCardService.printHardestCards;
import static flashcard.FlashCardService.removeCard;
import static flashcard.FlashCardService.resetStats;


public class Menu {
   public static String INTRODUCTION = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";

    public static void getCommand(String actionName) {
        switch (actionName) {
            case "add":
                addCard();
                break;
            case "remove":
                removeCard();
                break;
            case "import":
                importFromFile();
                break;
            case "export":
                exportToFile();
                break;
            case "ask":
                askCard();
                break;
            case "exit":
                System.out.println("Bye bye!");
                break;
            case "log":
                logToFile();
                break;
            case "hardest card":
                printHardestCards();
                break;
            case "reset stats":
                resetStats();
                break;
        }
    }
}