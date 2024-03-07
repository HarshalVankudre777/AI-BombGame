package edu.kit.kastel.ui.command.initialization;

import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.memory.Mode;
import edu.kit.kastel.ui.command.Command;
import edu.kit.kastel.ui.command.CommandResult;
import edu.kit.kastel.ui.command.CommandResultType;

/**
 * This command sets the initialization mode of the memory.
 * It supports changing the mode to either STOP or RANDOM,
 * with the possibility of specifying a seed for the RANDOM mode.
 * Changing modes is not allowed while the game is running.
 * @author Programmieren-Team
 */
public class SetInitModeCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 2;
    private static final int MODE_INDEX = 0;
    private static final int SEED_INDEX = 1;
    private static final String STOP_MODE_SYNTAX = "INIT_MODE_STOP";
    private static final String RANDOM_MODE_SYNTAX = "INIT_MODE_RANDOM";
    private static final String CHANGE_WHILE_PLAYING_ERROR = "Cannot change modes when game is running.";
    private static final String INVALID_SEED_ERROR = "Invalid seed.";
    private static final String MODE_CANNOT_CHANGE_ERROR = "Mode cannot be changed.";
    private static final String CHANGED_MODE_TO_STOP_MESSAGE = "Changed init mode to INIT_MODE_STOP.";
    private static final String CHANGED_MODE_TO_RANDOM_MESSAGE = "Changed init mode to INIT_MODE_RANDOM with seed %d.";
    private static final String CHANGED_MODE_FROM_RANDOM_TO_RANDOM = "Changed seed from %d to %d in INIT_MODE_RANDOM.";

    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        if (model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.FAILURE, CHANGE_WHILE_PLAYING_ERROR);
        }

        String mode = commandArguments[MODE_INDEX];
        long seed = parseSeed(commandArguments);
        if (seed == -1) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_SEED_ERROR);
        }

        return switch (mode) {
            case STOP_MODE_SYNTAX -> handleStopMode(model);
            case RANDOM_MODE_SYNTAX -> handleRandomMode(model, seed, commandArguments.length);
            default -> new CommandResult(CommandResultType.FAILURE, MODE_CANNOT_CHANGE_ERROR);
        };
    }

    private long parseSeed(String[] commandArguments) {
        if (commandArguments.length > 1) {
            try {
                return Long.parseLong(commandArguments[SEED_INDEX]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return 0;
    }

    private CommandResult handleStopMode(CodeFight model) {
        model.getMemoryInitializer().initializeWithDefault();
        model.setMemoryMode(Mode.STOP);
        return new CommandResult(CommandResultType.SUCCESS, CHANGED_MODE_TO_STOP_MESSAGE);
    }

    private CommandResult handleRandomMode(CodeFight model, long seed, int argsLength) {
        if (argsLength == 2 && model.getMemoryInitializer().getSeed() != seed) {
            model.getMemoryInitializer().initializeWithRandoms(seed);
            model.setMemoryMode(Mode.RANDOM);
            String message = String.format(CHANGED_MODE_TO_RANDOM_MESSAGE, seed);
            return new CommandResult(CommandResultType.SUCCESS, message);
        } else if (argsLength == 2) {
            return new CommandResult(CommandResultType.SUCCESS, String.format(CHANGED_MODE_FROM_RANDOM_TO_RANDOM,
                    model.getMemoryInitializer().getSeed(), seed));
        }

        return new CommandResult(CommandResultType.FAILURE, MODE_CANNOT_CHANGE_ERROR);
    }

    @Override
    public int lowerLimitedNumberOfArguments() {
        return LOWER_LIMIT_NUMBER_OF_ARGUMENTS;
    }

    @Override
    public int upperLimitGetNumberGfArguments() {
        return UPPER_LIMIT_NUMBER_OF_ARGUMENTS;
    }
}
