package Assignment1;

import tables.SymbolTable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Assignment1 {
    public List<List<String>> parsedCode = new ArrayList<>();

    public void readInputFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
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


    public void run(){
        System.out.println("Executing Assignment 1");


        SymbolTable symTable = new SymbolTable();

        // Simulated First Pass: Add labels and variables to the symbolName table
        symTable.addSymbol("START", 0x1000, "Label", "");
        symTable.addSymbol("VAR1", 0x1004, "Variable", "5");
        symTable.addSymbol("LOOP", 0x1008, "Label", "");
        symTable.addSymbol("COUNT", 0x100C, "Variable", "10");

        // Display the symbolName table
        symTable.display();
        
    }
}