package ud4.practices.soluciones.fileencryption;

import java.util.*;
import java.util.function.Consumer;

public class Menu {
    Scanner scanner;
    List<String> options;
    List<Consumer<Void>> mappings;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
        this.options = new ArrayList<>();
        this.mappings = new ArrayList<>();
    }

    public void addOption(String option, Consumer<Void> mapping){
        this.options.add(option);
        this.mappings.add(mapping);
    }

    public int userSelection(int upperBound){
        String prompt = "Introdueix l'elecció triada: ";
        String errorMessage = "Error! L'opció indicada no és vàlida.";

        System.out.print(prompt);
        int selection = scanner.nextInt();
        scanner.nextLine();

        if(selection < 0 || selection > upperBound){
            System.out.println(errorMessage);
            System.out.print(prompt);
            selection = scanner.nextInt();
        }
        return selection;
    }

    public void printOptions(){
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d. %s\n", i+1, options.get(i));
        }
        System.out.println("0. Exit");
    }

    public void executeSelection(int selection){
        Consumer<Void> function = mappings.get(selection - 1);
        function.accept(null);
        System.out.println();
    }

    public void menu(){
        int selection = -1;
        while(selection != 0) {
            printOptions();
            selection = userSelection(options.size());
            executeSelection(selection);
        }
    }
}
