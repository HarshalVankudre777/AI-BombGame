package edu.kit.kastel.model.ai;

import edu.kit.kastel.model.CyclicLinkedList;
import edu.kit.kastel.model.memory.MemoryCell;

import java.util.List;

/**
 * Executes AI commands in the memory.
 *
 * @author uiiux
 */
public class AICommandExecutor {
    private static final String UNEXPECTED_VALUE_EXCEPTION = "Unexpected value: ";
    private final CyclicLinkedList<MemoryCell> memory;
    private final AICommands aiCommands;
    private MemoryCell currentCell;
    private final List<AI> stoppedAIList;
    private AI currentAI;

    /**
     * Initializes the AI command Executor.
     *
     * @param memory        memory of the program
     * @param stoppedAIList list of stopped AI's
     */
    public AICommandExecutor(CyclicLinkedList<MemoryCell> memory, List<AI> stoppedAIList) {
        this.memory = memory;
        aiCommands = new AICommands(memory);
        this.stoppedAIList = stoppedAIList;

    }

    /**
     * Checks the type of AI command in the cell.
     * calls the relevant method from the AI commands.
     *
     * @throws IllegalStateException throws exception if the AI command name is Invalid
     */
    public void executeCommand() {
        aiCommands.setCell(currentCell);
        currentCell.setCurrentSymbol(currentCell.getDefaultSymbol());
        int cellPosition = memory.getPosition(currentCell);
        aiCommands.setCellPosition(cellPosition);
        switch (currentCell.getInstruction()) {
            case STOP:
                aiCommands.stop(stoppedAIList, currentAI);
                break;
            case MOV_R:
                aiCommands.movR(currentAI);
                break;
            case MOV_I:
                aiCommands.movI(currentAI);
                break;
            case ADD:
                aiCommands.add();
                break;
            case ADD_R:
                aiCommands.addR(currentAI);
                break;
            case JMP:
                aiCommands.jmp();
                break;
            case JMZ:
                aiCommands.jmz();
                break;
            case CMP:
                aiCommands.cmp();
                break;
            case SWAP:
                aiCommands.swap(currentAI);
                break;
            default:
                throw new IllegalStateException(UNEXPECTED_VALUE_EXCEPTION + currentCell.getInstruction());
        }
        currentCell = aiCommands.getCell();
    }

    /**
     * Sets the current cell which is to be executed.
     *
     * @param currentCellIndex Index of the current Cell
     */
    public void setCurrentCell(int currentCellIndex) {
        this.currentCell = memory.get(currentCellIndex);
    }

    /**
     * Gets the index of the current Cell.
     *
     * @return index of current cell
     */
    public int getNextCellIndex() {
        return aiCommands.getNextCellIndex();
    }


    /**
     * Sets the current AI which will execute the Instruction.
     *
     * @param currentAI current AI
     */
    public void setCurrentAI(AI currentAI) {
        this.currentAI = currentAI;
    }

}
