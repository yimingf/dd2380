import java.util.Scanner;
import java.lang.Math;
import java.util.Locale;
public class HMM3 {

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
    // observation sequence
    public int _o[];

    public HMM3() {}

    public double _log(double foo) {
        if (foo != 0.0) {
            return Math.log(foo);
        } else {
            return -1;
        }
    }

    public static void main( String[] args ) {
        
        HMM3 h = new HMM3();
        // 1. read the matrices into buffer
        Locale.setDefault(Locale.ENGLISH);
        Scanner sc = new Scanner(System.in);

        /* below process dedicated for HMM3 */
        h._n = sc.nextInt();
        if (h._n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        h._a = new double[h._n][h._n];
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._n; j++) {
                h._a[i][j] = sc.nextDouble();//h._log(sc.nextDouble()); // A
            }
        }

        if (h._n != sc.nextInt()) {
            System.out.println("*** var error: N mismatch ***");
        }
        h._m = sc.nextInt();
        h._b = new double[h._n][h._m]; // m * n
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._m; j++) {
                h._b[i][j] = sc.nextDouble();//h._log(sc.nextDouble()); // B
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
            h._pi[i] = sc.nextDouble();//h._log(sc.nextDouble()); PI
        }

        h._o = new int[sc.nextInt()];
        for (int i = 0; i < h._o.length; i++) {
            h._o[i] = sc.nextInt();
        }

        // 2. viterbi algorithm
        h._t = h._o.length;
        double[][] _delta = new double[h._t][h._n];
        int[][] _delta_idx = new int[h._t][h._n];
        for (int i = 0; i < h._n; i++) {
            _delta[0][i] = h._b[i][h._o[0]] * h._pi[i]; // my1(X1) e.g. basecase of viterbi
        }
        
        for (int t = 1; t < h._t; t++) {
            for (int i = 0; i < h._n; i++) {
                _delta[t][i] = 0.0; 
                for (int j = 0; j < h._n; j++) {
                    double foo = h._a[j][i] * _delta[t-1][j] * h._b[i][h._o[t]]; // probability of 
                    if (foo > _delta[t][i]) {
                        _delta[t][i] = foo;
                        _delta_idx[t][i] = j;
                        //System.out.print(foo);
                    }
                } /// for 3
                //System-out.println();
                //System.out.println(_delta[t][i]);
            } // for 2      
            //System.out.println();
        } // for 1

        int[] _x = new int[h._t];

        double foo = 0.0;
        for (int j = 0; j < h._n; j++) {
            if (_delta[h._t-1][j] > foo) {
                foo = _delta[h._t-1][j];
                _x[h._t-1] = j;
            }
        } // x[t-1]

        for (int t = h._t-2; t >= 0; t--) {
            _x[t] = _delta_idx[t+1][_x[t+1]];
        }

        // 3. print out the results
        String str = "";
        for (int i = 0; i < _x.length; i++) {
            str += Integer.toString(_x[i]) + " ";
        }
        System.out.println(str);
    }
}