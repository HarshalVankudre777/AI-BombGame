package edu.kit.kastel.model.ai;

import edu.kit.kastel.model.memory.MemoryCell;
import java.util.Collections;
import java.util.List;

/**
 * Stores all the attributes of AI.
 * Calls the AI command executor to execute an Instruction in the memory.
 *
 * @author uiiux
 */
public class AI {
    private String defaultSymbol;
    private String bombSymbol;
    private final String name;
    private final List<MemoryCell> aiCommands;

    private int stepsExecuted = 0;

    private int currentAIPosition = 0;
    private int memoryAllocated = 0;

    private int nextCellIndex;

    private boolean stopped = false;

    /**
     * Initializes the AI.
     *
     * @param name        name of the AI
     * @param aiCommands  Instruction Set of the AI
     */
    public AI(String name, List<MemoryCell> aiCommands) {
        this.name = name;
        this.aiCommands = aiCommands;
    }

    /**
     * Gets the name of the AI.
     *
     * @return name of the AI
     */
    public String getName() {
        return name;
    }

    /**
     * Increments the number of steps executed.
     */
    public void incrementStepsExecuted() {
        this.stepsExecuted++;
    }

    /**
     * Sets the default symbol of AI.
     *
     * @param defaultSymbol default symbol of AI
     */
    public void setDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
    }


    /**
     * Checks if the AI is stopped.
     *
     * @return true is AI is stopped
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Sets the bomb symbol of AI.
     *
     * @param bombSymbol bomb symbol of AI
     */
    public void setBombSymbol(String bombSymbol) {
        this.bombSymbol = bombSymbol;
    }

    /**
     * Gets the AI commands of AI.
     *
     * @return List of AI commands
     */
    public List<MemoryCell> getAiCommands() {
        return Collections.unmodifiableList(aiCommands);
    }

    /**
     * Gets the default Symbol of AI.
     *
     * @return default symbol of AI
     */

    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    /**
     * Gets the bomb Symbol of AI.
     *
     * @return bomb Symbol of AI
     */
    public String getBombSymbol() {
        return bombSymbol;
    }

    /**
     * Gets the steps executed by the AI.
     *
     * @return number of steps executed
     */
    public int getStepsExecuted() {
        return stepsExecuted;
    }

    /**
     * Sets the currentPosition of the AI.
     * If the currentPosition in memory has stop then it will skip that cell
     * so that the AI doesn't start with STOP.
     *
     * @param startIndex start index of the AI
     */
    public void setStartIndex(int startIndex) {
        int incrementValue = 0;
        for (MemoryCell aiCommand : aiCommands) {
            if (aiCommand.getInstruction().equals(InstructionName.STOP)) {
                incrementValue++;
            } else {
                break;
            }
        }
        currentAIPosition = startIndex + incrementValue;
        nextCellIndex = currentAIPosition;
    }

    /**
     * Stops the AI.
     */
    public void stop() {
        stopped = true;
    }

    /**
     * Sets the current Cell to be executed.
     * Executes the Instruction in the memory.
     * Gets the position of the next Instruction to be executed.
     *
     * @param aiCommandExecutor executes AI commands
     */

    public void execute(AICommandExecutor aiCommandExecutor) {
        aiCommandExecutor.setCurrentCell(currentAIPosition);
        aiCommandExecutor.setCurrentAI(this);
        aiCommandExecutor.executeCommand();
        if (!stopped) {
            stepsExecuted++;
            updateAIPosition(aiCommandExecutor.getNextCellIndex());
            nextCellIndex = aiCommandExecutor.getNextCellIndex();
        }

    }

    /**
     * Gets the index of the next cell this AI is going to execute.
     * @return index of the next cell
     */
    public int getNextCellIndex() {
        return nextCellIndex;
    }


    /**
     * Updates the current AI position.
     *
     * @param newPosition new position of AI
     */
    private void updateAIPosition(int newPosition) {
        currentAIPosition = newPosition;
    }

    /**
     * Gets the amount of memory allocated to the AI.
     *
     * @return size of memory allocated
     */
    public int getMemoryAllocated() {
        return memoryAllocated;
    }

    /**
     * Gets the amount of memory allocated to the AI.
     *
     * @param memoryAllocated size of memory allocated
     */
    public void setMemoryAllocated(int memoryAllocated) {
        this.memoryAllocated = memoryAllocated;
    }
}
