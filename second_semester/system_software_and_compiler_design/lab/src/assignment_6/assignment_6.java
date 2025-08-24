package Assignment_6;

import java.util.Scanner;

class Assignment_6 {
    private final String input;
    private int index;

    public Assignment_6(String input) {
        this.input = input.replaceAll("\\s", ""); // Remove spaces
        this.index = 0;
    }

    private char peek() {
        return (index < input.length()) ? input.charAt(index) : '\0'; // Return current character or end marker
    }

    private void match(char expected) {
        if (peek() == expected) {
            index++;
        } else {
            throw new RuntimeException("Syntax Error: Expected '" + expected + "' at position " + index);
        }
    }

    public void parse() {
        try {
            E(); // Start parsing from E
            if (index == input.length()) {
                System.out.println("Success: Input is syntactically correct.");
            } else {
                throw new RuntimeException("Syntax Error: Unexpected character '" + peek() + "' at position " + index);
            }
        } catch (RuntimeException e) {
            System.out.println("Failure: " + e.getMessage());
        }
    }

    private void E() {
        T();
        while (peek() == '+') {
            match('+');
            T();
        }
    }

    private void T() {
        F();
        while (peek() == '*') {
            match('*');
            F();
        }
    }

    private void F() {
        if (Character.isLetter(peek())) { // Match identifiers (id)
            match(peek());
        } else if (peek() == '(') { // Match parenthesis (E)
            match('(');
            E();
            match(')');
        } else {
            throw new RuntimeException("Syntax Error: Invalid token '" + peek() + "' at position " + index);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an arithmetic expression: ");
        String input = scanner.nextLine();
        scanner.close();

        Assignment_6 parser = new Assignment_6(input);
        parser.parse();
    }
}
