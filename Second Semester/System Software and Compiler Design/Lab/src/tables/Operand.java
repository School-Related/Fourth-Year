package tables;

public class Operand {
    public OpCode opCode;
    public String statementClass;
    public Integer machineCode;

    public Operand(OpCode opcode, String statementClass, Integer machineCode) {
        this.opCode = opcode;
        this.statementClass = statementClass;
        this.machineCode = machineCode;
    }
}
