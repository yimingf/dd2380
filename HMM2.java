import java.util.Scanner;
import java.lang.Math;
import java.util.Locale;
public class HMM2 {

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

    public HMM2() {}

    public static void main( String[] args ) {
        
        HMM2 h = new HMM2();
        // 1. read the matrices into buffer
        Locale.setDefault(Locale.ENGLISH);
        Scanner sc = new Scanner(System.in);

        /* below process dedicated for HMM2 */
        h._n = sc.nextInt();
        if (h._n != sc.nextInt()) {
            //System.out.println("*** var error: N mismatch ***");
        }
        //System.out.println("\nA");
        h._a = new double[h._n][h._n];  
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._n; j++) {
                h._a[i][j] = sc.nextDouble(); // A
                //System.out.print(h._a[i][j] + " ");
            }
            //System.out.println();
        }

        if (h._n != sc.nextInt()) {
            //System.out.println("*** var error: N mismatch ***");
        }
        h._m = sc.nextInt();
        //System.out.println("\nB");
        h._b = new double[h._n][h._m]; // m * n
        for (int i = 0; i < h._n; i++) {
            for (int j = 0; j < h._m; j++) {
                h._b[i][j] = sc.nextDouble(); // B
                //System.out.print(h._b[i][j] + " ");
            }
            //System.out.println();
        }

        if (1 != sc.nextInt()) {
            //System.out.println("*** var error: pi mismatch ***");
        }
        if (h._n != sc.nextInt()) {
            //System.out.println("*** var error: N mismatch ***");
        }
        //System.out.println("\nPI");
        h._pi = new double[h._n];
        for (int i = 0; i < h._n; i++) {
            h._pi[i] = sc.nextDouble();
            //System.out.println(h._pi[i]);
        }

        h._o = new int[sc.nextInt()];
        for (int i = 0; i < h._o.length; i++) {
            h._o[i] = sc.nextInt();
        }

        // 2. forward algorithm
        //System.out.println("\nAlpha iteration 1, basecase");
        h._t = h._o.length;
        double[][] _alpha = new double[h._t][h._n];
        for (int i = 0; i < h._n; i++) {
            _alpha[0][i] = h._pi[i] * h._b[i][h._o[0]]; // pi * 
            //System.out.println(h._pi[i]  + " * " + h._b[i][h._o[0]] + " = " + _alpha[0][i]);
        }

        ////System.out.println("\nAlpha");
        double foo = 0.0;
        for (int t = 1; t < h._t; t++) { // for each state 
            ////System.out.println("State t = " + t);
            for (int i = 0; i < h._n; i++) { // for each column in A
                foo = 0.0;
                for (int j = 0; j < h._n; j++) { // for each row in A 
                    foo += _alpha[t-1][j] * h._a[j][i];
                } // for 3
                _alpha[t][i] = foo * h._b[i][h._o[t]];
                ////System.out.println(_alpha[t][i]);
            } // for 2
            ////System.out.println();
        } // for 1



        // 3. print out the results
        //String str = "1 " + Integer.toString(bar.length);
        double result = 0.0;
        for (int i = 0; i < h._n; i++) {
            result += _alpha[h._t-1][i];
        }
        System.out.println(result);
    }
}