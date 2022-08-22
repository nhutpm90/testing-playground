package com.example.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.testing.service.Calculator;

@DisplayName("Test Math Operations")
class CalculatorTests {

	Calculator calculator;
	
	@BeforeAll
	static void setup() {
		System.out.println("executing @BeforeAll method");
	}
	
	@AfterAll
	static void cleanup() {
		System.out.println("executing @AfterAll method");
	}
	
	@BeforeEach
	void beforeEachTestMethod() {
		calculator = new Calculator();
		System.out.println("executing @BeforeEach method");
	}
	
	@DisplayName("Test 10 / 2")
	@Test
	void testDivide() {
		// Arrange
		int a = 10;
		int b = 5;
		int expectedResult = 2;
		
		// Act
		int actualResult = calculator.divide(a, b);
		
		// Assert
		assertEquals(expectedResult, actualResult, 
				String.format("%s / %s did not produce %s", a, b, expectedResult));
	}
	
	@DisplayName("Test 10 / 0")
	@Test
	void testDivide_WhenDividedByZero_ShouldThrowArimeticException() {
		// Arrange
		int a = 10;
		int b = 0;
		String expectedException = "/ by zero";
		
		// Act
		ArithmeticException actualException = assertThrows(ArithmeticException.class, () -> {
			calculator.divide(a, b);
		}, "Division by zero should have thrown an Arimethic exception");
		
		// Assert
		assertEquals(expectedException, actualException.getMessage(), 
				"unexpected exception message");
	}
	
	@Disabled("TODO: still need to work on it")
	@DisplayName("Test 10 + 2")
	@Test
	void testAdd() {
		fail("not implemented yet");
	}
	
	@DisplayName("Test subtraction [a, b, expectedResult]")
	@ParameterizedTest
	@MethodSource("testSubtractInputParameters")
	void testSubtract(int a, int b, int expectedResult) {
		System.out.println(String.format("running test %s - %s = %s", a, b, expectedResult));
		int actualResult = calculator.subtract(a, b);
		
		// Assert
		assertEquals(expectedResult, actualResult, 
				() -> a + " - " + b + " did not produce " + expectedResult);
	}
	
	static Stream<Arguments> testSubtractInputParameters() {
		return Stream.of(
				Arguments.of(10, 1, 9),
				Arguments.of(20, 5, 15)
		);
	}
}


