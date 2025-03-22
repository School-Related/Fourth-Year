package tables;

public class Symbol implements ICFourthColumn {
    private final String symbolName;
    private final Integer address;
    private final Integer length;
    private final Integer index;
    private final SymbolType type = SymbolType.SYMBOL;

    public Symbol(String symbolName, Integer address, Integer length, Integer index) {
        this.symbolName = symbolName;
        this.address = address;
        this.length = length;
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getSymbolName() {
        return symbolName;
    }

    public Integer getAddress() {
        return address;
    }

    public Integer getLength() {
        return length;
    }

    public SymbolType getType() {
        return type;
    }

    public Integer getValue() {
        return index;
    }
}
