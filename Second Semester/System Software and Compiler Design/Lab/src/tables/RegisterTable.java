package tables;

public enum RegisterTable {
    AREG(1),
    BREG(2),
    CREG(3),
    DREG(4);

    private final int machineCode;

    RegisterTable(int machineCode) {
        this.machineCode = machineCode;
    }
}
