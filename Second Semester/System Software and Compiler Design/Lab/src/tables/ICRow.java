package tables;

import java.util.Optional;

public class ICRow {
    private final Integer locationCounter;
    private final OpCode opCode;
    private final OpCode operand1;
    private final ICFourthColumn symbolOrConstant; // Polymorphism applied here

    public ICRow(Integer locationCounter, OpCode opCode, OpCode operand1, ICFourthColumn symbolOrConstant) {
        this.locationCounter = locationCounter;
        this.opCode = opCode;
        this.operand1 = operand1;
        this.symbolOrConstant = symbolOrConstant;
    }

    public void printICRow() {
        System.out.println("Location Counter: " + locationCounter);
        System.out.println("OpCode: " + opCode);
        System.out.println("Operand1: " + operand1);
        System.out.println("Symbol/Constant: " + symbolOrConstant.getType() + " " + symbolOrConstant.getValue());
    }

    public Optional<Integer> getLocationCounter() {
        return Optional.ofNullable(locationCounter);
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public OpCode getOperand1() {
        return operand1;
    }

    public Optional<ICFourthColumn> getSymbolOrConstant() {
        return Optional.ofNullable(symbolOrConstant);
    }
}
