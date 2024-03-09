package edu.kit.kastel.ui.command.initialization;

import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.ui.command.Command;
import edu.kit.kastel.ui.command.CommandResult;
import edu.kit.kastel.ui.command.CommandResultType;

/**
 * This command removes an AI.
 *
 * @author uiiux
 */
public class RemoveAICommand implements Command {

    private static final String AI_NOT_FOUND_ERROR = "AI not found.";
    private static final String REMOVE_WHILE_PLAYING_ERROR = " Cannot remove when game is running!";
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int AI_NAME_INDEX = 0;

    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        if (model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.FAILURE, REMOVE_WHILE_PLAYING_ERROR);
        }
        String aiName = commandArguments[AI_NAME_INDEX];
        for (AI ai : model.getListOfAI()) {
            if (ai.getName().equals(aiName)) {
                model.getListOfAI().remove(ai);
                return new CommandResult(CommandResultType.SUCCESS, aiName);
            }
        }
        return new CommandResult(CommandResultType.FAILURE, AI_NOT_FOUND_ERROR);
    }

    /**
     * Returns the number of arguments that the command expects.
     *
     * @return the number array of arguments that the command expects
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
