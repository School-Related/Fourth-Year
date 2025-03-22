import Assignment_1_2.Assignment_1_2;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\nHello, welcome to SSCD Practicals!");
        Assignment_1_2 assignment12 = new Assignment_1_2();
        System.out.println("Finished Parsing the input file and generated Machine Code\n\n");
        assignment12.readInputFile("Assignment_1_2/input.asc");
        assignment12.run();
    }
}