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
    public Matrix multiply(Matrix b) {
        if (cols!=b.rows) throw new RuntimeException("Dimension mismatch");
        double[][] out = new double[rows][b.cols];
        for (int i=0;i<rows;i++) for (int j=0;j<b.cols;j++) {
            double s=0; for (int k=0;k<cols;k++) s += a[i][k]*b.a[k][j]; out[i][j] = s;
        }
        return new Matrix(out);
    }
    public double determinant() { if (rows!=cols) throw new RuntimeException("Not square"); return det(a); }
    private double det(double[][] m) {
        int n = m.length;
        if (n==1) return m[0][0];
        if (n==2) return m[0][0]*m[1][1] - m[0][1]*m[1][0];
        double sum=0;
        for (int j=0;j<n;j++) sum += Math.pow(-1,j) * m[0][j] * det(minor(m,0,j));
        return sum;
    }
    private double[][] minor(double[][] m,int row,int col) {
        int n = m.length; double[][] mm = new double[n-1][n-1];
        int r=0;
        for (int i=0;i<n;i++) {
            if (i==row) continue;
            int c=0;
            for (int j=0;j<n;j++) {
                if (j==col) continue;
                mm[r][c++] = m[i][j];
            }
            r++;
        }
        return mm;
    }
