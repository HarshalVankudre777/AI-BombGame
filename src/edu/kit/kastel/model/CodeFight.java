package edu.kit.kastel.model;

import edu.kit.kastel.model.ai.AI;
import edu.kit.kastel.model.ai.AICommandExecutor;
import edu.kit.kastel.model.memory.MemoryCell;
import edu.kit.kastel.model.memory.MemoryInitializer;
import edu.kit.kastel.model.memory.Mode;
import edu.kit.kastel.ui.MemoryPrinter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the main control class for a code-based combat simulation between AI entities.
 *
 * @author uiiux
 */
public class CodeFight {

    private static final int DEFAULT_SYMBOL_INDEX = 0;
    private static final int BOUNDS_SYMBOL_INDEX = 1;
    private static final int CURRENT_SYMBOL_INDEX = 2;
    private static final int OTHER_SYMBOL_INDEX = 3;
    private static final int AI_HEAD_INDEX = 0;
    private final int memorySize;
    private final List<String> memorySymbols;
    private final List<String> aiSymbols;
    private final List<String> defaultSymbols;
    private final List<String> bombSymbols;
    private final CyclicLinkedList<MemoryCell> memory;
    private final MemoryPrinter memoryPrinter;
    private final MemoryInitializer memoryInitializer;
    private final CyclicLinkedList<AI> runningAI;
    private final List<AI> stoppedAIList;
    private final LinkedList<AI> playingList = new LinkedList<>();
    private final List<AI> listOfAI = new ArrayList<>();
    private Mode memoryMode;
    private AI currentAI;
    private AICommandExecutor aiCommandExecutor;
    private boolean playingPhase = false;

    /**
     * Constructs a new CodeFight simulation environment with specified memory size and symbols.
     *
     * @param memorySize    The size of the memory to be used in the simulation.
     * @param memorySymbols The symbols to represent different states in the memory.
     * @param aiSymbols     The symbols to be assigned to AI for identification.
     */
    public CodeFight(int memorySize, List<String> memorySymbols, List<String> aiSymbols) {
        this.memorySize = memorySize;
        this.memorySymbols = new ArrayList<>(memorySymbols);
        this.aiSymbols = new ArrayList<>(aiSymbols);
        this.defaultSymbols = new ArrayList<>();
        this.bombSymbols = new ArrayList<>();
        this.memory = new CyclicLinkedList<>();
        this.runningAI = new CyclicLinkedList<>();
        this.stoppedAIList = new ArrayList<>();
        this.memoryInitializer = new MemoryInitializer(memorySize, getMemory(), getMemoryDefaultSymbol());
        this.memoryMode = Mode.STOP;
        this.memoryPrinter = new MemoryPrinter(getMemory(), memorySize, memorySymbols.get(BOUNDS_SYMBOL_INDEX));
        setSymbols();
        getMemoryInitializer().initialInitialization();
    }

    /**
     * Assigns symbols to AIs based on their playing order.
     */
    public void assignSymbols() {
        for (int i = 0; i < getPlayingList().size(); i++) {
            getPlayingList().get(i).setDefaultSymbol(defaultSymbols.get(i));
            getPlayingList().get(i).setBombSymbol(bombSymbols.get(i));
        }

        for (AI ai : getPlayingList()) {
            for (MemoryCell cell : ai.getAiCommands()) {
                cell.setCurrentSymbol(ai.getDefaultSymbol());
                cell.setBombSymbol(ai.getBombSymbol());
                cell.setDefaultSymbol(ai.getDefaultSymbol());
            }
        }
    }

    /**
     * Loads AI commands into the simulation memory based on the AI list.
     */
    public void loadMemory() {
        int memoryPerAI = memorySize / getPlayingList().size();
        int baseIndex = 0;

        for (AI ai : getPlayingList()) {
            for (int i = baseIndex; i < baseIndex + ai.getAiCommands().size(); i++) {
                getMemory().replace(i, ai.getAiCommands().get(i - baseIndex));
            }
            baseIndex += memoryPerAI;
        }
    }


    /**
     * Handles the game logic, updating the current AI and setting start indexes for AIs.
     */
    public void gameHandler() {
        currentAI = runningAI.get(AI_HEAD_INDEX);
        int memoryPerAI = memorySize / runningAI.size();
        for (int i = 0; i < runningAI.size(); i++) {
            runningAI.get(i).setStartIndex(i * memoryPerAI);
            if (runningAI.get(i).equals(currentAI)) {
                memory.get(i * memoryPerAI).setCurrentSymbol(getCurrentSymbol());
            } else {
                memory.get(i * memoryPerAI).setCurrentSymbol(getOtherSymbol());
            }
        }
    }

