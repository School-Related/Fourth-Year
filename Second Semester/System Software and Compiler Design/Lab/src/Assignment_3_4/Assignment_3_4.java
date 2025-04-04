package Assignment_3_4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class MNT {
    int index;
    String macroName;
    int mdtIndex;

    public MNT(int index, String macroName, int mdtIndex) {
        this.index = index;
        this.macroName = macroName;
        this.mdtIndex = mdtIndex;
    }

    @Override
    public String toString() {
        return index + " " + macroName + " " + mdtIndex;
    }
}

class MDT {
    int index;
    String instruction;

    public MDT(int index, String instruction) {
        this.index = index;
        this.instruction = instruction;
    }

    @Override
    public String toString() {
        return index + " " + instruction;
    }
}

class ALA {
    int index;
    String argument;

    public ALA(int index, String argument) {
        this.index = index;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return index + " " + argument;
    }
}

public class Assignment_3_4 {
    private List<List<String>> parsedCode = new ArrayList<>();
    private Map<Integer, MNT> mntTable = new HashMap<>();
    private Map<Integer, MDT> mdtTable = new HashMap<>();
    private Map<Integer, ALA> alaTable = new HashMap<>();

    private int mntCounter = 1, mdtCounter = 1, alaCounter = 1;

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

    public void pass1() {
        boolean isMacroDefinition = false;

        for (List<String> currentLine : parsedCode) {
            if (currentLine.get(0).equals("MACRO")) {
                isMacroDefinition = true;
                continue;
            }

            if (isMacroDefinition) {
                if (currentLine.get(0).equals("MEND")) {
                    isMacroDefinition = false;
                    mdtTable.put(mdtCounter++, new MDT(mdtCounter - 1, "MEND"));
                    continue;
                }

                if (!mntTable.containsKey(mntCounter)) {
                    // Add entry to MNT table
                    mntTable.put(mntCounter++, new MNT(mntCounter - 1, currentLine.get(0), mdtCounter));

                    // Add arguments to ALA table
                    for (int i = 1; i < currentLine.size(); i++) {
                        alaTable.put(alaCounter++, new ALA(alaCounter - 1, currentLine.get(i)));
                    }
                }

                // Add instructions to MDT table
                StringBuilder instructionBuilder = new StringBuilder();
                for (String token : currentLine) {
                    if (token.startsWith("&")) { // Replace dummy arguments with ALA indices
                        for (Map.Entry<Integer, ALA> entry : alaTable.entrySet()) {
                            if (entry.getValue().argument.equals(token)) {
                                instructionBuilder.append("#").append(entry.getKey()).append(" ");
                            }
                        }
                    } else {
                        instructionBuilder.append(token).append(" ");
                    }
                }
                mdtTable.put(mdtCounter++, new MDT(mdtCounter - 1, instructionBuilder.toString().trim()));
            }
        }
    }

    public void displayTables() {
        System.out.println("\nMNT Table:");
        for (MNT entry : mntTable.values()) {
            System.out.println(entry);
        }

        System.out.println("\nMDT Table:");
        for (MDT entry : mdtTable.values()) {
            System.out.println(entry);
        }

        System.out.println("\nALA Table:");
        for (ALA entry : alaTable.values()) {
            System.out.println(entry);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n\nHello, welcome to SSCD Practicals!");

        Assignment_3_4 assignment34 = new Assignment_3_4();

        System.out.println("Finished Parsing the input file and generated Machine Code\n\n");

        assignment34.readInputFile("Assignment_3_4/input.asc");

        assignment34.pass1(); // Perform Pass 1

        assignment34.displayTables(); // Display the generated tables
    }
}
