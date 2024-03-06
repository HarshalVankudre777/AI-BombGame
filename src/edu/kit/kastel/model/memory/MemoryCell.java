package edu.kit.kastel.model.memory;

import edu.kit.kastel.model.ai.InstructionName;

/**
 * Represents a cell in program memory, holding an AI instruction and arguments.
 *
 * @author uiiux
 */
public class MemoryCell {

    private final InstructionName instruction;
    private int firstArgument;
    private int secondArgument;
    private String currentSymbol;
    private String bombSymbol;
    private String defaultSymbol;

    /**
     * Constructs a new MemoryCell with the specified instruction.
     *
     * @param instruction    The AICommand instruction to be held by this cell.
     * @param firstArgument  The first argument to be held by this cell.
     * @param secondArgument The second argument to be held by this cell.
     */
    public MemoryCell(InstructionName instruction, int firstArgument, int secondArgument) {
        this.instruction = instruction;
        this.firstArgument = firstArgument;
        this.secondArgument = secondArgument;
    }

    /**
     * Gets the AI instruction stored in this MemoryCell.
     *
     * @return the AICommand instruction.
     */
    public InstructionName getInstruction() {
        return instruction;
    }

    /**
     * Gets the first argument associated with the instruction.
     *
     * @return the first argument.
     */
    public int getFirstArgument() {
        return firstArgument;
    }

    /**
     * Gets the second argument associated with the instruction.
     *
     * @return the second argument.
     */
    public int getSecondArgument() {
        return secondArgument;
    }

    /**
     * Sets the first argument for this cell's instruction.
     *
     * @param firstArgument The first argument value.
     */
    public void setFirstArgument(int firstArgument) {
        this.firstArgument = firstArgument;
    }

    /**
     * Sets the second argument for this cell's instruction.
     *
     * @param secondArgument The second argument value.
     */
    public void setSecondArgument(int secondArgument) {
        this.secondArgument = secondArgument;
    }

    /**
     * Gets the current symbol associated with this memory cell.
     *
     * @return the current symbol.
     */
    public String getCurrentSymbol() {
        return currentSymbol;
    }

    /**
     * Sets the current symbol for this memory cell.
     *
     * @param currentSymbol The current symbol value.
     */
    public void setCurrentSymbol(String currentSymbol) {
        this.currentSymbol = currentSymbol;
    }

    /**
     * Sets the bomb symbol for this memory cell.
     *
     * @param bombSymbol The bomb symbol value.
     */
    public void setBombSymbol(String bombSymbol) {
        this.bombSymbol = bombSymbol;
    }

    /**
     * Gets the default symbol associated with this memory cell.
     *
     * @return the default symbol.
     */
    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    /**
     * Sets the default symbol for this memory cell.
     *
     * @param defaultSymbol The default symbol value.
     */
    public void setDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
    }

    /**
     * Creates a deep copy of this MemoryCell.
     *
     * @return A deep-copy of this MemoryCell.
     */
    public MemoryCell duplicate() {
        MemoryCell clonedCell = new MemoryCell(instruction, firstArgument, secondArgument);
        clonedCell.setCurrentSymbol(currentSymbol);
        clonedCell.setBombSymbol(bombSymbol);
        clonedCell.setDefaultSymbol(defaultSymbol);
        return clonedCell;
    }
}
