package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class InfixToPostfixConverterTest {
    private final InfixToPostfixConverter converter = new InfixToPostfixConverter();

    @ParameterizedTest
    @MethodSource("validExpressionsProvider")
    void testValidExpressions(String input, List<String> expected) {
        assertEquals(expected, converter.convert(input));
    }

    @ParameterizedTest
    @MethodSource("invalidExpressionsProvider")
    void testInvalidExpressions(String input, String expectedError) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> converter.convert(input));
        assertTrue(exception.getMessage().contains(expectedError));
    }

    private static Stream<Arguments> validExpressionsProvider() {
        return Stream.of(
                Arguments.of("3 + 4", List.of("3", "4", "+")),
                Arguments.of("3 * 2 + 1", List.of("3", "2", "*", "1", "+")),
                Arguments.of("3 * -2 + 6", List.of("3", "-2", "*", "6", "+")),
                Arguments.of("5", List.of("5")),
                Arguments.of("-5 + 3", List.of("-5", "3", "+")),
                Arguments.of("10 / 3 - 4 * 2", List.of("10", "3", "/", "4", "2", "*", "-")),
                Arguments.of("1 + 2 + 3 + 4", List.of("1", "2", "+", "3", "+", "4", "+"))
        );
    }

    private static Stream<Arguments> invalidExpressionsProvider() {
        return Stream.of(
                Arguments.of("3 + a", "Invalid token: a"),
                Arguments.of("2 $ 3", "Invalid token: $"),
                Arguments.of("5.2 + 3", "Invalid token: 5.2"),
                Arguments.of("3 + 4a", "Invalid token: 4a"),
                Arguments.of("3 ++ 4", "Invalid token: +"),
                Arguments.of("3 * / 4", "Unexpected operator: /")
        );
    }

    @Test
    void testEmptyInput() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.convert(""));
    }
}
