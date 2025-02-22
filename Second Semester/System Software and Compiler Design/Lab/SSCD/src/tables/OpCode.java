package tables;

public enum OpCode {
    STOP(0),
    ADD(1),
    SUB(2),
    MULT(3),
    MOVER(4),
    MOVEM(5),
    COMP(6),
    BC(7),
    DIV(8),
    READ(9),
    PRINT(10),
    DC(1),
    DS(2),
    START(1),
    END(2),
    ORIGIN(3),
    EQU(4),
    LTORG(5);

    private final int machineCode;

    OpCode(int machineCode) {
        this.machineCode = machineCode;
    }

    public int getMachineCode() {
        return machineCode;
    }
}
