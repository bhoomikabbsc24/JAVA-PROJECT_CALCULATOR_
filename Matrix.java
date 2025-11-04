import java.util.Arrays;

public class Matrix {
    private final double[][] a;
 public final int rows, cols;
    public Matrix(double[][] data) {
        rows = data.length; cols = data[0].length; a = new double[rows][cols];
        for (int i=0;i<rows;i++) a[i] = Arrays.copyOf(data[i], cols);
    }
 public Matrix add(Matrix b) {
        if (rows!=b.rows || cols!=b.cols) throw new RuntimeException("Dimension mismatch");
        double[][] out = new double[rows][cols];
        for (int i=0;i<rows;i++) for (int j=0;j<cols;j++) out[i][j] = a[i][j] + b.a[i][j];
        return new Matrix(out);
    }
