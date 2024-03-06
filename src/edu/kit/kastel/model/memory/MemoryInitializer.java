package edu.kit.kastel.model.memory;

import edu.kit.kastel.model.CyclicLinkedList;
import edu.kit.kastel.model.ai.InstructionName;
import java.util.Random;

/**
 * Initializes the memory with either default or random values.
 *
 * @author uiiux
 */
public class MemoryInitializer {
    private final int maxSize;
    private Random random;
    private final CyclicLinkedList<MemoryCell> memory;
    private final String defaultSymbol;

    /**
     * Constructs a MemoryInitializer with a specified maximum size, memory, and default symbol.
     *
     * @param maxSize       the maximum size of the memory.
     * @param memory        the cyclic linked list representing the memory.
     * @param defaultSymbol the default symbol to be used in memory cells.
     */
    public MemoryInitializer(int maxSize, CyclicLinkedList<MemoryCell> memory, String defaultSymbol) {
        this.maxSize = maxSize;
        this.random = new Random();
        this.memory = memory;
        this.defaultSymbol = defaultSymbol;
    }

    /**
     * Initializes the memory with default values.
     */
    public void initialInitialization() {
        for (int i = 0; i < maxSize; i++) {
            MemoryCell newCell = new MemoryCell(InstructionName.STOP, 0, 0);
            memory.add(newCell);
            newCell.setCurrentSymbol(defaultSymbol);
            newCell.setDefaultSymbol(defaultSymbol);
        }
    }

    /**
     * Reinitialized the memory with default values, replacing any existing cells.
     */
    public void initializeWithDefault() {
        for (int i = 0; i < maxSize; i++) {
            MemoryCell newCell = new MemoryCell(InstructionName.STOP, 0, 0);
            memory.replace(i, newCell);
            newCell.setCurrentSymbol(defaultSymbol);
            newCell.setDefaultSymbol(defaultSymbol);
        }
    }

    /**
     * Initializes the memory with random values for instruction names and arguments.
     *
     * @param seed the seed for the random number generator to ensure reproducibility.
     */
    public void initializeWithRandoms(long seed) {
        this.random = new Random(seed);
        int numberOfInstructions = InstructionName.values().length;
        for (int i = 0; i < maxSize; i++) {
            int instructionIndex = random.nextInt(numberOfInstructions);
            int firstArgument = random.nextInt();
            int secondArgument = random.nextInt();
            InstructionName instructionName = InstructionName.fromInt(instructionIndex);
            MemoryCell newCell = new MemoryCell(instructionName, firstArgument, secondArgument);
            memory.replace(i, newCell);
            newCell.setCurrentSymbol(defaultSymbol);
            newCell.setDefaultSymbol(defaultSymbol);
        }
    }

    /**
     * Returns the cyclic linked list representing the memory.
     *
     * @return the memory.
     */
    public CyclicLinkedList<MemoryCell> getMemory() {
        return memory;
    }
}
