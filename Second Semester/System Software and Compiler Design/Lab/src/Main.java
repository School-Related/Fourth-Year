import Assignment1.Assignment1;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, welcome to SSCD Practicals!");

        Assignment1 assignment1 = new Assignment1();
        assignment1.readInputFile("Assignment1/input.asc");
        System.out.println("Parsed Assembly Code:");
        for (List<String> line : assignment1.parsedCode) {
            System.out.println(line); // Prints the list format of each line
        }
        assignment1.run();
    }
}