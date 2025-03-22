package tables;

public class Symbol implements ICFourthColumn {
    private String symbolName;
    private Integer address;
    private Integer length;
    private final Integer index;
    private SymbolType type = SymbolType.SYMBOL;

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

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }
}
