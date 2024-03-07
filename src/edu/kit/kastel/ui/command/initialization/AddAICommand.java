package edu.kit.kastel.ui.command.initialization;

import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.ai.InstructionName;
import edu.kit.kastel.model.memory.MemoryCell;
import edu.kit.kastel.ui.command.Command;
import edu.kit.kastel.ui.command.CommandResult;
import edu.kit.kastel.ui.command.CommandResultType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This command adds an AI to the codefight.
 * @author Programmieren-Team
 * @version 1.0
 */
public class AddAICommand implements Command {
    private static final String INVALID_AI_NAME_ERROR = "Invalid AI name.";
    private static final String DUPLICATE_AI_NAME_ERROR = "AI with that name already exists.";
    private static final String INVALID_AI_INSTRUCTIONS_ERROR = "Invalid AI instructions.";
    private static final String ADDING_WHILE_PLAYING_ERROR = "Cannot add when the game is running!";
    private static final String TOO_MANY_INSTRUCTIONS_ERROR = "Too many AI Commands";
    private static final String INSTRUCTION_SEPARATOR = ",";
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 2;
    private static final int UPPER_LIMIT_NUMBER_OF_ARGUMENTS = 2;
    private static final int AI_NAME_INDEX = 0;
    private static final int AI_INSTRUCTIONS_INDEX = 1;
    private static final int ELEMENTS_IN_AI_COMMAND = 3;

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
            return new CommandResult(CommandResultType.FAILURE, ADDING_WHILE_PLAYING_ERROR);
        }
        if (commandArguments[AI_NAME_INDEX].contains(" ")) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_AI_NAME_ERROR);
        }
        String aiName = commandArguments[AI_NAME_INDEX];
        for (AI ai : model.getListOfAI()) {
            if (ai.getName().equals(aiName)) {
                return new CommandResult(CommandResultType.FAILURE, DUPLICATE_AI_NAME_ERROR);
            }
        }
        List<MemoryCell> instructionList = parseInstructions(commandArguments[AI_INSTRUCTIONS_INDEX]);
        if (instructionList.size() > (model.getMemorySize() / 2) + 1) {
            return new CommandResult(CommandResultType.FAILURE, TOO_MANY_INSTRUCTIONS_ERROR);
        }

        if (instructionList.isEmpty()) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_AI_INSTRUCTIONS_ERROR);
        }
        model.getListOfAI().add(new AI(aiName, instructionList));
        return new CommandResult(CommandResultType.SUCCESS, aiName);
    }

    /**
     * Parses the instructions in the list of Instruction Set.
     *
     * @param instructionsSet list of instructions
     * @return                parsed list of instructions
     */
    private List<MemoryCell> parseInstructions(String instructionsSet) {
        List<MemoryCell> tempInstructionList = new ArrayList<>();
        String[] instructions = instructionsSet.split(INSTRUCTION_SEPARATOR);
        boolean allInstructionsValid = instructions.length % ELEMENTS_IN_AI_COMMAND == 0;

        for (int i = 0; i <= instructions.length - 3; i += 3) {
            String instructionName = instructions[i];
            int firstArgument;
            int secondArgument;
            try {
                firstArgument = Integer.parseInt(instructions[i + 1]);
                secondArgument = Integer.parseInt(instructions[i + 2]);
            } catch (NumberFormatException e) {
                allInstructionsValid = false;
                break;
            }

            boolean found = false;
            for (InstructionName instruction : InstructionName.values()) {
                if (instruction.name().equals(instructionName)) {
                    tempInstructionList.add(new MemoryCell(instruction, firstArgument, secondArgument));
                    found = true;
                    break;
                }
            }
            if (!found) {
                allInstructionsValid = false;
                break;
            }
        }

        if (allInstructionsValid) {
            return tempInstructionList;
        } else {
            return Collections.emptyList();
        }
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
