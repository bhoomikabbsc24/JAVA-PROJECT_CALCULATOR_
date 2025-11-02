import java.math.BigInteger;
import java.util.*;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorEngine {
    private boolean degrees = false;
    private final Map<String, Double> variables = new HashMap<>();
    private final Set<String> functions = new HashSet<>(Arrays.asList(
            "sin","cos","tan","asin","acos","atan","ln","log","sqrt","abs","fact","nCr","nPr"
    ));

    public CalculatorEngine() {
        variables.put("M", 0.0);
    }

    public void setDegrees(boolean d) { degrees = d; }
    public boolean isDegrees() { return degrees; }
    public void storeMemory(double v) { variables.put("M", v); }
    public double recallMemory() { return variables.getOrDefault("M", 0.0); }

    public double evaluate(String expr) {
        List<String> rpn = toRPN(expr);
        return evalRPN(rpn);
    }

    private List<String> toRPN(String expr) {
        expr = expr.replaceAll("\\s+","").replaceAll("×", "*").replaceAll("÷", "/");
        List<String> output = new ArrayList<>();
        Stack<String> ops = new Stack<>();
        Pattern tokenPattern = Pattern.compile("\\d*\\.\\d+|\\d+|[a-zA-Z_][a-zA-Z0-9_]*|[()+\\-*/^%,]");
        Matcher m = tokenPattern.matcher(expr);
        List<String> tokens = new ArrayList<>();
        while (m.find()) tokens.add(m.group());
        for (String token: tokens) {
            if (isNumber(token) || isVariable(token)) output.add(token);
            else if (functions.contains(token)) ops.push(token);
            else if (token.equals(",")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) output.add(ops.pop());
                if (ops.isEmpty()) throw new RuntimeException("Misplaced comma");
            } else if (isOperator(token)) {
                while (!ops.isEmpty() && isOperator(ops.peek()) &&
                        ((isLeftAssoc(token) && precedence(token) <= precedence(ops.peek())) ||
                                (!isLeftAssoc(token) && precedence(token) < precedence(ops.peek())))) {
                    output.add(ops.pop());
                }
                ops.push(token);
            } else if (token.equals("(")) ops.push(token);
            else if (token.equals(")")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) output.add(ops.pop());
                if (ops.isEmpty()) throw new RuntimeException("Mismatched parentheses");
                ops.pop();
                if (!ops.isEmpty() && functions.contains(ops.peek())) output.add(ops.pop());
            } else throw new RuntimeException("Unknown token "+token);
        }
        while (!ops.isEmpty()) {
            String op = ops.pop();
            if (op.equals("(") || op.equals(")")) throw new RuntimeException("Mismatched parentheses");
            output.add(op);
        }
        return output;
    }
