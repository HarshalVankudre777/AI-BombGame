package edu.kit.kastel.model.ai;

import edu.kit.kastel.model.CyclicLinkedList;
import edu.kit.kastel.model.memory.MemoryCell;

import java.util.List;

/**
 * Implements methods for all the AI commands.
 *
 * @author uiiux
 */
public  class AICommands {
    private MemoryCell cell;
    private final CyclicLinkedList<MemoryCell> memory;
    private  int cellPosition;

    /**
     * Assigns the memory.
     * @param memory memory
     */
    public AICommands(CyclicLinkedList<MemoryCell> memory) {
        this.memory = memory;
    }

    /**
     * Stops the current AI.
     * @param stoppedAIList list of stopped AI's
     * @param currentAI     current AI
     */
    public void stop(List<AI> stoppedAIList, AI currentAI) {
        stoppedAIList.add(currentAI);
        currentAI.setStopped();
    }

    /**
     * Copies the source cell to the target position.
     * @param currentAI current AI
     */
    public void movR(AI currentAI) {
        MemoryCell sourceCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell clonedCell = sourceCell.duplicate();
        MemoryCell targetCell = memory.get(cellPosition + cell.getSecondArgument());
        int targetPosition = memory.getPosition(targetCell);
        assignSymbol(currentAI, sourceCell, clonedCell);
        memory.replace(targetPosition, clonedCell);
        cell = memory.getNext(cell);

    }

    /**
     * Copies the source cell to the target position with the help of an Intermediate cell.
     * @param currentAI current AI
     */
    public void movI(AI currentAI) {
        MemoryCell sourceCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell clonedCell = sourceCell.duplicate();
        MemoryCell intermediateCell = memory.get(cellPosition + cell.getSecondArgument());
        int intermediateCellPosition = memory.getPosition(intermediateCell);
        MemoryCell targetCell = memory.get(intermediateCellPosition + intermediateCell.getSecondArgument());
        int targetPosition = memory.getPosition(targetCell);
        assignSymbol(currentAI, sourceCell, clonedCell);
        memory.replace(targetPosition, clonedCell);
        cell = memory.getNext(cell);

    }

    /**
     * Adds the first Argument of cell to the second Argument.
     */
    public void add() {
        int modifiedSecondArgument = cell.getFirstArgument() + cell.getSecondArgument();
        cell.setSecondArgument(modifiedSecondArgument);
        cell = memory.getNext(cell);


    }

    /**
     * Adds the first argument of cell to the second argument of target cell.
     * @param currentAI current AI
     */
    public void addR(AI currentAI) {
        MemoryCell targetCell = memory.get(cellPosition + cell.getSecondArgument());
        targetCell.setSecondArgument(cell.getFirstArgument() + targetCell.getSecondArgument());
        assignSymbol(currentAI, targetCell, targetCell);
        cell = memory.getNext(cell);
    }

    /**
     * Compares two cells the skips next cell if they are not equal.
     */
    public void cmp() {
        int entryA = memory.get(cellPosition + cell.getFirstArgument()).getFirstArgument();
        int entryB = memory.get(cellPosition + cell.getSecondArgument()).getSecondArgument();
        if (entryA != entryB) {
            cell = memory.getNext(memory.getNext(cell));
        } else {
            cell = memory.getNext(cell);
        }
    }

    /**
     * Jumps to the specific cell.
     */
    public void jmp() {
        cell =  memory.get(cellPosition + cell.getFirstArgument());

    }

    /**
     * Jumps to the specific cell only if the second argument of target cell is 0.
     */
    public void jmz() {
        MemoryCell targetCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell checkCell = memory.get(cellPosition + cell.getSecondArgument());
        if (checkCell.getSecondArgument() == 0) {
            cell = targetCell;
        } else {
            cell = memory.getNext(cell);
        }
    }

    /**
     * Swaps the content of two cells.
     * @param currentAI current AI
     */
    public void swap(AI currentAI) {
        MemoryCell first = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell second = memory.get(cellPosition + cell.getSecondArgument());
        if (isBomb(first)) {
            second.setCurrentSymbol(currentAI.getBombSymbol());
            second.setDefaultSymbol(currentAI.getBombSymbol());
        }
        first.setFirstArgument(second.getSecondArgument());
        assignSymbol(currentAI, second, first);
        assignSymbol(currentAI, first, second);
        second.setSecondArgument(first.getFirstArgument());
        cell = memory.getNext(cell);
    }

    /**
     * Gets the current cell.
     * @return current Cell
     */
    public MemoryCell getCell() {
        return cell;
    }

    /**
     * Sets the current Cell.
     *
     * @param cell current cell
     */
    public void setCell(MemoryCell cell) {
        this.cell = cell;
    }

    /**
     * Gets position of the current cell.
     *
     * @param cellPosition current cell position
     */
    public void setCellPosition(int cellPosition) {
        this.cellPosition = cellPosition;
    }

    /**
     * Checks if the cell is a bomb or not.
     *
     * @param cell cell to be checked for a bomb
     * @return     true if it is bomb else false
     */
    private boolean isBomb(MemoryCell cell) {
        return cell.getInstruction().equals(InstructionName.STOP)
                || cell.getInstruction().equals(InstructionName.JMP) && cell.getFirstArgument() == 0
                || cell.getInstruction().equals(InstructionName.JMZ) && cell.getFirstArgument() == 0
                && cell.getSecondArgument() == 0;
    }

    /**
     * Gets the position of the next cell to be executed.
     *
     * @return position of next cell
     */
    public int getNextCellIndex() {
        return memory.getPosition(cell);
    }


    private void assignSymbol(AI currentAI, MemoryCell checkCell, MemoryCell targetCell) {
        if (isBomb(checkCell)) {
            targetCell.setCurrentSymbol(currentAI.getBombSymbol());
            targetCell.setDefaultSymbol(currentAI.getBombSymbol());
        } else {
            targetCell.setCurrentSymbol(currentAI.getDefaultSymbol());
            targetCell.setDefaultSymbol(currentAI.getDefaultSymbol());
        }

    }
}
