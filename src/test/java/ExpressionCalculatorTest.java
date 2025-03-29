package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionCalculatorTest {
    private final ExpressionCalculator calculator = new ExpressionCalculator();

    @ParameterizedTest
    @MethodSource("validExpressionsProvider")
    void testValidExpressions(String input, int expected) {
        assertEquals(expected, calculator.calculate(input));
    }

    @ParameterizedTest
    @MethodSource("invalidExpressionsProvider")
    void testInvalidExpressions(String input, Class<? extends Exception> exceptionType, String errorMessage) {
        Exception exception = assertThrows(exceptionType,
                () -> calculator.calculate(input));
        assertTrue(exception.getMessage().contains(errorMessage));
    }

    private static Stream<Arguments> validExpressionsProvider() {
        return Stream.of(
                Arguments.of("2 + 3", 5),
                Arguments.of("3 * 2 + 1", 7),
                Arguments.of("  3 *  -2  + 6  ", 0),
                Arguments.of("10 - 2 * 3 + 8 / 4", 6),
                Arguments.of("-2147483648", Integer.MIN_VALUE),
                Arguments.of(" 12  /  3  -  4 ", 0),
                Arguments.of("5 + 0 * 100", 5),
                Arguments.of("0 / 5", 0),
                Arguments.of("  -5  ", -5)
        );
    }

    private static Stream<Arguments> invalidExpressionsProvider() {
        return Stream.of(
                Arguments.of("4 / 0", ArithmeticException.class, "Division by zero"),
                Arguments.of("2 + * 3", IllegalArgumentException.class, "Unexpected operator: *"),
                Arguments.of("3 + 4a", IllegalArgumentException.class, "Invalid token"),
                Arguments.of("3 ++ 4", IllegalArgumentException.class, "Invalid token"),
                Arguments.of("", IllegalArgumentException.class, "Empty expression"),
                Arguments.of("3 3 +", IllegalArgumentException.class, "Unexpected number after token: 3"),
                Arguments.of("3 +", IllegalArgumentException.class, "Insufficient operands")
        );
    }

    @Test
    void testBoundaryValues() {
        // Test integer overflow/underflow behavior
        assertEquals(Integer.MIN_VALUE, calculator.calculate("2147483647 + 1"));
        assertEquals(Integer.MAX_VALUE, calculator.calculate("-2147483648 - 1"));
    }

    @Test
    void testComplexOperatorCombinations() {
        assertEquals(6, calculator.calculate("10 - 3 * 2 + 8 / 4"));  // 10-6+2
        assertEquals(-8, calculator.calculate("2 + 3 * -4 + 6 / 3")); // 2-12+2
    }
}
