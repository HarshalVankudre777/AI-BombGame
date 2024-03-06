package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.CodeFight;

/**
 * This command quits the program.
 *
 * @author Programmieren-Team
 */
public class QuitCommand implements Command {
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 0;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 0;

    private final CommandHandler commandHandler;

    /**
     * Provides access to the command handler.
     *
     * @param commandHandler command handler of the program
     */
    public QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
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
        commandHandler.quit();
        return new CommandResult(CommandResultType.SUCCESS, null);
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
