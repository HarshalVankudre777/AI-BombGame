package edu.kit.kastel.ui.command;

import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.CodeFight;
import edu.kit.kastel.model.memory.MemoryCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This command starts the game.
 *
 * @author uiiux
 */
public class StartGameCommand implements Command {

    private static final String AI_DOES_NOT_EXIST = "AI %s does not exist.";
    private static final String GAME_STARTED_MESSAGE = "Game started.";
    private static final String GAME_HAS_ALREADY_STARTED_ERROR = "Game is already running.";
    private static final int NUMBER_OF_SYMBOLS_PER_AI = 2;
    private static final int LOWER_LIMIT_NUMBER_OF_ARGUMENTS = 2;
    private final CodeFight codeFight;

    /**
     * provides access to the game model.
     *
     * @param codeFight game model
     */
    public StartGameCommand(CodeFight codeFight) {
        this.codeFight = codeFight;
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

        if (model.isPlayingPhase()) {
            return new CommandResult(CommandResultType.FAILURE, GAME_HAS_ALREADY_STARTED_ERROR);
        }
        for (String argument : commandArguments) {
            boolean aiFound = false;
            for (AI ai : model.getListOfAI()) {
                if (argument.equals(ai.getName())) {
                    model.getPlayingList().add(ai);
                    aiFound = true;
                    break;
                }
            }
            if (!aiFound) {
                return new CommandResult(CommandResultType.FAILURE, String.format(AI_DOES_NOT_EXIST, argument));
            }
        }
        renameDuplicates(model);
        for (AI ai : model.getPlayingList()) {
            model.getRunningAI().add(ai);
        }
        model.assignSymbols();
        model.loadMemory();
        model.createAICommandExecutor();
        model.gameHandler();
        model.setPlayingPhase(true);
        return new CommandResult(CommandResultType.SUCCESS, GAME_STARTED_MESSAGE);
    }

    /**
     * Renames all the duplicate AI's that are going to play
     *
     * @param model the game model
     */
    private void renameDuplicates(CodeFight model) {
        List<AI> updatedList = new ArrayList<>();
        HashMap<String, Integer> nameCount = new HashMap<>();
        for (AI ai : model.getPlayingList()) {
            String aiName = ai.getName();
            nameCount.put(aiName, nameCount.getOrDefault(aiName, 0) + 1);
        }

        HashMap<String, Integer> currentIndex = new HashMap<>();
        for (AI ai : model.getPlayingList()) {
            String aiName = ai.getName();
            if (nameCount.get(aiName) > 1) {
                int index = currentIndex.getOrDefault(aiName, 0);
                AI newAI = copyAI(ai, aiName + "#" + index);
                updatedList.add(newAI);
                currentIndex.put(aiName, index + 1);
            } else {
                updatedList.add(ai);
            }
        }

        model.getPlayingList().clear();
        model.getPlayingList().addAll(updatedList);
    }

    /**
     * Deep copies a AI
     * @param ai      AI to be copies
     * @param newName new name of the new AI
     * @return        new deep copy of the AI
     */
    private AI copyAI(AI ai, String newName) {
        if (ai == null || newName == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        List<MemoryCell> instructions = new ArrayList<>();
        for (MemoryCell memoryCell: ai.getAiCommands()) {
            MemoryCell copyOfMemoryCell = memoryCell.duplicate();
            if (copyOfMemoryCell != null) {
                instructions.add(copyOfMemoryCell);
            }
        }
        AI newAI = new AI(newName, instructions);
        newAI.setDefaultSymbol(ai.getDefaultSymbol());
        newAI.setBombSymbol(ai.getBombSymbol());
        return newAI;
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
        return codeFight.getAiSymbols().size() / NUMBER_OF_SYMBOLS_PER_AI;
    }
}
