package tables;

public class Symbol {
    String symbolName;
    int address;
    int length;
    int index;

    public Symbol(String symbolName, int address, int length, int index) {
        this.symbolName = symbolName;
        this.address = address;
        this.length = length;
        this.index = index;
    }
}
