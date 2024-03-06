
package edu.kit.kastel.ui.command;


import edu.kit.kastel.model.CodeFight;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 */
public interface Command {

    /**
     * Executes the command.
     * 
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(CodeFight model, String[] commandArguments);

    /**
     * Returns the least number of arguments that the command expects.
     * 
     * @return the lower limit number of arguments that the command expects
     */
    int lowerLimitedNumberOfArguments();

    /**
     * Returns the most number of arguments that the command expects.
     *
     * @return the upper limit number of arguments that the command expects
     */
    int upperLimitGetNumberGfArguments();
}

