package tianma.learn.ds.AST;

public class JavaParserTest {
    int j = 1, k =5;

    public static void main(String[] args) {
        int i = 0, m =9, a;
        while( i<m ) {
            i++;
        }
        a = i+m;
        new JavaParserTest().abc();
        System.out.println("Local Variable Declarations inside main() : "+ i +" , "+ m+" , "+a);
    }

    public void abc() {
        if (j == k/5) {
            int g = 45;
            System.out.println("Local Variable Declarations inside abc() : " + g);
        }
        j = j*k;
        System.out.println("Global Variable Declarations inside abc() :  "+ j + " , " + k);
    }
}
