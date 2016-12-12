import java.util.Scanner;
import java.lang.Math;
import java.util.Locale;
import java.text.DecimalFormat;
public class HMMC9 {

    /* --- all variables refer to tutorial stamp --- */

    // length of observation sequence
    private static int _t;
    // number of states in the model
    private static int _n;
    // number of observation symbols
    private static int _m;
    // state transition probabilities
    private static double _a[][];
    // observation probability matrix
    private static double _b[][];
    // initial state distribution
    private static double _pi[];
    // observation sequence
    private static int _o[];
    // scale coefficient
    private static double _c[];
    // gamma in b-w algorithm
    private static double _gamma_t[][][];
    private static double _gamma[][];
    // alpha in b-w algorithm
    private static double _alpha[][];
    // beta in b-w algorithm
    private static double _beta[][];
    // re-estimation matrix in b-w algorithm
    private static double _a_0[][];
    private static double _b_0[][];

    public static void main( String[] args ) {
        
        // 0. read the matrices into buffer
        Locale.setDefault(Locale.ENGLISH);
        Scanner sc = new Scanner(System.in);

        /* below process gets initialization data */
        _n = sc.nextInt();
        if (_n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        _a = new double[_n][_n];
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _n; j++) {
                _a[i][j] = sc.nextDouble(); // A
            }
        }

        if (_n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        _m = sc.nextInt();
        _b = new double[_n][_m]; // n * m
        for (int i = 0; i < _n; i++) {
            for (int j = 0; j < _m; j++) {
                _b[i][j] = sc.nextDouble(); // B
            }
        }

        if (1 != sc.nextInt()) {
            System.out.println("*** var error: pi mismatch ***");
        }
        if (_n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        double[] _oldPI = new double[_n];
        _pi = new double[_n];
        for (int i = 0; i < _n; i++) {
            _pi[i] = sc.nextDouble(); // pi
            _oldPI[i]=_pi[i];
        }

        _t = sc.nextInt();
        _o = new int[_t];
        for (int i = 0; i < _o.length; i++) {
            _o[i] = sc.nextInt(); // O
        }
        sc.close();

        // 1. initialization
        int maxIters = 10000;
        int iters = 0;
        double oldLogProb = -1000000.0;
        //double convergenceLimit = -1;
        double[][] _oldA = _a;
        double[][] _oldB = _b; 
        double[][] _correctA = {{0.7, 0.05, 0.25}, {0.1, 0.80, 0.10}, {0.2, 0.30, 0.50}};
        double[][] _correctB = {{0.7, 0.2, 0.1, 0.0}, {0.1, 0.4, 0.3, 0.2}, {0.0, 0.1, 0.2, 0.7}};
        _alpha = new double[_t][_n];
        _beta = new double[_t][_n];
        _a_0 = new double[_n][_n];
        _b_0 = new double[_n][_m];
        _gamma_t = new double[_t][_n][_n];
        _gamma = new double[_t][_n];
        _c = new double[_t];
        while (true) {
            // 2. the alpha-pass
            _alpha = alphaPass(_o, _pi, _a, _b);
            // 3. the beta-pass
            _beta = betaPass(_o, _pi, _a, _b);
            // 4. computer gamma
            computeGamma(_o, _a, _b);
            // 5. re-estimate
            reEstimate(_o, _pi, _a, _b);

            // _compute log[P(O | ?)]
            double logProb = 0.0;
            for (int i = 0; i < _t; i++)
                logProb += log(_c[i], 10);
            logProb = -logProb;
            //System.out.println("\nold probability " + oldLogProb + "\nnew probability " + logProb );
            // To iterate or not to iterate, that is the question. . .
            iters++;
            if ((iters < maxIters) && (logProb > oldLogProb))
                oldLogProb = logProb;
            else{
                if (logProb <= oldLogProb) 
                    System.out.println("stopped due to logProb <= oldLogProb!");
                else if (iters < maxIters)
                    System.out.println("Max iterations reached");
                else 
                    System.out.println("weird...");

                System.out.println("Iterations " + iters +"\nlogProb " + logProb + "\n");

                break; 
            }
        }

        System.out.println("\nPI original");
        printReadableMatrix(_oldPI);

        System.out.println("\nA original");
        printReadableMatrix(_oldA);
        System.out.println("A");
        printReadableMatrix(_a);
        System.out.println("Difference A");
        printMatrixDistance(_a,_oldA);

        System.out.println("\nB original");
        printReadableMatrix(_oldB);
        System.out.println("B");
        printReadableMatrix(_b);
        System.out.println("Difference B");
        printMatrixDistance(_b,_oldB);

    }

    private static double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }

