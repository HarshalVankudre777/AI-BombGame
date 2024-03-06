
package edu.kit.kastel.ui.command;



import edu.kit.kastel.model.CodeFight;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class handles the user input and executes the commands.
 * 
 * @author Programmieren-Team
 */
public final class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = " +";
    private static final String ERROR_PREFIX = "Error, ";
    private static final String QUIT_COMMAND = "quit";
    private static final String HELP_COMMAND = "help";
    private static final String ADD_AI_COMMAND = "add-ai";
    private static final String REMOVE_AI_COMMAND = "remove-ai";
    private static final String SET_INIT_MODE_COMMAND = "set-init";
    private static final String START_GAME_COMMAND = "start-game";
    private static final String END_GAME_COMMAND = "end-game";
    private static final String NEXT_COMMAND = "next";
    private static final String SHOW_AI_COMMAND = "show-ai";
    private static final String SHOW_MEMORY_COMMAND = "show-memory";
    private static final String COMMAND_NOT_FOUND_FORMAT = "command '%s' not found!";
    private static final String WRONG_ARGUMENTS_COUNT_FORMAT = "wrong number of arguments for command '%s'!";
    private static final String INVALID_RESULT_TYPE_FORMAT = "Unexpected value: %s";

    private final CodeFight codeFight;
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs a new CommandHandler.
     *
     * @param codeFight the codefight game that this instance manages
     */
    public CommandHandler(CodeFight codeFight) {
        this.codeFight = Objects.requireNonNull(codeFight);
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Starts the interaction with the user.
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the interaction with the user.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splitCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);
        String commandName = splitCommand[0];
        String[] commandArguments = Arrays.copyOfRange(splitCommand, 1, splitCommand.length);

        executeCommand(commandName, commandArguments);
    }

    private void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            System.err.println(ERROR_PREFIX + COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
        } else if (commandArguments.length < commands.get(commandName).lowerLimitedNumberOfArguments()
                || commandArguments.length > commands.get(commandName).upperLimitGetNumberGfArguments()) {
            System.err.println(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));
        } else {
            CommandResult result = commands.get(commandName).execute(codeFight, commandArguments);
            String output = switch (result.getType()) {
                case SUCCESS -> result.getMessage();
                case FAILURE -> ERROR_PREFIX + result.getMessage();
            };
            if (output != null) {
                switch (result.getType()) {
                    case SUCCESS -> System.out.println(output);
                    case FAILURE -> System.err.println(output);
                    default -> throw new IllegalStateException(INVALID_RESULT_TYPE_FORMAT.formatted(result.getType()));
                }
            }
        }
    }

    private void initCommands() {
        addCommand(QUIT_COMMAND, new QuitCommand(this));
        addCommand(HELP_COMMAND, new HelpCommand());
        addCommand(ADD_AI_COMMAND, new AddAICommand());
        addCommand(REMOVE_AI_COMMAND, new RemoveAICommand());
        addCommand(SET_INIT_MODE_COMMAND, new SetInitModeCommand());
        addCommand(START_GAME_COMMAND, new StartGameCommand(codeFight));
        addCommand(END_GAME_COMMAND, new EndGameCommand());
        addCommand(NEXT_COMMAND, new NextCommand());
        addCommand(SHOW_AI_COMMAND, new ShowAiCommand());
        addCommand(SHOW_MEMORY_COMMAND, new ShowMemoryCommand());
    }

    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }

}




