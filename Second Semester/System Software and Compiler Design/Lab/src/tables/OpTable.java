package tables;

import java.util.Map;

public class OpTable {
    private Map<OpCode, Operand> table;

    public OpTable(OpCode opcode, String statementClass, Integer machineCode) {
        table.put(opcode, new Operand(opcode, statementClass, machineCode));
    }
}
