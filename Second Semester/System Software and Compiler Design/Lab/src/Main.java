import Assignment1.Assignment1;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, welcome to SSCD Practicals!");
        Assignment1 assignment1 = new Assignment1();
        assignment1.readInputFile("Assignment1/input.asc");
        assignment1.run();
    }
}