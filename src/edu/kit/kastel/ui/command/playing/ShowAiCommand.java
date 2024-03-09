package edu.kit.kastel.ui.command.playing;

import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.memory.MemoryCell;
import edu.kit.kastel.ui.command.Command;
import edu.kit.kastel.ui.command.CommandResult;
import edu.kit.kastel.ui.command.CommandResultType;

/**
 * This command shows the state of the AI.
 *
 * @author uiiux
 */
public class ShowAiCommand implements Command {
    private static final String GAME_NOT_STARTED_ERROR = "Game not yet started.";
    private static final String AI_DISPLAY_FORMAT = "%s (%s@%d)";
    private static final String NEXT_CELL_FORMAT = "Next Command: %s|%d|%d @%d";
    private static final String RUNNING = "RUNNING";
    private static final String STOPPED = "STOPPED";
    private static final String AI_DOES_NOT_EXIST_ERROR = "AI does not exist!";
    private static final int AI_NAME_INDEX = 0;
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 1;

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
            return new CommandResult(CommandResultType.FAILURE, GAME_NOT_STARTED_ERROR);
        }
        String aiName = commandArguments[AI_NAME_INDEX];
        boolean aiFound = false;
        StringBuilder sb = new StringBuilder();
        for (AI ai : model.getPlayingList()) {
            if (ai.getName().equals(aiName)) {
                aiFound = true;
                if (model.getStoppedAIList().contains(ai)) {
                    sb.append(AI_DISPLAY_FORMAT.formatted(ai.getName(), STOPPED, ai.getStepsExecuted()));
                } else {
                    MemoryCell nextCell = model.getMemory().get(ai.getNextCellIndex());
                    sb.append(AI_DISPLAY_FORMAT.formatted(ai.getName(), RUNNING, ai.getStepsExecuted()));
                    sb.append(System.lineSeparator());
                    sb.append(NEXT_CELL_FORMAT.formatted(
                            nextCell.getInstruction(),
                            nextCell.getFirstArgument(),
                            nextCell.getSecondArgument(),
                            ai.getNextCellIndex())
                    );
                }
            }
        } if (!aiFound) {
            return new CommandResult(CommandResultType.FAILURE, AI_DOES_NOT_EXIST_ERROR);
        }
        return new CommandResult(CommandResultType.SUCCESS, sb.toString());
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
