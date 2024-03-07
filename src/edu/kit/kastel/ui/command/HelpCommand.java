package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;

/**
 * This commands provides help.
 *
 * @author Programmieren-Team
 */
public class HelpCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 0;

    private static final String INITIALIZATION_HELP_TEXT = """
            add-ai: Adds a new AI into the game.
            remove-ai: Removes a AI from the game.
            set-init: Initializes the memory with either default values or random.
            start-game: The game progress to the playing phase with the specifies AI's.
            """;

    private static final String PLAYING_PHASE_HELP_TEXT = """
            next: No of steps to be executed.
            show-memory: Shows either overview of memory or in detail.
            show-ai: Shows the current state of the AI.
            end-game: Ends the game, the game switches back to the Initialization phase.
            """;


    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */

    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        if (!model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.SUCCESS, INITIALIZATION_HELP_TEXT);
        } else {
            return new CommandResult(CommandResultType.SUCCESS, PLAYING_PHASE_HELP_TEXT);
        }
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