    /**
     * Creates an AI command executor for the simulation.
     */
    public void createAICommandExecutor() {
        aiCommandExecutor = new AICommandExecutor(getMemory(), stoppedAIList);
    }

    /**
     * Returns the list of AI symbols.
     *
     * @return A list of AI symbols.
     */
    public List<String> getAiSymbols() {
        return aiSymbols;
    }

    /**
     * Returns the size of the memory used in the simulation.
     *
     * @return The memory size.
     */
    public int getMemorySize() {
        return memorySize;
    }

    /**
     * Returns the current memory mode of the simulation.
     *
     * @return The current memory mode.
     */
    public Mode getMemoryMode() {
        return memoryMode;
    }

    /**
     * Sets the memory mode of the simulation.
     *
     * @param memoryMode The new memory mode.
     */
    public void setMemoryMode(Mode memoryMode) {
        this.memoryMode = memoryMode;
    }

    /**
     * Returns the list of currently running AIs in the simulation.
     *
     * @return A cyclic linked list of running AIs.
     */
    public CyclicLinkedList<AI> getRunningAI() {
        return runningAI;
    }

    /**
     * Returns the current AI being processed in the simulation.
     *
     * @return The current AI.
     */
    public AI getCurrentAI() {
        return currentAI;
    }

    /**
     * Updates the current AI to the next AI in the cycle.
     */
    public void updateAI() {
        currentAI = runningAI.getNext(currentAI);
    }

    /**
     * Returns the AI command executor.
     *
     * @return The AI command executor.
     */
    public AICommandExecutor getAiCommandExecutor() {
        return aiCommandExecutor;
    }

    /**
     * Returns the default symbol for the memory.
     *
     * @return The default memory symbol.
     */
    public String getMemoryDefaultSymbol() {
        return memorySymbols.get(DEFAULT_SYMBOL_INDEX);
    }

    /**
     * Returns the list of AIs that have been stopped.
     *
     * @return A list of stopped AIs.
     */
    public List<AI> getStoppedAIList() {
        return stoppedAIList;
    }

    /**
     * Sets the default and bomb symbols for the AIs based on the aiSymbols list.
     */
    private void setSymbols() {
        for (int i = 0; i < aiSymbols.size() - 1; i += 2) {
            defaultSymbols.add(aiSymbols.get(i));
            bombSymbols.add(aiSymbols.get(i + 1));
        }
    }


    /**
     * Sets if the game is in playing phase or not.
     *
     * @param playingPhase boolean if the game is in the playing phase
     */
    public void setPlayingPhase(boolean playingPhase) {
        this.playingPhase = playingPhase;
    }
    /**
     * Returns the cyclic linked list representing the simulation memory.
     *
     * @return The memory as a cyclic linked list of MemoryCell objects.
     */
    public CyclicLinkedList<MemoryCell> getMemory() {
        return memory;
    }

    /**
     * Returns the memory printer associated with this simulation.
     *
     * @return The MemoryPrinter instance used for displaying memory state.
     */
    public MemoryPrinter getMemoryPrinter() {
        return memoryPrinter;
    }

    /**
     * Returns the memory initializer for the simulation.
     *
     * @return The MemoryInitializer used to initialize and reset the simulation memory.
     */
    public MemoryInitializer getMemoryInitializer() {
        return memoryInitializer;
    }

    /**
     * Returns the list of AIs currently playing in the simulation.
     *
     * @return A LinkedList of AI objects that are part of the current game.
     */
    public LinkedList<AI> getPlayingList() {
        return playingList;
    }

    /**
     * Returns the list of all AIs that have been added to the simulation.
     *
     * @return A List of AI objects added to the simulation.
     */
    public List<AI> getListOfAI() {
        return listOfAI;
    }

    /**
     * Indicates whether the simulation is currently in the playing phase.
     *
     * @return True if the simulation is in the playing phase, false otherwise.
     */
    public boolean isPlayingPhase() {
        return playingPhase;
    }

    /**
     * Gets the Symbol used to show the next cell of Current AI.
     *
     * @return Symbol for current AI
     */
    public String getCurrentSymbol() {
        return memorySymbols.get(CURRENT_SYMBOL_INDEX);
    }

    /**
     * Gets tje Symbol used to show the next cell of Other AIs.
     *
     * @return Symbol of Other AIs
     */
    public String getOtherSymbol() {
        return memorySymbols.get(OTHER_SYMBOL_INDEX);

    }

}

