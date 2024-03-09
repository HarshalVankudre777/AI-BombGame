
package edu.kit.kastel.ui.command.playing;

import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.memory.MemoryCell;
import edu.kit.kastel.ui.command.Command;
import edu.kit.kastel.ui.command.CommandResult;
import edu.kit.kastel.ui.command.CommandResultType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This commands executes the next steps.
 *
 * @author Programmieren-Team
 */
public class NextCommand implements Command {
    private static final String INVALID_STEPS_ERROR = "Invalid Steps!";
    private static final String GAME_NOT_STARTED_ERROR = "Game not yet started.";
    private static final String STOPPED_AI_OUTPUT_FORMAT = "%s executed %d steps until stopping.";
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int STEPS_INDEX = 0;

    private final List<AI> alreadyStopped;


    /**
     * Initializes a new list for AIs that are already stopped.
     */

    public NextCommand() {
        alreadyStopped = new ArrayList<>();
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
        if (!model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.FAILURE, GAME_NOT_STARTED_ERROR);
        }
        int cellsToExecute = 1;
        if (commandArguments.length == 1) {
            try {
                cellsToExecute = Integer.parseInt(commandArguments[STEPS_INDEX]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, INVALID_STEPS_ERROR);
            }
        }
        for (int i = 0; i < cellsToExecute; i++) {
            model.getCurrentAI().execute(model.getAiCommandExecutor());
            if (model.getStoppedAIList().size() == model.getRunningAI().size()) {
                break;
            }
            for (int j = 0; j < model.getRunningAI().size(); j++) {
                model.updateAI();
                if (model.getCurrentAI().isStopped()) {
                    break;
                }
            }

            updateNextSymbols(model);

        }
        if (model.getStoppedAIList().isEmpty()) {
            return new CommandResult(CommandResultType.SUCCESS, null);
        }
        if (!model.isAllAIsStopped()) {
            if (model.getPlayingList().size() == model.getStoppedAIList().size()) {
                model.setAllAIsStopped(true);
            }
            return new CommandResult(CommandResultType.SUCCESS, parseOutput(model.getStoppedAIList()));
        }
        return new CommandResult(CommandResultType.SUCCESS, null);
    }

    private void updateNextSymbols(CodeFight model) {
        Map<Integer, String> tempSymbols = new HashMap<>();
        for (int i = 0; i < model.getRunningAI().size(); i++) {
            AI ai = model.getRunningAI().get(i);
            if (model.getStoppedAIList().contains(ai)) {
                continue;
            }
            int nextCellIndex = ai.getNextCellIndex();
            tempSymbols.put(nextCellIndex, model.getOtherSymbol());
        }

        for (int i = 0; i < model.getRunningAI().size(); i++) {
            AI ai = model.getRunningAI().get(i);
            MemoryCell nextCell = model.getMemory().get(ai.getNextCellIndex());
            if (model.getStoppedAIList().contains(ai)) {
                continue;
            }
            if (ai.equals(model.getCurrentAI())) {
                nextCell.setCurrentSymbol(model.getCurrentSymbol());
                tempSymbols.remove(ai.getNextCellIndex());
            } else if (tempSymbols.containsKey(ai.getNextCellIndex())) {
                nextCell.setCurrentSymbol(tempSymbols.get(ai.getNextCellIndex()));
            }
        }
    }


    private String parseOutput(List<AI> stoppedAIList) {
        StringBuilder sb = new StringBuilder();
        for (AI ai : stoppedAIList) {
            if (alreadyStopped.contains(ai)) {
                continue;
            }
            alreadyStopped.add(ai);
            sb.append(STOPPED_AI_OUTPUT_FORMAT.formatted(ai.getName(), ai.getStepsExecuted()));
            sb.append(System.lineSeparator());
            ai.incrementStepsExecuted();
        }
        if (!sb.isEmpty()) {
            return sb.toString().trim();
        }
        return null;
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
