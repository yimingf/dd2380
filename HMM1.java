import java.util.Scanner;
import java.lang.Math;
import java.util.Locale;
public class HMM1 {

    /* --- all variables refer to tutorial stamp --- */

    // length of observation sequence
    public int _t;
    // number of states in the model
    public int _n;
    // number of observation symbols
    public int _m;
    // state transition probabilities
    public double _a[][];
    // observation probability matrix
    public double _b[][];
    // initial state distribution
    public double _pi[];

    public HMM1() {}

    public static void main( String[] args ) {
        
        HMM1 h = new HMM1();
        // 1. read the matrices into buffer
        Locale.setDefault(Locale.ENGLISH);
        Scanner sc = new Scanner(System.in);

        /* below process dedicated for HMM1 */
        h._n = sc.nextInt();
        if (h._n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        h._a = new double[h._n][h._n];
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._n; j++) {
                double tmp = sc.nextDouble();
                h._a[j][i] = tmp; // A
            }
        }

        if (h._n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        h._m = sc.nextInt();
        h._b = new double[h._m][h._n]; // m * n
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._m; j++) {
                h._b[j][i] = sc.nextDouble(); // B
            }
        }

        if (1 != sc.nextInt()) {
            System.out.println("*** var error: pi mismatch ***");
        }
        if (h._n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        h._pi = new double[h._n];
        for (int i = 0; i < h._n; i++) {
            h._pi[i] = sc.nextDouble();
        }

        // 2. matrix multiplication
        double[] foo = new double[h._n];
        for (int i = 0; i < h._n; i++) {
            double bar = 0.0;
            for (int j = 0; j < h._n; j++) {
                bar += h._pi[j] * h._a[i][j];
            }
            foo[i] = bar;
        }

        double[] bar = new double[h._m];
        for (int i = 0; i < h._m; i++) {
            double baz = 0.0;
            for (int j = 0; j < h._n; j++) {
                baz += foo[j] * h._b[i][j];
            }
            bar[i] = baz;//Math.round(baz * 10) / 10.0; // rounding
        }

        // 3. print out the results
        StringBuilder sb = new StringBuilder();
        sb.append("1 ");
        sb.append(Integer.toString(bar.length));
        for (int i = 0; i < bar.length; i++) {
            sb.append(" ");
            sb.append(bar[i]);
        }
        System.out.println(sb.toString());
    }
}