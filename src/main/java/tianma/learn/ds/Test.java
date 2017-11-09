package tianma.learn.ds;

import tianma.learn.ds.Template;

public class Test {
    int j = 1, k = 5;

    public static void main(String[] args) {
        int i = 0, m = 9, a;
        while (i < m) {
            i++;
        }
        a = i + m;
        for (i = 0, m=0; i < 10 && a<10; i++, m++) {
            int d = 3;
        }
        new Test().abc(a);
        System.out.println("Local Variable Declarations inside main() : " + i + " , " + m + " , " + a);
    }

    public void abc(int a) {
        if (j == k / 5) {
            if (k==8) {j=0;}
            int g = 45;
            System.out.println("Local Variable Declarations inside abc() : " + g);
        }
        else if (j==k/4) {
            System.out.println("Local Variable Declarations inside abc() : " +a);
        }
        else {
            System.out.println("Local Variable Declarations inside abc() :" +a);
        }
        if (k==99) {j=0;}
        j = j * k;
        System.out.println("Global Variable Declarations inside abc() :  " + j + " , " + k);
    }
}