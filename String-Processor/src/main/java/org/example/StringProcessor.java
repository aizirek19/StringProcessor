package org.example;



import javax.naming.Context;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Stack;

public class StringProcessor {

    public boolean isStrongPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }
        // Check for at least one uppercase letter, one lowercase letter, one digit, and one special symbol
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()-+";
        return specialChars.contains(String.valueOf(ch));
    }

    public int calculateDigits(String sentence) {
        int digitCount = 0;
        for (char ch : sentence.toCharArray()) {
            if (Character.isDigit(ch)) {
                digitCount++;
            }
        }
        return digitCount;
    }

    public int calculateWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }

        // Split the sentence by spaces and count the words
        String[] words = sentence.split("\\s+");
        return words.length;
    }

    public double calculateExpression(String expression) {
        // Using Java's built-in ScriptEngine to evaluate mathematical expressions
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        try {
            Object result = engine.eval(expression);
            if (result instanceof Number) {
                return ((Number) result).doubleValue();
            } else {
                // Handle non-numeric results or unexpected cases
                throw new IllegalStateException("Invalid result for the expression: " + expression);
            }
        } catch (ScriptException e) {
            // Handle script evaluation errors, e.g., due to invalid expressions
            throw new IllegalArgumentException("Invalid expression: " + expression, e);
        }
    }

    public double evaluateExpression(String expression) {
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (Character.isDigit(ch)) {
                operands.push((double) Character.getNumericValue(ch));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    evaluate(operands, operators);
                }
                operators.pop(); // Pop the '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && hasHigherPrecedence(ch, operators.peek())) {
                    evaluate(operands, operators);
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            evaluate(operands, operators);
        }

        return operands.pop();
    }

    private void evaluate(Stack<Double> operands, Stack<Character> operators) {
        double operand2 = operands.pop();
        double operand1 = operands.pop();
        char operator = operators.pop();

        switch (operator) {
            case '+':
                operands.push(operand1 + operand2);
                break;
            case '-':
                operands.push(operand1 - operand2);
                break;
            case '*':
                operands.push(operand1 * operand2);
                break;
            case '/':
                operands.push(operand1 / operand2);
                break;
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private boolean hasHigherPrecedence(char op1, char op2) {
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }
}
