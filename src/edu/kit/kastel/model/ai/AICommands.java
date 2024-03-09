package edu.kit.kastel.model.ai;

import edu.kit.kastel.model.CyclicLinkedList;
import edu.kit.kastel.model.memory.MemoryCell;

import java.util.List;

/**
 * Implements methods to execute AI commands on a cyclic memory structure.
 * This class is responsible for performing actions defined by AI instructions,
 * such as moving, adding, comparing cells, and more, based on the commands
 * stored within the memory cells.
 *
 * @author uiiux
 */
public class AICommands {
    private MemoryCell cell;
    private final CyclicLinkedList<MemoryCell> memory;
    private int cellPosition;
    private AI currentAI;

    /**
     * Creates an instance of AICommands with the specified memory.
     *
     * @param memory The cyclic linked list representing the memory.
     */
    public AICommands(CyclicLinkedList<MemoryCell> memory) {
        this.memory = memory;
    }

    /**
     * Stops the execution of the current AI.
     *
     * @param stoppedAIList A list collecting the AIs that have been stopped.
     */
    public void stop(List<AI> stoppedAIList) {
        if (currentAI.stopped()) {
            stoppedAIList.add(currentAI);
            currentAI.stop();
        }
    }

    /**
     * Moves data from a source cell to a target cell based on the first and second arguments of the current cell.
     */
    public void movR() {
        MemoryCell sourceCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell targetCell = memory.get(cellPosition + cell.getSecondArgument());
        transferCellData(sourceCell, targetCell);
        cell = memory.getNext(cell);
    }

    /**
     * Moves data from a source cell to a target cell using an intermediate cell to determine the target's position.
     */
    public void movI() {
        MemoryCell sourceCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell intermediateCell = memory.get(cellPosition + cell.getSecondArgument());
        int intermediateCellPosition = memory.getPosition(intermediateCell);
        MemoryCell targetCell = memory.get(intermediateCellPosition + intermediateCell.getSecondArgument());
        transferCellData(sourceCell, targetCell);
        cell = memory.getNext(cell);
    }

    /**
     * Adds the first argument of the current cell to its second argument and updates the cell accordingly.
     */
    public void add() {
        int result = cell.getFirstArgument() + cell.getSecondArgument();
        cell.setSecondArgument(result);
        cell = memory.getNext(cell);
    }

    /**
     * Adds the first argument of the current cell to the second argument of a target cell determined by the current cell's second argument.
     */
    public void addR() {
        MemoryCell targetCell = memory.get(cellPosition + cell.getSecondArgument());
        int result = cell.getFirstArgument() + targetCell.getSecondArgument();
        targetCell.setSecondArgument(result);
        cell = memory.getNext(cell);
    }

    /**
     * Compares the first argument of two cells and skips the next cell if they are not equal.
     */
    public void cmp() {
        MemoryCell firstCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell secondCell = memory.get(cellPosition + cell.getSecondArgument());
        if (firstCell.getFirstArgument() != secondCell.getSecondArgument()) {
            cell = memory.getNext(memory.getNext(cell));
        } else {
            cell = memory.getNext(cell);
        }
    }

    /**
     * Jumps to a specific cell determined by the current cell's first argument.
     */
    public void jmp() {
        cell = memory.get(cellPosition + cell.getFirstArgument());
    }

    /**
     * Jumps to a cell determined by the first argument if the second argument of the current cell is zero.
     */
    public void jmz() {
        MemoryCell checkCell = memory.get(cellPosition + cell.getSecondArgument());
        if (checkCell.getSecondArgument() == 0) {
            cell = memory.get(cellPosition + cell.getFirstArgument());
        } else {
            cell = memory.getNext(cell);
        }
    }

    /**
     * Swaps the arguments of two cells specified by the current cell's arguments.
     */
    public void swap() {
        MemoryCell firstCell = memory.get(cellPosition + cell.getFirstArgument());
        MemoryCell secondCell = memory.get(cellPosition + cell.getSecondArgument());
        int temp = firstCell.getFirstArgument();
        firstCell.setFirstArgument(secondCell.getSecondArgument());
        secondCell.setSecondArgument(temp);
        cell = memory.getNext(cell);
    }


    /**
     * Transfers data from the source cell to the target cell, including instruction and arguments.
     * This method also updates the symbol of the target cell based on the AI's configuration.
     *
     * @param sourceCell The cell from which data is copied.
     * @param targetCell The cell to which data is copied.
     */
    private void transferCellData(MemoryCell sourceCell, MemoryCell targetCell) {
        targetCell.setInstruction(sourceCell.getInstruction());
        targetCell.setFirstArgument(sourceCell.getFirstArgument());
        targetCell.setSecondArgument(sourceCell.getSecondArgument());
        assignSymbol(currentAI, sourceCell, targetCell);
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

    /**
     * Sets the Current AI.
     *
     * @param currentAI Current AI
     */
    public void setCurrentAI(AI currentAI) {
        this.currentAI = currentAI;
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
