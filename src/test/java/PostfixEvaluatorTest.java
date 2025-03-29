package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class PostfixEvaluatorTest {
    private final PostfixEvaluator evaluator = new PostfixEvaluator();

    @ParameterizedTest
    @MethodSource("validPostfixProvider")
    void testValidPostfix(List<String> postfix, int expected) {
        assertEquals(expected, evaluator.evaluate(postfix));
    }

    @ParameterizedTest
    @MethodSource("invalidPostfixProvider")
    void testInvalidPostfix(List<String> postfix, Class<? extends Exception> exceptionType, String errorMessage) {
        Exception exception = assertThrows(exceptionType,
                () -> evaluator.evaluate(postfix));
        assertTrue(exception.getMessage().contains(errorMessage));
    }

    private static Stream<Arguments> validPostfixProvider() {
        return Stream.of(
                Arguments.of(List.of("3", "4", "+"), 7),
                Arguments.of(List.of("5", "3", "-"), 2),
                Arguments.of(List.of("2", "3", "*"), 6),
                Arguments.of(List.of("6", "2", "/"), 3),
                Arguments.of(List.of("-3", "2", "*"), -6),
                Arguments.of(List.of("5", "2", "*", "1", "+"), 11),
                Arguments.of(List.of("3", "4", "2", "*", "+"), 11),  // 3 + (4*2)
                Arguments.of(List.of("2147483647"), Integer.MAX_VALUE),
                Arguments.of(List.of("-2147483648"), Integer.MIN_VALUE),
                Arguments.of(List.of("5", "2", "/"), 2)  // Integer division
        );
    }

    private static Stream<Arguments> invalidPostfixProvider() {
        return Stream.of(
                Arguments.of(List.of("3", "+"), IllegalArgumentException.class, "Insufficient operands"),
                Arguments.of(List.of("3", "0", "/"), ArithmeticException.class, "Division by zero"),
                Arguments.of(List.of("3", "4", "&"), IllegalArgumentException.class, "Unknown operator"),
                Arguments.of(List.of("3", "4", "+", "5"), IllegalArgumentException.class, "Invalid postfix"),
                Arguments.of(List.of(), IllegalArgumentException.class, "Invalid postfix"),
                Arguments.of(List.of("3", "4", "5", "+"), IllegalArgumentException.class, "Invalid postfix")
        );
    }

    @Test
    void testIntegerOverflow() {
        List<String> postfix = List.of("2147483647", "1", "+");
        assertEquals(Integer.MIN_VALUE, evaluator.evaluate(postfix));
    }
}
