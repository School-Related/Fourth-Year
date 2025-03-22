package tables;

public class Constants implements ICFourthColumn {
    private final Integer length;
    private final SymbolType type = SymbolType.CONSTANT;

    public Constants(Integer length) {
        this.length = length;
    }

    public Integer getValue() {
        return length;
    }

    public SymbolType getType() {
        return type;
    }
}
