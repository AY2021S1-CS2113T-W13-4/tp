package seedu.lifeasier;

import java.util.Scanner;

/**
 * The Ui class deals with all interactions with the user
 */
public class Ui {

    public static final String SEPARATOR = "=========================================================================";

    public static final String LOGO = "\n"
            + "  _      _  __ ______          _           \n"
            + " | |    (_)/ _|  ____|        (_)          \n"
            + " | |     _| |_| |__   __ _ ___ _  ___ _ __ \n"
            + " | |    | |  _|  __| / _` / __| |/ _ \\ '__|\n"
            + " | |____| | | | |___| (_| \\__ \\ |  __/ |   \n"
            + " |______|_|_| |______\\__,_|___/_|\\___|_|   \n"
            + "\n";

    public void printSeparator() {
        System.out.println(SEPARATOR);
    }

    public void showWelcomeMessage() {
        printSeparator();
        printSeparator();
        printLogo();
        printSeparator();
        printSeparator();
    }

    private void printLogo() {
        System.out.println(LOGO);
    }

    /**
     * Returns read user command
     */
    public String readCommand() {
        Scanner fullCommand = new Scanner(System.in);

        return fullCommand.nextLine();
    }
}