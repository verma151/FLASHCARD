package flashcard;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlashCardService {
   private static final Map<String, String> flashCards = new LinkedHashMap<>();
    private static final Map<String, Integer> mistakesCounter = new HashMap<>();
    private static final Scanner intScanner = new Scanner(System.in);
    private static final ArrayList<String> logs = new ArrayList<>();
    private static Scanner scanner;

    private FlashCardService() {}

    public static Scanner getScanner() {
        scanner = new Scanner(System.in);
        return scanner;
    }

    public static void addCard() {
        String card = createCard();
        if (!card.isEmpty()) {
            String definition = createDefinition();
            if (!definition.isEmpty()) {
                flashCards.put(card, definition);
                outputMsg("The pair (\"" + card + "\":\"" + definition + "\") has been added. \n");
            }
        }
    }

    public static void removeCard() {
        outputMsg("The card:");
        String card = inputMessage();
        if (flashCards.containsKey(card)) {
            flashCards.remove(card);
            mistakesCounter.remove(card);
            outputMsg("The card has been removed \n");
        } else {
            outputMsg("Can't remove \"" + card + "\": there is no such card. \n");
        }
    }

    public static void exportToFile() {
        try {
            outputMsg("File name:");
            String fileName = inputMessage();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            int count = 0;
            for (Map.Entry<String,String> entry : flashCards.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue() + " - " + mistakesCounter.get(entry.getKey()));
                writer.newLine();
                count++;
            }
            outputMsg(count + " cards have been saved.");
            writer.close();
        } catch (FileNotFoundException ex) {
            outputMsg("File not found. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportToFile(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            int count = 0;
            for (Map.Entry<String,String> entry : flashCards.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue() + " - " + mistakesCounter.get(entry.getKey()));
                writer.newLine();
                count++;
            }
            System.out.println(count + " cards have been saved");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void importFromFile() {
        outputMsg("File name:");
        String pathToFile = inputMessage();
        BufferedReader br = null;
        int count = 0;
        try {
            br = new BufferedReader(new FileReader(new File(pathToFile)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(" - ");
                if (lines.length == 3) {
                    flashCards.put(lines[0].trim(), lines[1].trim());
                    mistakesCounter.put(lines[0].trim(), Integer.parseInt(lines[2].trim()));
                    count++;
                }
            }
            outputMsg(count + " cards have been loaded.");
        } catch (FileNotFoundException ex) {
            outputMsg("File not found. \n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void importFromFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(" - ");
                if (lines.length == 3) {
                    flashCards.put(lines[0].trim(), lines[1].trim());
                    mistakesCounter.put(lines[0].trim(), Integer.parseInt(lines[2].trim()));
                    count++;
                }
            }
            System.out.println(count + " cards have been loaded.");
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void askCard() {
        outputMsg("How many times to ask?");
        int iterations = inputNumber();
        for (int i = 0; i < iterations; i++) {
            String key = getRandomKey();
            outputMsg("Print the definition of \"" + key + "\":");
            String answer = inputMessage();
            if (flashCards.get(key).equals(answer)) {
                outputMsg("Correct!");
            } else if (flashCards.containsValue(answer)) {
                incrementMistake(key);
                String definition = FlashCardService.getKeyByValue(flashCards, answer).findFirst().get();
                outputMsg("Wrong. The right answer is \"" + flashCards.get(key) + "\", but your definition" +
                        " is correct for \"" + definition + "\".");
            } else {
                incrementMistake(key);
                outputMsg("Wrong. The right answer is \"" + flashCards.get(key) + "\".\n");
            }
        }
    }

    public static void logToFile() {
        BufferedWriter writer = null;
        try {
            outputMsg("File name:");
            String fileName = inputMessage();
            writer = new BufferedWriter(new FileWriter(fileName, false));
            for (String log : logs) {
                writer.write(log);
                writer.newLine();
            }
            outputMsg("The log has been saved.\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printHardestCards() {
        if (mistakesCounter.isEmpty()) {
            outputMsg("There are no cards with errors.\n");
        } else {
            int result = mistakesCounter.entrySet().stream().max((o1, o2) -> {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else if (o1.getValue() < o2.getValue()) {
                    return -1;
                } else {
                    return 0;
                }
            }).get().getValue();
            List<String> mistakes = getKeyByValue(mistakesCounter, result).collect(Collectors.toList());
            if (mistakes.size() == 1) {
                outputMsg("The hardest card is \"" + mistakes.get(0) + "\". You have " + result + " errors answering it.\n");
            } else {
                printMsg("The hardest cards are ");
                for (int i = 0; i < mistakes.size(); i++) {
                    if (i == mistakes.size() - 1) {
                        printMsg(mistakes.get(i));
                    } else {
                        printMsg(mistakes.get(i) + ", ");
                    }
                }
                outputMsg(". You have " + result + " errors answering them.\n");
            }
        }
    }

    public static void resetStats() {
        mistakesCounter.clear();
        outputMsg("Card statistics has been reset.\n");
    }

    static void outputMsg(String msg) {
        logs.add(msg);
        System.out.println(msg);
    }

    static void printMsg(String msg) {
        logs.add(msg);
        System.out.print(msg);
    }

    private static String inputMessage() {
        String input = getScanner().nextLine();
        logs.add(input);
        return input;
    }

    private static Integer inputNumber() {
        Integer input = intScanner.nextInt();
        logs.add(input.toString());
        return input;
    }

    private static void incrementMistake(String key) {
        mistakesCounter.putIfAbsent(key, 0);
        mistakesCounter.put(key, mistakesCounter.get(key) + 1);
    }

    private static String createCard() {
        outputMsg("The card:");
        String card = inputMessage();
        if (flashCards.containsKey(card)) {
            outputMsg("The card \"" + card + "\" already exists. \n");
            return "";
        }
        return card;
    }

    private static String createDefinition() {
        outputMsg("The definition of the card:");
        String definition = inputMessage();
        if (flashCards.containsValue(definition)) {
            outputMsg("The definition \"" + definition + "\" already exists. \n");
            return "";
        }
        return definition;
    }

    private static String getRandomKey() {
        int randomArrayIndex  = new Random().nextInt(flashCards.keySet().toArray().length);
        return (String) flashCards.keySet().toArray()[randomArrayIndex];
    }

    private static Stream<String> getKeyByValue(Map<String, String> flashCards, String value) {
        return flashCards
                .entrySet()
                .stream().filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    private static Stream<String> getKeyByValue(Map<String, Integer> flashCards, int value) {
        return flashCards
                .entrySet()
                .stream().filter(entry -> value == entry.getValue())
                .map(Map.Entry::getKey);
    }

}
