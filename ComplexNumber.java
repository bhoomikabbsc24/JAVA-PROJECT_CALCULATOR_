public class ComplexNumber {
    public final double re, im;
    public ComplexNumber(double r, double i) { re = r; im = i; }
    public ComplexNumber add(ComplexNumber o) { return new ComplexNumber(re+o.re, im+o.im); }
    public ComplexNumber sub(ComplexNumber o) { return new ComplexNumber(re-o.re, im-o.im); }
