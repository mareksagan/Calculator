package org.example;

import java.util.*;

/**
 * Evaluates postfix notation expressions with stack-based computation
 * and robust error checking.
 */
public class PostfixEvaluator {

    /**
     * Computes the result of a postfix expression
     * @param postfix Validated postfix tokens
     * @return Integer result of computation
     * @throws ArithmeticException for division by zero
     * @throws IllegalArgumentException for invalid expressions
     */
    public int evaluate(List<String> postfix) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : postfix) {
            if (token.matches("-?\\d+")) {
                // Push numbers directly to stack
                stack.push(Integer.parseInt(token));
            } else {
                // Handle operators with operand validation
                processOperator(token, stack);
            }
        }

        // Final stack validation
        if (stack.size() != 1) {
            throw new IllegalArgumentException(
                    "Invalid postfix expression - missing operators");
        }

        return stack.pop();
    }

    /** Processes operators with proper operand handling */
    private void processOperator(String token, Deque<Integer> stack) {
        // Special handling for unary minus
        if (token.equals("u-")) {
            if (stack.isEmpty()) {
                throw new IllegalArgumentException(
                        "Missing operand for unary minus");
            }
            stack.push(-stack.pop());
        } else {
            // Binary operators require two operands
            if (stack.size() < 2) {
                throw new IllegalArgumentException(
                        "Insufficient operands for operator: " + token);
            }
            int b = stack.pop();
            int a = stack.pop();
            stack.push(applyOperation(a, b, token));
        }
    }

    /** Executes arithmetic operations with safety checks */
    private int applyOperation(int a, int b, String operator) {
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b; // Order matters (a - b)
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new ArithmeticException(
                        "Division by zero");
                yield a / b; // Integer division (truncate toward zero)
            }
            default -> throw new IllegalArgumentException(
                    "Unknown operator: " + operator);
        };
    }
}
