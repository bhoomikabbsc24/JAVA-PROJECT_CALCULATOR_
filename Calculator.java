import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Calculator extends JFrame implements ActionListener {
    private final JTextField display;
    private final JTextArea historyArea;
    private boolean darkTheme = false;
    private final CalculatorEngine engine = new CalculatorEngine();
    private final HistoryManager historyManager = new HistoryManager();

    public Calculator() {
        setTitle("Enhanced Java Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 520);
        setLayout(new BorderLayout(8,8));
        setLocationRelativeTo(null);

        // Top: display
        display = new JTextField();
        display.setFont(new Font("Consolas", Font.BOLD, 26));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setPreferredSize(new Dimension(0, 60));
        add(display, BorderLayout.NORTH);

        // Center: keypad
        JPanel grid = new JPanel(new GridLayout(6, 7, 6, 6));
        String[] labels = {
            "7","8","9","/","sqrt","(", "Del",
            "4","5","6","*","^",")","C",
            "1","2","3","-","log","ln","±",
            "0",".","=", "+","sin","cos","tan",
            "nCr","nPr","fact","%","abs","π","e",
            "MS","MR","Hist","Theme","Deg","Rad","M"
        };

        for (String s : labels) {
            JButton b = new JButton(s);
            b.setFont(new Font("Arial", Font.PLAIN, 14));
            b.addActionListener(this);
            grid.add(b);
        }

        // Right: history panel
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane histScroll = new JScrollPane(historyArea);
        histScroll.setPreferredSize(new Dimension(300, 0));

        JPanel center = new JPanel(new BorderLayout());
        center.add(grid, BorderLayout.CENTER);
        center.add(histScroll, BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        // Bottom: simple label
        JLabel footer = new JLabel("Enter expression and press =  — supports functions: sin, cos, tan, ln, log, sqrt, nCr(n,r), nPr(n,r), fact(n)");
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        add(footer, BorderLayout.SOUTH);

        // Key bindings: Enter -> evaluate, Esc -> clear, Backspace -> delete last char
        display.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) evaluate();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) display.setText("");
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    // default behavior is fine
                }
            }
        });
        
        // initial history load and theme
        refreshHistory();
        applyTheme();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        try {
            switch (cmd) {
                case "=" -> evaluate();
                case "C" -> display.setText("");
                case "Del" -> {
                    String s = display.getText();
                    if (!s.isEmpty()) display.setText(s.substring(0, s.length()-1));
                }
                case "Hist" -> refreshHistory();
                case "Theme" -> { darkTheme = !darkTheme; applyTheme(); }
                case "MS" -> { // store memory M
                    try {
                        double v = Double.parseDouble(display.getText());
                        engine.storeMemory(v);
                        JOptionPane.showMessageDialog(this, "Stored to M");
                    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Bad number"); }
                }
                case "MR" -> display.setText(Double.toString(engine.recallMemory()));
                case "Deg" -> engine.setDegrees(true);
                case "Rad" -> engine.setDegrees(false);
                case "±" -> toggleSign();
                case "π" -> insertAtCaret(Double.toString(Math.PI));
                case "e" -> insertAtCaret(Double.toString(Math.E));
                case "sqrt","sin","cos","tan","log","ln","abs","fact","nCr","nPr" -> insertAtCaret(cmd + "(");
                case "x" -> insertAtCaret("*");
                case "M" -> insertAtCaret("M");
                default -> insertAtCaret(cmd);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }






    



        


        // Если в стеке остались операторы, добавляем их в входную строку
        while (sbStack.length() > 0) {
            sbOut.append(" ").append(sbStack.substring(sbStack.length()-1));
            sbStack.setLength(sbStack.length()-1);
        }

        return  sbOut.toString();
    }

    /**
     * Функция проверяет, является ли текущий символ оператором
     */
    private static boolean isOp(char c) {
        switch (c) {
            case '-':
            case '+':
            case '*':
            case '/':
            case '^':
                return true;
        }
        return false;
    }

    /**
     * Возвращает приоритет операции
     * @param op char
     * @return byte
     */
    private static byte opPrior(char op) {
        switch (op) {
            case '^':
                return 3;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return 1; // Тут остается + и -
    }

    /**
     * Считает выражение, записанное в обратной польской нотации
     * @param sIn
     * @return double result
     */
    private static double calculate(String sIn) throws Exception {
        double dA = 0, dB = 0;
        String sTmp;
        Deque<Double> stack = new ArrayDeque<Double>();
        StringTokenizer st = new StringTokenizer(sIn);
        while(st.hasMoreTokens()) {
            try {
                sTmp = st.nextToken().trim();
                if (1 == sTmp.length() && isOp(sTmp.charAt(0))) {
                    if (stack.size() < 2) {
                        throw new Exception("Неверное количество данных в стеке для операции " + sTmp);
                    }
                    dB = stack.pop();
                    dA = stack.pop();
                    switch (sTmp.charAt(0)) {
                        case '+':
                            dA += dB;
                            break;
                        case '-':
                            dA -= dB;
                            break;
                        case '/':
                            dA /= dB;
                            break;
                        case '*':
                            dA *= dB;
                            break;
                        case '%':
                            dA %= dB;
                            break;
                        case '^':
                            dA = Math.pow(dA, dB);
                            break;
                        default:
                            throw new Exception("Недопустимая операция " + sTmp);
                    }
                    stack.push(dA);
                } else {
                    dA = Double.parseDouble(sTmp);
                    stack.push(dA);
                }
            } catch (Exception e) {
                throw new Exception("Недопустимый символ в выражении");
            }
        }

        if (stack.size() > 1) {
            throw new Exception("Количество операторов не соответствует количеству операндов");
        }

        return stack.pop();
    }
}
