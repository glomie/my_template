package com.temp.alg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Solves the 24-point game using each of four input numbers exactly once.
 */
public class TwentyFourAlg {

    private static final Fraction TARGET = new Fraction(24, 1);

    /**
     * Returns every distinct expression that evaluates to 24.
     *
     * @param numbers four integers used exactly once
     * @return solutions in deterministic discovery order
     */
    public static List<String> solve(int... numbers) {
        validate(numbers);

        List<Operand> operands = new ArrayList<>(numbers.length);
        for (int number : numbers) {
            operands.add(new Operand(new Fraction(number, 1), String.valueOf(number)));
        }

        Set<String> solutions = new LinkedHashSet<>();
        search(operands, solutions);
        return new ArrayList<>(solutions);
    }

    public static boolean canSolve(int... numbers) {
        return !solve(numbers).isEmpty();
    }

    private static void search(List<Operand> operands, Set<String> solutions) {
        if (operands.size() == 1) {
            Operand result = operands.get(0);
            if (TARGET.equals(result.value)) {
                solutions.add(removeOuterParentheses(result.expression));
            }
            return;
        }

        for (int i = 0; i < operands.size() - 1; i++) {
            for (int j = i + 1; j < operands.size(); j++) {
                Operand left = operands.get(i);
                Operand right = operands.get(j);
                List<Operand> remaining = without(operands, i, j);

                tryNext(remaining, combine(left, right, '+'), solutions);
                tryNext(remaining, combine(left, right, '*'), solutions);
                tryNext(remaining, combine(left, right, '-'), solutions);
                tryNext(remaining, combine(right, left, '-'), solutions);

                if (!right.value.isZero()) {
                    tryNext(remaining, combine(left, right, '/'), solutions);
                }
                if (!left.value.isZero()) {
                    tryNext(remaining, combine(right, left, '/'), solutions);
                }
            }
        }
    }

    private static void tryNext(List<Operand> remaining, Operand result, Set<String> solutions) {
        List<Operand> next = new ArrayList<>(remaining);
        next.add(result);
        search(next, solutions);
    }

    private static List<Operand> without(List<Operand> operands, int first, int second) {
        List<Operand> remaining = new ArrayList<>(operands.size() - 1);
        for (int i = 0; i < operands.size(); i++) {
            if (i != first && i != second) {
                remaining.add(operands.get(i));
            }
        }
        return remaining;
    }

    private static Operand combine(Operand left, Operand right, char operator) {
        Fraction value;
        switch (operator) {
            case '+':
                value = left.value.add(right.value);
                break;
            case '-':
                value = left.value.subtract(right.value);
                break;
            case '*':
                value = left.value.multiply(right.value);
                break;
            case '/':
                value = left.value.divide(right.value);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }

        String expression = "(" + left.expression + " " + operator + " " + right.expression + ")";
        return new Operand(value, expression);
    }

    private static String removeOuterParentheses(String expression) {
        return expression.substring(1, expression.length() - 1);
    }

    private static void validate(int[] numbers) {
        if (numbers == null || numbers.length != 4) {
            throw new IllegalArgumentException("Exactly four numbers are required");
        }
    }

    public static void main(String[] args) {
        int[] numbers = args.length == 4 ? parseArguments(args) : readNumbers();
        List<String> solutions = solve(numbers);

        if (solutions.isEmpty()) {
            System.out.println("No solution.");
            return;
        }

        System.out.println("Found " + solutions.size() + " solution(s):");
        for (String solution : solutions) {
            System.out.println(solution + " = 24");
        }
    }

    private static int[] parseArguments(String[] args) {
        int[] numbers = new int[4];
        for (int i = 0; i < args.length; i++) {
            numbers[i] = Integer.parseInt(args[i]);
        }
        return numbers;
    }

    private static int[] readNumbers() {
        System.out.println("Enter four integers:");
        int[] numbers = new int[4];
        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = scanner.nextInt();
            }
        }
        return numbers;
    }

    private static final class Operand {
        private final Fraction value;
        private final String expression;

        private Operand(Fraction value, String expression) {
            this.value = value;
            this.expression = expression;
        }
    }

    private static final class Fraction {
        private final long numerator;
        private final long denominator;

        private Fraction(long numerator, long denominator) {
            if (denominator == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            }

            if (denominator < 0) {
                numerator = -numerator;
                denominator = -denominator;
            }

            long divisor = greatestCommonDivisor(numerator, denominator);
            this.numerator = numerator / divisor;
            this.denominator = denominator / divisor;
        }

        private Fraction add(Fraction other) {
            return new Fraction(
                    numerator * other.denominator + other.numerator * denominator,
                    denominator * other.denominator);
        }

        private Fraction subtract(Fraction other) {
            return new Fraction(
                    numerator * other.denominator - other.numerator * denominator,
                    denominator * other.denominator);
        }

        private Fraction multiply(Fraction other) {
            return new Fraction(numerator * other.numerator, denominator * other.denominator);
        }

        private Fraction divide(Fraction other) {
            return new Fraction(numerator * other.denominator, denominator * other.numerator);
        }

        private boolean isZero() {
            return numerator == 0;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (!(object instanceof Fraction)) {
                return false;
            }
            Fraction other = (Fraction) object;
            return numerator == other.numerator && denominator == other.denominator;
        }

        @Override
        public int hashCode() {
            int result = Long.hashCode(numerator);
            return 31 * result + Long.hashCode(denominator);
        }

        private static long greatestCommonDivisor(long first, long second) {
            first = Math.abs(first);
            second = Math.abs(second);
            while (second != 0) {
                long remainder = first % second;
                first = second;
                second = remainder;
            }
            return first == 0 ? 1 : first;
        }
    }
}
