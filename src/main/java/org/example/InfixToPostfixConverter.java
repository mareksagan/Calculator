package org.example;

import java.util.*;

/**
 * Converts infix mathematical expressions to postfix notation (Reverse Polish Notation)
 * using the Shunting-yard algorithm.
 */
public class InfixToPostfixConverter {

    /**
     * Converts a space-separated infix expression to postfix notation
     * @param expression Input expression (e.g., "3 + 4 * 2")
     * @return List of tokens in postfix order
     * @throws IllegalArgumentException for invalid tokens
     */
    public List<String> convert(String expression) {
        // Handle empty input first
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        // Split and filter empty tokens
        String[] tokens = Arrays.stream(expression.split("\\s+"))
                .filter(token -> !token.isEmpty())
                .toArray(String[]::new);

        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        boolean expectOperand = true;

        for (String token : tokens) {
            if (isNumber(token)) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Unexpected number after token: " + token);
                }
                output.add(token);
                expectOperand = false;
            } else if (isOperator(token)) {
                // Handle consecutive operators
                if (expectOperand && !token.equals("-")) {
                    throw new IllegalArgumentException("Unexpected operator: " + token);
                }

                // Handle valid operator placement
                while (!stack.isEmpty() &&
                        isOperator(stack.peek()) &&
                        precedence(stack.peek()) >= precedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
                expectOperand = true;
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    /**
     * Checks if token is a positive/negative integer
     */
    private boolean isNumber(String token) {
        return token.matches("-?\\d+");
    }

    /**
     * Checks if token is a supported operator
     */
    private boolean isOperator(String token) {
        return "+-*/".contains(token) && token.length() == 1;
    }

    /**
     * Returns operator precedence (PEMDAS rules)
     * * and / have higher precedence than + and -
     */
    private int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}