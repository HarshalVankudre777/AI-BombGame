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
    private static final String MEMORY_AREA_DISPLAY_FORMAT = "%s %2d: %5s | %2d | %2d" + System.lineSeparator();
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
        for (int i = cell; i < cell + 10; i++) {
            InstructionName name = currentCell.getInstruction();
            int firstArgument = currentCell.getFirstArgument();
            int secondArgument = currentCell.getSecondArgument();
            int currentCellPosition = memory.getPosition(currentCell);
            sb.append(String.format(MEMORY_AREA_DISPLAY_FORMAT, currentCell.getCurrentSymbol(),
                    currentCellPosition, name, firstArgument, secondArgument)
            );
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
            symbolsLine.append(currentCell.getCurrentSymbol());
            if (memory.getPosition(currentCell) == memory.getPosition(start) - 1) {
                symbolsLine.append(boundsSymbol);
            }
            if (memory.getPosition(currentCell) == memory.getPosition(end) - 1) {
                symbolsLine.append(boundsSymbol);
            }
        }
        return symbolsLine.toString();
    }
}

