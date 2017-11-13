package tianma.learn.ds.Launcher;


        import com.sun.org.apache.xpath.internal.operations.String;

public class Point {
    int x=0,y=0,z=0;
    int count = 5;

    public void move(int dx, int dy, int dz) {
        x += dx; y += dy; z += dz;
        if(x==y){
            x = x+1;
        }
    }


       /* public static class SClas s {
                private int i = 0;
                int k = 3;
                private String myString = "abc";
                private long value = -1;
                //int x = k * k/3 + k*++i + k;
                int x = 0; int y = 0;
                void move(int dx, int dy) { x += dx; y += dy; }
        }
        */

}
