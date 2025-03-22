package Assignment_1_2;

import tables.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Assignment_1_2 {
    public List<List<String>> parsedCode = new ArrayList<>();
    private int symbolTableIndex = 1;
    private int locationCounter = 0;
    Map<String, Symbol> symbolTable = new HashMap<>();
    List<ICRow> intermediateCode = new ArrayList<>();

    public void displaySymbolTable(Map<String, Symbol> symbolTable) {
        System.out.println("Symbol Table:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-10s%n", "Index", "Symbol", "Address", "Length");
        System.out.println("-------------------------------------------------------");
        for (Symbol s : symbolTable.values().stream().sorted(
                Comparator.comparing(Symbol::getIndex)).toList()) {
            System.out.printf("%-10d %-10s %-10d %-10s%n", s.getIndex(), s.getSymbolName(), s.getAddress(), s.getLength());
        }
        System.out.println("-------------------------------------------------------\n\n");
    }

    public void displayICTable(List<ICRow> intermediateCode) {
        System.out.println("Intermediate Code Table:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-10s %-10s %-10s %-10s%n", "Location", "OpCode", "Operand1", "Operand2");
        System.out.println("-------------------------------------------------------");
        for (ICRow ic : intermediateCode) {
            System.out.printf("%-10s %-10s %-10s %-10s%n", ic.getLocationCounter().isPresent() ?
                            ic.getLocationCounter().get() : "", ic.getOpCode().getStatementClass() + " " + ic.getOpCode().getMachineCode(), ic.getOperand1() == null ? "" : ic.getOperand1().getMachineCode(),
                    ic.getSymbolOrConstant().isPresent() ? ic.getSymbolOrConstant().get().getType() + " " + ic.getSymbolOrConstant().get().getValue() : "");
        }
        System.out.println("-------------------------------------------------------\n\n");
    }

    public void displayMachineCode(List<ICRow> intermediateCode, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Machine Code:\n");
            writer.write("-------------------------------------------------------\n");
            writer.write(String.format("%-10s %-10s %-10s %-10s%n", "Location", "OpCode", "Operand1", "Operand2"));
            writer.write("-------------------------------------------------------\n");

            for (ICRow ic : intermediateCode) {
                writer.write(String.format("%-10s %-10s %-10s %-10s%n",
                        ic.getLocationCounter().isPresent() ? ic.getLocationCounter().get() : "",
                        ic.getOpCode().getMachineCode(),
                        ic.getOperand1() == null ? "" : ic.getOperand1().getMachineCode(),
                        ic.getSymbolOrConstant().isPresent() &&
                                ic.getSymbolOrConstant().get().getType() == SymbolType.SYMBOL ?
                                ((ic.getSymbolOrConstant().get() instanceof Symbol symbol) ?
                                        (symbol.getAddress()) : "") : ""));
            }

            writer.write("-------------------------------------------------------\n\n");
            System.out.println("Machine code successfully written to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public OpCode getOpCode(String opcode) {
        for (OpCode o : OpCode.values()) {
            if (o.name().equalsIgnoreCase(opcode)) {
                return o;
            }
        }
        return null;
    }

    public void readInputFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> currentLine = Arrays.asList(line.trim().split("[ ,]+"));
                    parsedCode.add(currentLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        for (List<String> currentLine : parsedCode) {
            // check if the first word is part of our opcode else it's a symbol
            if (Arrays.stream(OpCode.values()).anyMatch
                    (c -> c.name().equalsIgnoreCase(currentLine.getFirst()))) {
                OpCode currentOpCode = getOpCode(currentLine.getFirst());
                Symbol currentSymbol;

                // check for start
                if (currentOpCode.name().equalsIgnoreCase(OpCode.START.name())) {
                    locationCounter = Integer.parseInt(currentLine.get(1));
                    intermediateCode.add(new ICRow(null, OpCode.START, null,
                            new Constants(100)));
                    continue;
                }

                // check for origin
                if (currentOpCode.name().equalsIgnoreCase(OpCode.ORIGIN.name())) {
                    locationCounter = symbolTable.get(currentLine.get(1)).getAddress();
                    intermediateCode.add(new ICRow(null, currentOpCode, null,
                            null));
                    continue;
                }

                // check for end
                if (currentOpCode.name().equalsIgnoreCase(OpCode.END.name())) {
                    intermediateCode.add(new ICRow(null, currentOpCode, null,
                            null));
                    continue;
                }

                // any other instruction
                // check if symbol is present
                if (symbolTable.containsKey(currentLine.get(2))) {
                    currentSymbol = symbolTable.get(currentLine.get(2));
                } else {
                    currentSymbol = new Symbol(currentLine.get(2), null, 1, symbolTableIndex++);
                    symbolTable.put(currentLine.get(2), currentSymbol);
                }
                intermediateCode.add(new ICRow(locationCounter, currentOpCode,
                        getOpCode(currentLine.get(1)),
                        currentSymbol));
                locationCounter++;
            } else {
                Symbol currentSymbol;
                // check if symbol is present if not create.
                if (!symbolTable.containsKey(currentLine.getFirst())) {
                    currentSymbol = new Symbol(currentLine.getFirst(), locationCounter, 1, symbolTableIndex++);
                    symbolTable.put(currentLine.getFirst(), currentSymbol);
                } else {
                    currentSymbol = symbolTable.get(currentLine.getFirst());
                    currentSymbol.setAddress(locationCounter);
                }

                OpCode currentOpCode = getOpCode(currentLine.get(1));

                // check for ds
                if (currentOpCode.name().equalsIgnoreCase(OpCode.DS.name())) {
                    intermediateCode.add(new ICRow(locationCounter, currentOpCode, null,
                            new Constants(Integer.parseInt(currentLine.get(2)))));
                    locationCounter += Integer.parseInt(currentLine.get(2));
                    symbolTable.get(currentSymbol.getSymbolName()).setLength(Integer.parseInt(currentLine.get(2)));
                    continue;
                }
                // check for dc
                if (currentOpCode.name().equalsIgnoreCase(OpCode.DC.name())) {
                    intermediateCode.add(new ICRow(locationCounter, currentOpCode, null,
                            new Constants(Integer.parseInt(currentLine.get(2)))));
                    locationCounter++;
                    continue;
                }
                // normal instruction
                intermediateCode.add(new ICRow(
                        locationCounter,
                        currentOpCode,
                        getOpCode(currentLine.get(2)),
                        symbolTable.get(currentLine.get(3))));
                locationCounter++;
            }
        }

        // Display the symbolName table
        displaySymbolTable(symbolTable);
        displayICTable(intermediateCode);
        displayMachineCode(intermediateCode, "src/Assignment_1_2/output.txt");
    }
}