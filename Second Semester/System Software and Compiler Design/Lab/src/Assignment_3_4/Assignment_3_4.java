package Assignment_3_4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class MNT {
    int index;
    String macroName;
    int mdtIndex; // Store internally but don't display in toString

    public MNT(int index, String macroName, int mdtIndex) {
        this.index = index;
        this.macroName = macroName;
        this.mdtIndex = mdtIndex;
    }

    @Override
    public String toString() {
        return index + " " + macroName; // Only display index and name
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
        return "#" + index + " " + argument;
    }
}

public class Assignment_3_4 {
    private List<List<String>> parsedCode = new ArrayList<>();
    private Map<Integer, MNT> mntTable = new HashMap<>();
    private Map<Integer, MDT> mdtTable = new HashMap<>();
    private Map<Integer, ALA> alaTable = new HashMap<>();
    private List<String> alpWithoutMacroDefinition = new ArrayList<>();

    private int mntCounter = 1, mdtCounter = 1, alaCounter = 1;
    private Map<String, Integer> macroNameToMntIndex = new HashMap<>();
    private Map<String, Integer> formalParamToAlaIndex = new HashMap<>();

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

        for (int i = 0; i < parsedCode.size(); i++) {
            List<String> currentLine = parsedCode.get(i);

            if (currentLine.isEmpty()) continue;

            String firstToken = currentLine.get(0);

            if (firstToken.equals("MACRO")) {
                isMacroDefinition = true;
                continue;
            }

            if (firstToken.equals("MEND")) {
                isMacroDefinition = false;
                mdtTable.put(mdtCounter++, new MDT(mdtCounter - 1, "MEND"));
                continue;
            }

            if (isMacroDefinition) {
                if (!macroNameToMntIndex.containsKey(firstToken)) {
                    // This is a macro name definition
                    mntTable.put(mntCounter, new MNT(mntCounter, firstToken, mdtCounter));
                    macroNameToMntIndex.put(firstToken, mntCounter);
                    mntCounter++;

                    // Clear the parameter mapping for this new macro
                    formalParamToAlaIndex.clear();

                    // Add formal parameters to ALA (only those starting with &)
                    for (int j = 1; j < currentLine.size(); j++) {
                        String param = currentLine.get(j);
                        if (param.startsWith("&")) {
                            alaTable.put(alaCounter, new ALA(alaCounter, param));
                            formalParamToAlaIndex.put(param, alaCounter);
                            alaCounter++;
                        }
                    }
                }

                // Add to MDT
                StringBuilder mdtLine = new StringBuilder();
                for (int j = 0; j < currentLine.size(); j++) {
                    String token = currentLine.get(j);
                    if (j > 0 && token.startsWith("&")) {
                        // Replace formal parameter with ALA index
                        Integer alaIndex = formalParamToAlaIndex.get(token);
                        if (alaIndex != null) {
                            mdtLine.append("#").append(alaIndex).append(" ");
                        } else {
                            mdtLine.append(token).append(" ");
                        }
                    } else {
                        mdtLine.append(token).append(" ");
                    }
                }
                mdtTable.put(mdtCounter++, new MDT(mdtCounter - 1, mdtLine.toString().trim()));
            } else {
                // Not in macro definition, add to ALP without macro definition
                StringBuilder alpLine = new StringBuilder();
                for (String token : currentLine) {
                    alpLine.append(token).append(" ");
                }
                alpWithoutMacroDefinition.add(alpLine.toString().trim());
            }
        }
    }

    public void pass2() {
        List<String> expandedCode = new ArrayList<>();

        for (String line : alpWithoutMacroDefinition) {
            String[] tokens = line.split("\\s+");

            if (tokens.length == 0) {
                expandedCode.add("");
                continue;
            }

            String firstToken = tokens[0];

            // Check if it's a macro call
            if (macroNameToMntIndex.containsKey(firstToken)) {
                // Get the MNT entry for this macro
                int mntIndex = macroNameToMntIndex.get(firstToken);
                MNT mntEntry = mntTable.get(mntIndex);

                // Get the starting MDT index for this macro
                int mdtIndex = mntEntry.mdtIndex;

                // Create mapping from formal parameters (in ALA) to actual arguments
                Map<Integer, String> alaIndexToActualArg = new HashMap<>();
                for (int i = 1; i < tokens.length && i <= alaTable.size(); i++) {
                    alaIndexToActualArg.put(i, tokens[i]);
                }

                // Process the MDT entries for this macro
                boolean isFirstLine = true;
                int currentMdtIndex = mdtIndex;
                while (true) {
                    MDT mdtEntry = mdtTable.get(currentMdtIndex);

                    if (mdtEntry == null || mdtEntry.instruction.equals("MEND")) {
                        break;
                    }

                    if (isFirstLine) {
                        // Skip the macro name line
                        isFirstLine = false;
                        currentMdtIndex++;
                        continue;
                    }

                    // Replace #n with actual arguments
                    String expandedInstruction = mdtEntry.instruction;
                    for (Map.Entry<Integer, String> entry : alaIndexToActualArg.entrySet()) {
                        expandedInstruction = expandedInstruction.replace("#" + entry.getKey(), entry.getValue());
                    }

                    expandedCode.add(expandedInstruction);
                    currentMdtIndex++;
                }
            } else {
                // Not a macro call, add as-is
                expandedCode.add(line);
            }
        }

        // Write expanded code to output file
        try (FileWriter writer = new FileWriter("src/Assignment_3_4/output.txt")) {
            for (String line : expandedCode) {
                writer.write(line + "\n");
            }
            System.out.println("Successfully wrote expanded code to output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayTables() {
        System.out.println("\nMNT Table:");
        System.out.println("MNT Index\tMacro Name");
        for (MNT entry : mntTable.values()) {
            System.out.println(entry);
        }

        System.out.println("\nMDT Table:");
        System.out.println("MDT Index\tInstruction");
        for (MDT entry : mdtTable.values()) {
            System.out.println(entry);
        }

        System.out.println("\nALA Table:");
        System.out.println("ALA Index\tDummy Arguments");
        for (ALA entry : alaTable.values()) {
            System.out.println(entry);
        }

        System.out.println("\nALP without Macro Definition but with Macro Call:");
        for (String line : alpWithoutMacroDefinition) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n\nHello, welcome to SSCD Practicals!");

        Assignment_3_4 assignment34 = new Assignment_3_4();

        System.out.println("Parsing the input file...");
        assignment34.readInputFile("Assignment_3_4/input.asc");

        System.out.println("Performing Pass 1...");
        assignment34.pass1(); // Perform Pass 1

        System.out.println("Displaying tables after Pass 1...");
        assignment34.displayTables(); // Display the generated tables

        System.out.println("Performing Pass 2...");
        assignment34.pass2(); // Perform Pass 2

        System.out.println("Generated expanded code in output.txt");
    }
}
