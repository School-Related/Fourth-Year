package tables;

import java.util.*;

public class SymbolTable {
    private final Map<String, Symbol> table;

    // Constructor
    public SymbolTable() {
        table = new HashMap<>();
    }

    // Add a symbol to the table
    public void addSymbol(String symbol, int address, String type, String value) {
        table.put(symbol, new Symbol(symbol, address, type, value));
    }

    // Retrieve a symbol
    public Symbol getSymbol(String symbol) {
        return table.get(symbol);
    }

    // Check if a symbol exists
    public boolean contains(String symbol) {
        return table.containsKey(symbol);
    }

    // Print the symbol table
    public void display() {
        System.out.println("Symbol Table:");
        System.out.printf("%-10s %-10s %-10s %-10s%n", "Symbol", "Address", "Type", "Value");
        System.out.println("--------------------------------------");
        for (Symbol s : table.values()) {
            System.out.printf("%-10s %-10d %-10s %-10s%n", s.symbol(), s.address(), s.type(), s.value());
        }
    }
}