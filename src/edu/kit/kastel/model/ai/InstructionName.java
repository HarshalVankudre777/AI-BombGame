package edu.kit.kastel.model.ai;

/**
 * Instructions names of the Instructions.
 *
 * @author uiiux
 */
public enum InstructionName {

    /**
     * Stop instruction.
     */
    STOP,

    /**
     * Move relative instruction.
     */
    MOV_R,

    /**
     * Move intermediate instruction.
     */
    MOV_I,

    /**
     * Add instruction.
     */
    ADD,

    /**
     * Add relative Instruction.
     */
    ADD_R,

    /**
     * Jump instruction.
     */
    JMP,

    /**
     * Jump if Zero instruction.
     */
    JMZ,

    /**
     * Compare instruction.
     */
    CMP,

    /**
     * Swap instruction.
     */
    SWAP;

    /**
     * Gets the index of the instruction.
     *
     * @param index index of the instruction
     * @return      index of the instruction
     */
    public static InstructionName fromInt(int index) {
        return values()[index];
    }

}
