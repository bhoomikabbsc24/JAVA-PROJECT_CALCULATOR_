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
    private double evalRPN(List<String> rpn) {
        Stack<Double> st = new Stack<>();
        for (String token: rpn) {
            if (isNumber(token)) st.push(Double.parseDouble(token));
            else if (isVariable(token)) st.push(variables.getOrDefault(token, 0.0));
            else if (functions.contains(token)) applyFunction(token, st);
            else if (isOperator(token)) {
                if (st.size() < 2) throw new RuntimeException("Insufficient operands");
                double b = st.pop(); double a = st.pop();
                st.push(applyOp(token, a, b));
            } else throw new RuntimeException("Unknown RPN token "+token);
        }
        if (st.size() != 1) throw new RuntimeException("Invalid expression");
        return st.pop();
    }
    private void applyFunction(String fn, Stack<Double> st) {
        switch (fn) {
            case "sin": applyUnary(st, x -> Math.sin(adjustAngle(x))); break;
            case "cos": applyUnary(st, x -> Math.cos(adjustAngle(x))); break;
            case "tan": applyUnary(st, x -> Math.tan(adjustAngle(x))); break;
            case "asin": applyUnary(st, x -> revertAngle(Math.asin(x))); break;
            case "acos": applyUnary(st, x -> revertAngle(Math.acos(x))); break;
            case "atan": applyUnary(st, x -> revertAngle(Math.atan(x))); break;
            case "ln": applyUnary(st, Math::log); break;
            case "log": applyUnary(st, x -> Math.log10(x)); break;
            case "sqrt": applyUnary(st, Math::sqrt); break;
            case "abs": applyUnary(st, Math::abs); break;
            case "fact": applyUnary(st, this::factorial); break;
            case "nCr": applyBinaryInt(st, this::nCr); break;
            case "nPr": applyBinaryInt(st, this::nPr); break;
            default: throw new RuntimeException("Unsupported function "+fn);
        }
    }
    private double adjustAngle(double v) { return degrees ? Math.toRadians(v) : v; }
    private double revertAngle(double r) { return degrees ? Math.toDegrees(r) : r; }

    private void applyUnary(Stack<Double> st, DoubleUnaryOperator op) {
        if (st.isEmpty()) throw new RuntimeException("Insufficient args");
        st.push(op.applyAsDouble(st.pop()));
    }
    private void applyBinaryInt(Stack<Double> st, java.util.function.BiFunction<Integer,Integer,Double> f) {
        if (st.size() < 2) throw new RuntimeException("Insufficient args");
        int b = (int)Math.round(st.pop()); int a = (int)Math.round(st.pop());
        st.push(f.apply(a,b));
    }
    private double factorial(double x) {
        int n = (int)Math.round(x);
        if (n < 0) throw new RuntimeException("Negative factorial");
        BigInteger res = BigInteger.ONE;
        for (int i=2;i<=n;i++) res = res.multiply(BigInteger.valueOf(i));
        return res.doubleValue();
    }
    private Double nCr(Integer n, Integer r) {
        if (r<0 || n<0 || r>n) return 0.0;
        return (double)binomial(n,r);
    }
    private Double nPr(Integer n, Integer r) {
        if (r<0 || n<0 || r>n) return 0.0;
        BigInteger res = BigInteger.ONE;
        for (int i=0;i<r;i++) res = res.multiply(BigInteger.valueOf(n-i));
        return res.doubleValue();
    }
      private long binomial(int n,int k) {
        if (k<0||k>n) return 0;
        k = Math.min(k, n-k);
        BigInteger num = BigInteger.ONE, den = BigInteger.ONE;
        for (int i=1;i<=k;i++) {
            num = num.multiply(BigInteger.valueOf(n-(k-i)));
            den = den.multiply(BigInteger.valueOf(i));
        }
        return num.divide(den).longValue();
    }
    private static boolean isNumber(String s) { return s.matches("\\d*\\.\\d+|\\d+"); }
    private static boolean isVariable(String s) { return s.matches("[a-zA-Z_][a-zA-Z0-9_]*"); }
    private static boolean isOperator(String s) { return "+-*/^%".contains(s); }
    private static int precedence(String op) {
        switch(op) { case "+": case "-": return 2; case "*": case "/": case "%": return 3; case "^": return 4; default: return 0; }
    }
