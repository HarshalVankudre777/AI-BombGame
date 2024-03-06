package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.ai.AI;

import java.util.ArrayList;
import java.util.List;

/**
 * This commands ends the game.
 *
 * @author Programmieren-Team
 */
public class EndGameCommand implements Command {
    private static final String GAME_NOT_STARTED_ERROR = "Game not yet started.";
    private static final String RUNNING_AI_FORMAT = "Running AIs: %s";
    private static final String STOPPED_AI_FORMAT = "Stopped AIs: %s";
    private static final String AI_NAME_SEPARATOR = ", ";
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 0;

    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        model.getRunningAI().clear();
        StringBuilder message = new StringBuilder();
        if (!model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.FAILURE, GAME_NOT_STARTED_ERROR);
        }
        if (model.getPlayingList().size() == model.getStoppedAIList().size()) {
            message.append(STOPPED_AI_FORMAT.formatted(
                    getStoppedAIList(model.getPlayingList(), model.getStoppedAIList()))
            );
        } else if (model.getStoppedAIList().isEmpty()) {
            message.append(RUNNING_AI_FORMAT.formatted(
                    getRunningAIList(model.getPlayingList(), model.getStoppedAIList()))
            );
        } else {
            message.append(RUNNING_AI_FORMAT.formatted(
                    getRunningAIList(model.getPlayingList(), model.getStoppedAIList()))
            );
            message.append(System.lineSeparator());
            message.append(STOPPED_AI_FORMAT.formatted(
                    getStoppedAIList(model.getPlayingList(), model.getStoppedAIList()))
            );

        }
        model.setPlayingPhase(false);
        model.getPlayingList().clear();
        model.getStoppedAIList().clear();
        return new CommandResult(CommandResultType.SUCCESS, message.toString());
    }


    private String getRunningAIList(List<AI> playingAIlist, List<AI> stoppedAIList) {
        StringBuilder sb = new StringBuilder();
        List<AI> tempList = new ArrayList<>(playingAIlist);
        for (AI ai : stoppedAIList) {
            tempList.remove(ai);
        }
        for (AI ai : tempList) {
            sb.append(ai.getName()).append(AI_NAME_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    private String getStoppedAIList(List<AI> playingList, List<AI> stoppedAIList) {
        List<AI> temp = new ArrayList<>(playingList);
        StringBuilder sb = new StringBuilder();
        for (AI ai : playingList) {
            if (!stoppedAIList.contains(ai)) {
                temp.remove(ai);
            }
        }
        for (AI ai : temp) {
            sb.append(ai.getName()).append(AI_NAME_SEPARATOR);
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
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

