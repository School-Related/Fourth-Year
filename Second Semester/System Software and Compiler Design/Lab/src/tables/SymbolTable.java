package tables;

import java.util.*;

public class SymbolTable {
    private final Map<String, Symbol> table;
    private int index = 1;
    // Constructor
    public SymbolTable() {
        table = new HashMap<>();
    }

    // Add a symbolName to the table
    public void addSymbol(String symbolName, int address, Integer length) {
        table.put(symbolName, new Symbol(symbolName, address, length, index++));
    }

    // Retrieve a symbolName
    public Symbol getSymbol(String symbol) {
        return table.get(symbol);
    }

    // Check if a symbolName exists
    public boolean contains(String symbol) {
        return table.containsKey(symbol);
    }

    // Print the symbolName table
    public void display() {
        System.out.println(table.values());
        System.out.println("Symbol Table:");
        System.out.printf("%-10s %-10s %-10s %-10s%n", "Index", "Symbol", "Address", "Length");
        System.out.println("--------------------------------------");
        for (Symbol s : table.values()) {
            System.out.printf("%-10d %-10s %-10d %-10s%n", s.index, s.symbolName, s.address, s.length);
        }
    }
}