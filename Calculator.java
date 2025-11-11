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

    private void insertAtCaret(String txt) {
        int pos = display.getCaretPosition();
        String before = display.getText();
        String after = before.substring(0, pos) + txt + before.substring(pos);
        display.setText(after);
        display.requestFocus();
        display.setCaretPosition(pos + txt.length());
    }

    private void toggleSign() {
        String t = display.getText();
        if (t.isEmpty()) return;
        if (t.startsWith("-")) display.setText(t.substring(1));
        else display.setText("-" + t);
    }

    private void evaluate() {
        String expr = display.getText().trim();
        if (expr.isEmpty()) return;
        try {
            double res = engine.evaluate(expr);
            String out;
            if (Math.abs(Math.rint(res) - res) < 1e-10) out = String.format("%.0f", res);
            else out = Double.toString(res);
            historyManager.append(expr, out);
            refreshHistory();
            display.setText(out);
            engine.storeMemory(res);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Evaluation error: " + ex.getMessage());
        }
    }

    private void refreshHistory() {
        try {
            java.util.List<String> lines = historyManager.readAll();
            StringBuilder sb = new StringBuilder();
            for (int i = lines.size()-1; i >= 0; i--) sb.append(lines.get(i)).append("\n");
            historyArea.setText(sb.toString());
        } catch (Exception e) {
            historyArea.setText("");
        }
    }
    private void applyTheme() {
         Color bg = darkTheme ? Color.DARK_GRAY : Color.WHITE;
         Color fg = darkTheme ? Color.WHITE : Color.BLACK;
         display.setBackground(bg); display.setForeground(fg);
         historyArea.setBackground(bg); historyArea.setForeground(fg);
         getContentPane().setBackground(darkTheme ? Color.GRAY : UIManager.getColor("Panel.background"));
     }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}





    



        


     
   
