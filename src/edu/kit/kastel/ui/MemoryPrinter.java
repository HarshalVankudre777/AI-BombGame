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
    private static final String PRINT_DETAIL_FORMAT = "%%s %%%dd: %%%ds | %%%dd | %%%dd%%n";
    private static final int CELLS_TO_SHOW = 10;
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
        for (int i = 0; i < CELLS_TO_SHOW; i++) {
            InstructionName name = currentCell.getInstruction();
            int currentCellPosition = memory.getPosition(currentCell);
            maxPosLength = Math.max(maxPosLength, String.valueOf(currentCellPosition).length());
            maxNameLength = Math.max(maxNameLength, name.toString().length());
            maxFirstArgLength = Math.max(maxFirstArgLength,
                    String.valueOf(Math.abs(currentCell.getFirstArgument())).length());
            maxSecondArgLength = Math.max(maxSecondArgLength,
                    String.valueOf(Math.abs(currentCell.getSecondArgument())).length());

            currentCell = memory.getNext(currentCell);
            if (currentCell.equals(startingCell)) {
                break;
            }

        }

        String dynamicFormatPattern = String.format(PRINT_DETAIL_FORMAT,
                maxPosLength, maxNameLength,
                maxFirstArgLength, maxSecondArgLength);

        currentCell = startingCell;
        for (int i = 0; i < CELLS_TO_SHOW; i++) {
            int currentCellPosition = memory.getPosition(currentCell);
            String formattedLine = String.format(dynamicFormatPattern,
                    currentCell.getCurrentSymbol(), currentCellPosition,
                    currentCell.getInstruction().toString(),
                    currentCell.getFirstArgument(), currentCell.getSecondArgument());

            sb.append(formattedLine);
            if (i == CELLS_TO_SHOW - 1) {
                break;
            }

            if (memory.getNext(currentCell).equals(startingCell)) {
                break;
            }
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
            if (memory.getPosition(currentCell) == memory.getPosition(end)) {
                symbolsLine.append(boundsSymbol);
            }
        }
        return symbolsLine.toString();
    }
}

