package org.example;

import java.util.*;

/**
 * Main calculator class that coordinates conversion and evaluation
 */
public class ExpressionCalculator {
    private final InfixToPostfixConverter converter;
    private final PostfixEvaluator evaluator;

    public ExpressionCalculator() {
        this.converter = new InfixToPostfixConverter();
        this.evaluator = new PostfixEvaluator();
    }

    /**
     * Method for evaluating expressions
     * @param expression Space-separated infix expression
     * @return Calculation result
     * @throws IllegalArgumentException for invalid expressions
     * @throws ArithmeticException for division by zero
     */
    public int calculate(String expression) {
        // Convert to postfix first, then evaluate
        List<String> postfix = converter.convert(expression);
        return evaluator.evaluate(postfix);
    }
}