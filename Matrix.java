import java.util.Arrays;

public class Matrix {
    private final double[][] a;
 public final int rows, cols;
    public Matrix(double[][] data) {
        rows = data.length; cols = data[0].length; a = new double[rows][cols];
        for (int i=0;i<rows;i++) a[i] = Arrays.copyOf(data[i], cols);
    }
