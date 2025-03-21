package Assignment1;

import tables.ICRow;
import tables.OpCode;
import tables.SymbolTable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Assignment1 {
    public List<List<String>> parsedCode = new ArrayList<>();
    private int symbolTableIndex = 1;
    private int locationCounter = 0;
    SymbolTable symTable = new SymbolTable();
    List<ICRow> intermediateCode = new ArrayList<>();

    public OpCode getOpCode(String opcode){
        for (OpCode o : OpCode.values()){
            if (o.name().equalsIgnoreCase(opcode)){
                return o;
            }
        }
        return null;
    }


    public void readInputFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            if (inputStream == null) {
                System.out.println("File not found: " + fileName);
                return;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> currentLine = Arrays.asList(line.trim().split("[ ,]+"));
                parsedCode.add(currentLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void run() {
        System.out.println("Executing Assignment 1");

        for (int i = 0; i < parsedCode.size(); i++) {
            // you have to start with the start thing.

            if (i == 0) {
                if (Objects.equals(parsedCode.get(i).get(0), "START")) {
                    System.out.println("You have to start with start");
                    return;
                }
                // assign location counter
                locationCounter = Integer.parseInt(parsedCode.get(i).get(1));
            }
            // check if the first word is part of our opcode
            int finalI = i;
            if (Arrays.stream(OpCode.values()).anyMatch
                    (c -> c.name().equalsIgnoreCase(parsedCode.get(finalI).get(0)))) {
                OpCode currentOpCode = getOpCode(parsedCode.get(finalI).get(1));
                // if ds is there, increase location counter by more
                if (parsedCode.get(finalI).get(1).equalsIgnoreCase(OpCode.DS.name())) {
                    intermediateCode.add(ICRow(locationCounter + finalI, get));
                } else {
                    intermediateCode.add(ICRow(locationCounter++));
                }
            }


        }


        // Simulated First Pass: Add labels and variables to the symbolName table
        symTable.addSymbol("START", 1000, 1);
        symTable.addSymbol("f", 2000, 1);


        // Display the symbolName table
        symTable.display();

    }
}