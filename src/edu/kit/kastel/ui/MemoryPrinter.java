package edu.kit.kastel.ui;

import edu.kit.kastel.model.CyclicLinkedList;
import edu.kit.kastel.model.ai.InstructionName;
import edu.kit.kastel.model.memory.MemoryCell;

/**
 * Prints the memory as output.
 *
 * @author uiiux
 */
public class MemoryPrinter {
    private final CyclicLinkedList<MemoryCell> memory;
    private final int size;
    private final String boundsSymbol;

    /**
     * Initializes the printer of memory.
     *
     * @param memory  memory
     * @param size    size of the memory
     * @param boundsSymbol symbol used to display bounds while printing memory
     */
    public MemoryPrinter(CyclicLinkedList<MemoryCell> memory, int size, String boundsSymbol) {
        this.memory = memory;
        this.size = size;
        this.boundsSymbol = boundsSymbol;
    }


    /**
     * Prints every cell of the memory in detail.
     * @param cell Memory cell
     * @return Memory cells in detail
     */
    public String printDetail(int cell) {
        MemoryCell currentCell = memory.get(cell);
        MemoryCell startingCell = currentCell;
        StringBuilder sb = new StringBuilder();

        int maxPosLength = 0;
        int maxNameLength = 0;
        int maxFirstArgLength = 0;
        int maxSecondArgLength = 0;
        for (int i = 0; i < 10; i++) {
            InstructionName name = currentCell.getInstruction();
            int currentCellPosition = memory.getPosition(currentCell);
            maxPosLength = Math.max(maxPosLength, Integer.toString(currentCellPosition).length());
            maxNameLength = Math.max(maxNameLength, name.toString().length());
            maxFirstArgLength = Math.max(maxFirstArgLength, Integer.toString(currentCell.getFirstArgument()).length());
            maxSecondArgLength = Math.max(maxSecondArgLength, Integer.toString(currentCell.getSecondArgument()).length());
            currentCell = memory.getNext(currentCell);
        }

        currentCell = startingCell;

        for (int i = 0; i < 10; i++) {
            String name = currentCell.getInstruction().toString();
            int firstArgument = currentCell.getFirstArgument();
            int secondArgument = currentCell.getSecondArgument();
            int currentCellPosition = memory.getPosition(currentCell);
            String formattedLine = String.format(
                    "%s %" + maxPosLength + "d: %5s | %" + maxFirstArgLength + "d | %"
                            + maxSecondArgLength + "d" + System.lineSeparator(),
                    currentCell.getCurrentSymbol(), currentCellPosition, name, firstArgument, secondArgument);
            sb.append(formattedLine);
            currentCell = memory.getNext(currentCell);
        }

        sb.insert(0, printOverViewWithBounds(startingCell, currentCell) + System.lineSeparator());
        return sb.toString().trim();
    }


    /**
     * Prints an overview of the memory.
     * @return Overview of memory
     */
    public StringBuilder printOverview() {
        StringBuilder symbolsLine = new StringBuilder();
        for (int cellNo = 0; cellNo < size; cellNo++) {
            MemoryCell currentCell = memory.get(cellNo);
            symbolsLine.append(currentCell.getCurrentSymbol());
        }
        return symbolsLine;
    }

    private String printOverViewWithBounds(MemoryCell start, MemoryCell end) {
        StringBuilder symbolsLine = new StringBuilder();
        for (int cellNo = 0; cellNo < size; cellNo++) {
            MemoryCell currentCell = memory.get(cellNo);
            if (memory.getPosition(currentCell) == memory.getPosition(start)) {
                symbolsLine.append(boundsSymbol);
            }
            symbolsLine.append(currentCell.getCurrentSymbol());
            if (memory.getPosition(currentCell) == memory.getPosition(end) - 1) {
                symbolsLine.append(boundsSymbol);
            }
        }
        return symbolsLine.toString();
    }
}

