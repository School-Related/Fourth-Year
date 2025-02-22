package tables;

public class Symbol {
    String symbolName;
    int address;
    String type;
    String value;

    public Symbol(String symbolName, int address, String type, String value) {
        this.symbolName = symbolName;
        this.address = address;
        this.type = type;
        this.value = value;
    }
}
