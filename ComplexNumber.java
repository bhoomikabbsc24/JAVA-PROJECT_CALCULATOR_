public class ComplexNumber {
    public final double re, im;
    public ComplexNumber(double r, double i) { re = r; im = i; }
    public ComplexNumber add(ComplexNumber o) { return new ComplexNumber(re+o.re, im+o.im); }
    public ComplexNumber sub(ComplexNumber o) { return new ComplexNumber(re-o.re, im-o.im); }
    public ComplexNumber mul(ComplexNumber o) { return new ComplexNumber(re*o.re - im*o.im, re*o.im + im*o.re); }
    public ComplexNumber div(ComplexNumber o) { double d = o.re*o.re + o.im*o.im; return new ComplexNumber((re*o.re+im*o.im)/d, (im*o.re - re*o.im)/d); }
    public double mag() { return Math.hypot(re, im); }
    public ComplexNumber conj() { return new ComplexNumber(re, -im); }
    @Override public String toString() {
        if (im==0) return String.format("%.6f", re);
        if (re==0) return String.format("%.6fi", im);
        return String.format("%.6f%+.6fi", re, im);
    }
}
