package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;

import java.util.HashMap;
import java.util.Map;

/**
 * This commands provides help.
 *
 * @author Programmieren-Team
 */
public class HelpCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final String ADD_AI_COMMAND = "add-ai";
    private static final String REMOVE_AI_COMMAND = "remove-ai";
    private static final String HELP_COMMAND = "help";
    private static final String SET_INIT_MODE_COMMAND = "set-init-mode";
    private static final String START_GAME_COMMAND = "start-game";
    private static final String END_GAME_COMMAND = "end-game";
    private static final String NEXT_COMMAND = "next";
    private static final String SHOW_AI_COMMAND = "show-ai";
    private static final String SHOW_MEMORY_COMMAND = "show-memory";
    private static final String ADD_AI_HELP_TEXT = "Adds a new AI into the game.";
    private static final String HELP_TEXT = "Shows the description of commands for the current phase of the game";
    private static final String REMOVE_AI_HELP_TEXT = "Removes a AI from the game.";
    private static final String SET_INIT_HELP_TEXT = "Initializes the memory with either default values or random.";
    private static final String START_GAME_HELP_TEXT = "The game progress to the playing phase with the specific AI's.";
    private static final String NEXT_HELP_TEXT = "Executes next no of steps to be executed.";
    private static final String SHOW_MEMORY_HELP_TEXT = "Shows either overview of memory or in detail.";
    private static final String SHOW_AI_HELP_TEXT = "Shows the current state of the AI.";
    private static final String END_GAME_HELP_TEXT = "Ends the game, the game switches back to the Initialization phase.";
    private static final String HELP_FORMAT = "%s: %s";

    private final Map<String, String> helpMap;

    /**
     * Initializes the help map.
     */
    public HelpCommand() {
        this.helpMap = new HashMap<>();
    }


    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */

    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        // Clear the helpMap to ensure it only contains relevant commands for each execution
        helpMap.clear();

        if (!model.isPlayingPhase()) {
            helpMap.put(ADD_AI_COMMAND, ADD_AI_HELP_TEXT);
            helpMap.put(HELP_COMMAND, HELP_TEXT);
            helpMap.put(REMOVE_AI_COMMAND, REMOVE_AI_HELP_TEXT);
            helpMap.put(SET_INIT_MODE_COMMAND, SET_INIT_HELP_TEXT);
            helpMap.put(START_GAME_COMMAND, START_GAME_HELP_TEXT);
        } else {

            helpMap.put(END_GAME_COMMAND, END_GAME_HELP_TEXT);
            helpMap.put(NEXT_COMMAND, NEXT_HELP_TEXT);
            helpMap.put(SHOW_AI_COMMAND, SHOW_AI_HELP_TEXT);
            helpMap.put(SHOW_MEMORY_COMMAND, SHOW_MEMORY_HELP_TEXT);
        }

        StringBuilder helpText = new StringBuilder();
        for (Map.Entry<String, String> entry : helpMap.entrySet()) {
            helpText.append(String.format(HELP_FORMAT, entry.getKey(), entry.getValue()));
            helpText.append(System.lineSeparator());
        }

        return new CommandResult(CommandResultType.SUCCESS, helpText.toString().trim());
    }



    /**
     * Returns the number of arguments that the command expects.
     *
     * @return the number of arguments that the command expects
     */
    @Override
    public int lowerLimitedNumberOfArguments() {
        return LOWER_LIMIT_NUMBER_OF_ARGUMENTS;
    }

    /**
     * Returns the most number of arguments that the command expects.
     *
     * @return the upper limit number of arguments that the command expects
     */
    @Override
    public int upperLimitGetNumberGfArguments() {
        return UPPER_LIMIT_NUMBER_OF_ARGUMENTS;
    }
}

