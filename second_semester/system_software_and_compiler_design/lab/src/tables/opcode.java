package tables;

public enum OpCode {
    STOP("IS", 0),
    ADD("IS", 1),
    SUB("IS", 2),
    MULT("IS", 3),
    MOVER("IS", 4),
    MOVEM("IS", 5),
    COMB("IS", 6),
    BC("IS", 7),
    DIV("IS", 8),
    READ("IS", 9),
    PRINT("IS", 10),
    DC("DL", 2),
    DS("DL", 1),
    START("AD", 1),
    END("AD", 2),
    ORIGIN("AD", 3),
    EQU("AD", 4),
    LTORG("AD", 5),
    AREG("RG", 1),
    BREG("RG", 2),
    CREG("RG", 3);

    private final String statementClass;
    private final int machineCode;

    OpCode(String statementClass, int machineCode) {
        this.statementClass = statementClass;
        this.machineCode = machineCode;
    }

    public String getStatementClass() {
        return statementClass;
    }

    public int getMachineCode() {
        return machineCode;
    }
}
