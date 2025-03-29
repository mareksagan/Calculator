package org.example;

import java.util.Scanner;

/**
 * Handles user interaction with enhanced error handling and features
 */
public class CalculatorApp {
    private static final String HELP_TEXT = """
        === Calculator Help ===
        Enter space-separated mathematical expressions
        Supported operators: + - * /
        Example: 3 * -2 + 6 / 3
        Commands:
          help    - Show this message
          exit    - Quit the application
          version - Show application version
        """;

    private static final String VERSION = "1.1.0";

    public static void main(String[] args) {
        ExpressionCalculator calculator = new ExpressionCalculator();
        Scanner scanner = new Scanner(System.in);

        printWelcomeMessage();

        while (true) {
            System.out.print("\ncalc> ");
            String input = scanner.nextLine().trim();

            // Handle special commands first
            if (input.equalsIgnoreCase("exit")) {
                break;
            } else if (input.equalsIgnoreCase("help")) {
                System.out.println(HELP_TEXT);
                continue; // Skip to next iteration
            } else if (input.equalsIgnoreCase("version")) {
                System.out.println("Calculator Version: " + VERSION);
                continue; // Skip to next iteration
            }

            // Process mathematical expressions
            try {
                if (validateInput(input)) continue;

                int result = calculator.calculate(input);
                System.out.println("  Result: \u001B[32m" + result + "\u001B[0m");

            } catch (Exception e) {
                displayError(e);
            }
        }

        scanner.close();
        System.out.println("\nThank you for using Calculator!");
    }

    private static void printWelcomeMessage() {
        System.out.println("\n\u001B[36m=== Java Calculator ===\u001B[0m");
        System.out.println("Type 'help' for instructions");
        System.out.println("Type 'exit' to quit\n");
    }

    private static boolean handleSpecialCommands(String input) {
        if (input.equalsIgnoreCase("exit")) {
            return true;
        }
        if (input.equalsIgnoreCase("help")) {
            System.out.println(HELP_TEXT);
            return false;
        }
        if (input.equalsIgnoreCase("version")) {
            System.out.println("Calculator Version: " + VERSION);
            return false;
        }
        return false;
    }

    private static boolean validateInput(String input) {
        if (input.isEmpty()) {
            System.out.println("\u001B[33mPlease enter an expression or command\u001B[0m");
            return true;
        }
        if (!input.matches("^[0-9+\\-*/.\\s]+$") && !input.equalsIgnoreCase("help")) {
            System.out.println("\u001B[31mError: Invalid characters in input\u001B[0m");
            return true;
        }
        return false;
    }

    private static void displayError(Exception e) {
        String message = e.getMessage();
        String errorType = "Error";

        if (e instanceof IllegalArgumentException) {
            if (message.startsWith("Invalid token")) {
                String token = message.substring(message.lastIndexOf(" ") + 1);
                System.out.println("\u001B[31m" + errorType + ": Invalid character '" +
                        token + "' in expression\u001B[0m");
            } else if (message.contains("Insufficient operands")) {
                String operator = message.substring(message.lastIndexOf(" ") + 1);
                System.out.println("\u001B[31m" + errorType +
                        ": Not enough numbers for operator '" + operator + "'\u001B[0m");
            } else {
                System.out.println("\u001B[31m" + errorType + ": " + message + "\u001B[0m");
            }
        } else if (e instanceof ArithmeticException) {
            System.out.println("\u001B[31mMath Error: " + message + "\u001B[0m");
        } else {
            System.out.println("\u001B[31mUnexpected Error: " + message + "\u001B[0m");
            e.printStackTrace();
        }

        System.out.println("Need help? Type 'help' for instructions");
    }
}
