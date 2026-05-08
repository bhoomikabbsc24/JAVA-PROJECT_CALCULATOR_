# Enhanced Java Calculator

A feature-rich, professional-grade Java calculator application with a modern GUI, advanced mathematical functions, and calculation history tracking.

## 📋 Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
- [Usage Guide](#usage-guide)
- [Supported Functions](#supported-functions)
- [Key Bindings](#key-bindings)
- [Architecture](#architecture)

## ✨ Features

### Core Calculation Features
- **Basic Arithmetic**: Addition, subtraction, multiplication, division, and modulo operations
- **Advanced Functions**: 
  - Trigonometric: sin, cos, tan, asin, acos, atan
  - Logarithmic: ln (natural log), log (base 10)
  - Other: sqrt, factorial, absolute value
  - Combinatorics: nCr (combinations), nPr (permutations)
- **Power Operations**: Exponentiation (^) with support for both positive and negative exponents
- **Angle Modes**: Switch between degrees and radians for trigonometric calculations

### User Interface Features
- **Modern GUI**: Built with Java Swing for cross-platform compatibility
- **Real-time Display**: Clear, large display with right-aligned text
- **Calculation History**: View previous calculations in a dedicated history panel
- **Dark Theme**: Toggle between light and dark themes
- **Mathematical Constants**: Quick access to π (pi) and e (Euler's number)
- **Sign Toggle**: Easy negation of numbers with the ± button

### Memory Functions
- **MS (Memory Store)**: Save a value to memory
- **MR (Memory Recall)**: Retrieve the stored memory value
- **Automatic Memory**: Latest calculation result is automatically stored in memory

### Advanced Features
- **Expression Evaluation**: Support for complex mathematical expressions with proper operator precedence
- **Parentheses Support**: Nested parentheses for grouping operations
- **Unary Operators**: Proper handling of unary minus (negative numbers)
- **BigInteger Support**: Accurate factorial calculations for large numbers
- **Error Handling**: Clear error messages for invalid expressions

## 📁 Project Structure

```
JAVA-PROJECT_CALCULATOR_/
├── Calculator.java              # Main GUI application (JFrame)
├── CalculatorEngine.java        # Mathematical evaluation engine (RPN-based)
├── ComplexNumber.java           # Complex number support class
├── HistoryManager.java          # Calculation history persistence
├── Matrix.java                  # Matrix operations (future enhancement)
└── README.md                    # This file
```

### Class Responsibilities

| Class | Purpose |
|-------|---------|
| **Calculator** | GUI implementation using Swing; handles user interactions and button events |
| **CalculatorEngine** | Core calculation logic; converts infix to RPN; manages variables and memory |
| **ComplexNumber** | Complex number arithmetic (for potential future use) |
| **HistoryManager** | Reads/writes calculation history to persistent storage |
| **Matrix** | Placeholder for matrix operations |

## 🛠️ Technologies Used

- **Java**: Version 8+ (uses Lambda expressions and Java Swing)
- **Swing**: GUI framework for the user interface
- **Regular Expressions**: Pattern matching for tokenization
- **BigInteger**: Arbitrary-precision arithmetic for factorials
- **File I/O**: Persistent history storage

## 📦 Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command-line terminal or IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Compilation

#### Using Command Line
```bash
# Navigate to the project directory
cd JAVA-PROJECT_CALCULATOR_

# Compile all Java files
javac *.java

# Run the application
java Calculator
```

#### Using an IDE
1. Create a new Java project
2. Add all `.java` files from the repository
3. Set `Calculator` as the main class
4. Run the project

### File Structure After Compilation
After compilation, you'll see `.class` files generated for each Java source file.

## 🚀 Usage Guide

### Basic Operations

1. **Simple Calculation**
   - Enter: `5 + 3`
   - Press: `=` or Enter key
   - Result: `8`

2. **Complex Expression**
   - Enter: `(10 + 5) * 2 - 3`
   - Press: `=`
   - Result: `27`

3. **Using Functions**
   - Enter: `sin(30)` (in degrees mode)
   - Press: `=`
   - Result: `0.5`

### Supported Operations & Functions

| Symbol | Operation | Example |
|--------|-----------|---------|
| `+` | Addition | `5 + 3` → `8` |
| `-` | Subtraction | `10 - 4` → `6` |
| `*` | Multiplication | `6 * 7` → `42` |
| `/` | Division | `20 / 4` → `5` |
| `^` | Exponentiation | `2 ^ 8` → `256` |
| `%` | Modulo | `17 % 5` → `2` |
| `sqrt()` | Square root | `sqrt(16)` → `4` |
| `sin()`, `cos()`, `tan()` | Trigonometric | `sin(90)` → `1` (degrees) |
| `ln()` | Natural logarithm | `ln(10)` → `2.303` |
| `log()` | Base-10 logarithm | `log(100)` → `2` |
| `fact()` | Factorial | `fact(5)` → `120` |
| `nCr()` | Combinations | `nCr(5,2)` → `10` |
| `nPr()` | Permutations | `nPr(5,2)` → `20` |
| `abs()` | Absolute value | `abs(-5)` → `5` |

### Button Guide

| Button | Function |
|--------|----------|
| `=` | Evaluate expression |
| `C` | Clear entire display |
| `Del` | Delete last character |
| `Hist` | Refresh history display |
| `Theme` | Toggle dark/light theme |
| `MS` | Store display value to memory |
| `MR` | Recall memory value |
| `Deg` / `Rad` | Switch angle mode |
| `±` | Toggle sign (positive/negative) |
| `π` | Insert pi constant |
| `e` | Insert Euler's number |

## ⌨️ Key Bindings

| Key | Action |
|-----|--------|
| `Enter` | Evaluate expression (same as `=`) |
| `Esc` | Clear display |
| `Backspace` | Delete last character |

## 🏗️ Architecture

### Expression Evaluation Pipeline

```
User Input
    ↓
Tokenization (Regex-based)
    ↓
Unary Operator Processing
    ↓
Shunting-Yard Algorithm (Infix → RPN)
    ↓
RPN Stack Evaluation
    ↓
Result Display
    ↓
History Storage
```

### Key Algorithms

1. **Shunting-Yard Algorithm**: Converts infix notation to Reverse Polish Notation (RPN) with proper operator precedence
2. **RPN Evaluation**: Stack-based calculation of postfix expressions
3. **Unary Minus Handling**: Special preprocessing to correctly handle negative numbers and unary operators

### Operator Precedence (Highest to Lowest)

1. **Unary Minus** (precedence: 5)
2. **Exponentiation `^`** (precedence: 4, right-associative)
3. **Multiplication, Division, Modulo** (precedence: 3)
4. **Addition, Subtraction** (precedence: 2)

## 💾 History Management

- Calculations are automatically saved to persistent storage
- History is displayed in a scrollable panel on the right side
- Most recent calculations appear at the top
- Format: `expression = result`

## 🔍 Error Handling

The calculator provides clear error messages for:
- Mismatched parentheses
- Division by zero
- Invalid function arguments
- Insufficient operands
- Malformed expressions

Example:
```
Input: 1/0
Error: Division by zero
```

## 📝 Example Calculations

```
5 + 3 * 2                    → 11
(5 + 3) * 2                  → 16
sqrt(16) + abs(-5)           → 9
sin(90) + cos(0)             → 2 (degrees mode)
2^10 - 100                   → 924
fact(5) / nCr(5,2)          → 12
```

## 🐛 Known Limitations

- Complex number support is available in the `ComplexNumber` class but not yet integrated into the GUI
- Matrix operations are not yet implemented
- History is stored locally; no cloud sync
- Maximum calculation precision depends on Java's `Double` type

## 🎯 Future Enhancements

- [ ] Integrate complex number operations into GUI
- [ ] Implement matrix calculator
- [ ] Add equation solver
- [ ] Include graph plotting capability
- [ ] Support for scientific notation display
- [ ] User preferences/settings persistence
- [ ] Multi-line expression input

## 📄 License

This project is created as part of a Java CIE 3 assessment.

## 👤 Author

Bhoomika BBBSC24

---

**Version**: 1.0  
**Last Updated**: 2026-05-08
