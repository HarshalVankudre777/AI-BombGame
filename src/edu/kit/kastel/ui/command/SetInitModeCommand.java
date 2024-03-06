package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.memory.Mode;

/**
 * This Commands sets the Mode of the Memory.
 * @author Programmieren-Team
 */
public class SetInitModeCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 2;
    private static final int MODE_INDEX = 0;
    private static final int SEED_INDEX = 1;
    private static final String STOP_MODE_SYNTAX = "INIT_MODE_STOP";
    private static final String RANDOM_MODE_SYNTAX = "INIT_MODE_RANDOM";
    private static final String CHANGE_WHILE_PLAYING_ERROR = "Cannot change modes when game is running";
    private static final String INVALID_SEED_ERROR = "Invalid seed.";
    private static final String CHANGED_MODE_TO_STOP_MESSAGE =
            "Changed init mode from INIT_MODE_RANDOM 0 to INIT_MODE_STOP.";
    private static final String CHANGED_MODE_TO_RANDOM_MESSAGE =
            "Changed init mode from INIT_MODE_STOP to INIT_MODE_RANDOM %d.";

    private static final String MODE_CANNOT_CHANGE_ERROR = "Mode cannot be changed.";



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
            return new CommandResult(CommandResultType.FAILURE, CHANGE_WHILE_PLAYING_ERROR);
        }
        String mode = commandArguments[MODE_INDEX];
        long seed = 0;
        if (commandArguments.length > 1) {
            try {
                seed = Long.parseLong(commandArguments[SEED_INDEX]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, INVALID_SEED_ERROR);
            }
        }

        if (mode.equals(STOP_MODE_SYNTAX) && model.getMemoryMode() == Mode.RANDOM) {
            model.getMemoryInitializer().initializeWithDefault();
            model.setMemoryMode(Mode.STOP);
            return new CommandResult(CommandResultType.SUCCESS, CHANGED_MODE_TO_STOP_MESSAGE);
        } else if (mode.equals(RANDOM_MODE_SYNTAX) && model.getMemoryMode() == Mode.STOP
                && commandArguments.length == 2) {
            model.getMemoryInitializer().initializeWithRandoms(seed);
            model.setMemoryMode(Mode.RANDOM);
            return new CommandResult(CommandResultType.SUCCESS, String.format(CHANGED_MODE_TO_RANDOM_MESSAGE, seed));
        }
        return new CommandResult(CommandResultType.FAILURE, MODE_CANNOT_CHANGE_ERROR);
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
