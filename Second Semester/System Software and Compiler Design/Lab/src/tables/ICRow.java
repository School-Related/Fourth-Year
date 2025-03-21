package tables;

public class ICRow {
    int locationCounter;
    Operand opCode;
    Operand operand1;
    Operand operand2;

    ICRow(int locationCounter, Operand opCode, Operand operand1, Operand operand2) {
        this.locationCounter = locationCounter;
        this.opCode = opCode;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }
}
