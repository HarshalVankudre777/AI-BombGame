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
    private static final String ADD_AI_HELP_TEXT = "Adds a new AI into the game.";
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

        return new CommandResult(CommandResultType.SUCCESS, helpMap.toString());


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