    private static void reEstimate(int[] O, double[] pi, double[][] A,
            double[][] B) {

        int N = _n;
        int T = _t;
        int M = _m;

        // re-estimate p
        for (int i = 0; i < N; i++) {
            pi[i] = _gamma[0][i];
        }

        // re-estimate A
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double numer = 0.0;
                double denom = 0.0;
                for (int t = 0; t < T - 1; t++) {
                    numer += _gamma_t[t][i][j];
                    denom += _gamma[t][i];
                }
                _a_0[i][j] = numer / denom;
            }
        }

        // re-estimate B
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                double numer = 0.0;
                double denom = 0.0;
                for (int t = 0; t < T - 1; t++) {
                    if (O[t] == j)
                        numer += _gamma[t][i];
                    denom += _gamma[t][i];
                }
                _b_0[i][j] = numer / denom;
            }
        }

        for (int i = 0; i < pi.length; i++) {
            _pi[i] = _gamma[0][i];
        }

        _a = _a_0;
        _b = _b_0; 

    }

    private static void computeGamma(int[] O, double[][] A, double[][] B) {
        int N = _n;
        int T = _t;

        for (int t = 0; t < T - 1; t++) {
            double denom = 0.0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    denom += _alpha[t][i] * _a[i][j] * _b[j][_o[t + 1]]
                            * _beta[t + 1][j];
                }
            }

            for (int i = 0; i < N; i++) {
                _gamma[t][i] = 0.0;
                for (int j = 0; j < N; j++) {
                    _gamma_t[t][i][j] = (_alpha[t][i] * _a[i][j] * _b[j][_o[t + 1]] * _beta[t + 1][j]) / denom;
                    _gamma[t][i] += _gamma_t[t][i][j];
                }
            }
        }
    }

    private static double[][] betaPass(int[] O, double[] pi, double[][] A,
            double[][] B) {
        int N = _n;
        int T = _t;

        double[][] beta = new double[T][N];

        // Scale beta.
        for (int i = 0; i < N; i++)
            beta[T - 1][i] = _c[T - 1];

        // Beta-pass
        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < N; i++) {
                beta[t][i] = 0.0;
                for (int j = 0; j < N; j++)
                    beta[t][i] = beta[t][i]
                            + (A[i][j] * B[j][O[t + 1]] * beta[t + 1][j]);
                // Scale beta with same factor as alpha
                beta[t][i] = _c[t] * beta[t][i];
            }
        }

        return beta;
    }

    private static double[][] alphaPass(int[] O, double[] pi, double[][] A,
            double[][] B) {
        // Initialization
        int N = _n;
        int T = _t;

        double[][] alpha = new double[T][N];

        // _compute alpha[0][i]
        _c[0] = 0.0;
        for (int i = 0; i < N; i++) {
            alpha[0][i] = pi[i] * B[i][O[0]];
            _c[0] += alpha[0][i];
        }

        // Scale the alpha[0][i]
        _c[0] = 1.0 / _c[0];
        for (int i = 0; i < N; i++)
            alpha[0][i] = _c[0] * alpha[0][i];

        // _compute alpha[t][i]
        for (int t = 1; t < T; t++) {
            _c[t] = 0.0;
            for (int i = 0; i < N; i++) {
                alpha[t][i] = 0.0;
                for (int j = 0; j < N; j++)
                    alpha[t][i] = alpha[t][i] + (alpha[t - 1][j] * A[j][i]);
                alpha[t][i] = alpha[t][i] * B[i][O[t]];
                _c[t] = _c[t] + alpha[t][i];
            }
            // Scale alpha[t][i]
            _c[t] = 1.0 / _c[t];
            for (int i = 0; i < N; i++)
                alpha[t][i] = _c[t] * alpha[t][i];
        }
        return alpha;
    }

    private static void printMatrix(double[][] matrix) {
        int rows = 0;
        int cols = 0;
        String line = "";
        for (double[] row : matrix) {
            rows++;
            for (double j : row) {
                cols++;
                line = line + (j + " ");
            }
        }
        System.out.println(matrix.length + " " + matrix[0].length + " " + line);
    }
    private static void printReadableMatrix(double[] matrix) {
        DecimalFormat numberFormat = new DecimalFormat("#.00000000");
        for(double col : matrix){
            System.out.print(numberFormat.format(col) + " ");
        }
        System.out.println();
    }

    private static void printReadableMatrix(double[][] matrix) {
        String line = "";
        DecimalFormat numberFormat = new DecimalFormat("#.00000000");
        for (double[] row : matrix) {
            for (double col : row) {
                System.out.print(numberFormat.format(col) + " ");
            }
            System.out.println();
        }
    }

    private static void printMatrixDistance(double[][] original, double[][] m2){
        DecimalFormat numberFormat = new DecimalFormat("#.00000000");
        double totalDist = 0.0;
        double tmp = 0.0;
        for(int row=0; row<original.length; row++) {
            for (int col=0; col<original[0].length; col++ ) {
                tmp = Math.abs(original[row][col]-m2[row][col]);
                totalDist += tmp;
                System.out.print(numberFormat.format(tmp)+ " ");   
            }
            System.out.println();
        }
        System.out.println("Total additive distance " + totalDist );
        System.out.println("Average additive distance " + totalDist/(m2.length*m2[0].length) );
    }
}