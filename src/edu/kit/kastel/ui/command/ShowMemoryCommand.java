package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;

/**
 * This class represents a command that shows the memory.
 *
 * @author Programmieren-Team
 */
public class ShowMemoryCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 1;
    private static final String INVALID_CELL_NUMBER = "Invalid cell number.";


    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(CodeFight model, String[] commandArguments) {
        if (commandArguments.length == 0) {
            return new CommandResult(CommandResultType.SUCCESS, model.getMemoryPrinter().printOverview().toString());
        }
        int startingCell;
        try {
            startingCell = Integer.parseInt(commandArguments[0]);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_CELL_NUMBER);
        }
        if (startingCell > model.getMemorySize()) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_CELL_NUMBER);
        }
        return new CommandResult(CommandResultType.SUCCESS, model.getMemoryPrinter().printDetail(startingCell));
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
