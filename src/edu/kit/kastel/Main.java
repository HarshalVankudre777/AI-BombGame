package edu.kit.kastel;

import edu.kit.kastel.ui.command.CommandHandler;
import edu.kit.kastel.model.CodeFight;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Entry point of the program.
 *
 * @author uiiux
 */
public final class Main {

    private static final String UTILITY_CLASS_CONSTRUCTOR_MESSAGE = "Utility classes cannot be instantiated";
    private static final String WELCOME_MESSAGE = "Welcome to CodeFight 2024. Enter 'help' for more details.";
    private static final String INVALID_ARGUMENTS_ERROR = "Error, invalid command line arguments.";
    private static final String EMPTY_SPACE = " ";
    private static final int MEMORY_SIZE_INDEX = 0;
    private static final int LAST_MEMORY_SYMBOL_INDEX = 4;
    private static final int FIRST_AI_SYMBOL_INDEX = 5;
    private static final int MEMORY_SIZE_LIMIT = 1337;

    private Main() {
        throw new UnsupportedOperationException(UTILITY_CLASS_CONSTRUCTOR_MESSAGE);
    }

    /**
     * Entry point of the program main-method.
     *
     * @param args command arguments of the program
     */
    public static void main(String[] args) {
        Optional<CodeFight> codeFight = getCodeFight(args);
        if (codeFight.isPresent()) {
            CommandHandler commandHandler = new CommandHandler(codeFight.get());
            System.out.println(WELCOME_MESSAGE);
            commandHandler.handleUserInput();
        } else {
            System.err.println(INVALID_ARGUMENTS_ERROR);
        }

    }

    /**
     * Checks if the command arguments of the program are valid
     *
     * @param args command arguments of the program
     * @return     Program model
     */
    private static Optional<CodeFight> getCodeFight(String[] args) {
        if (args.length % 2 == 0) {
            return Optional.empty();
        }
        int memorySize;
        try {
            memorySize = Integer.parseInt(args[MEMORY_SIZE_INDEX]);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
        if (memorySize < 7 || memorySize > MEMORY_SIZE_LIMIT) {
            return Optional.empty();
        }
        List<String> memorySymbols = new ArrayList<>();
        for (int i = 1; i <= LAST_MEMORY_SYMBOL_INDEX; i++) {
            if (Objects.equals(args[i], EMPTY_SPACE)) {
                return Optional.empty();
            }
            memorySymbols.add(args[i]);
        }
        List<String> aiSymbols = new ArrayList<>();
        for (int i = FIRST_AI_SYMBOL_INDEX; i < args.length; i++) {
            if (Objects.equals(args[i], EMPTY_SPACE)) {
                return Optional.empty();
            }
            aiSymbols.add(args[i]);
        }
        if (hasDuplicates(memorySymbols) || hasDuplicates(aiSymbols)) {
            return Optional.empty();
        }
        return Optional.of(new CodeFight(memorySize, memorySymbols, aiSymbols));
    }


    private static boolean hasDuplicates(List<String> array) {
        Set<String> set = new HashSet<>();
        for (String element : array) {
            if (!set.add(element)) {
                return true;
            }
        }
        return false;
    }
}
