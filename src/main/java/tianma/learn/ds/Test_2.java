package tianma.learn.ds;

import tianma.learn.ds.Template;

public class Test_2 {
    int j = 1, k = 5;

    public static void main(String[] args) {
        int i = 0, m = 9, a;
        while (i < m) {
            Template.instrum("10", "WhileStatement", "i < m", "i", i, "m", m);
			i++;
			Template.instrum("11", "PostfixExpression", "i++", "i", i);
        }
        a = i + m;
		Template.instrum("13", "Assignment", "a=i + m", "a", a, "i", i, "m", m);
        for (i = 0, m=0; i < 10 && a<10; i++, m++) {
            Template.instrum("14", "ForStatement", "i < 10 && a < 10", "a", a,
					"i", i, "m", m);
			int d = 3;
        }
        new Test_1().abc(a);
		Template.instrum("17", "MethodInvocation", "new Test_1().abc(a)", "a",
				a);
        System.out.println("Local Variable Declarations inside main() : " + i + " , " + m + " , " + a);
    }

    public void abc(int a) {
        if (j == k / 5) {
            Template.instrum("22", "IfStatement", "j == k / 5", "j", j, "k", k);
			if (k==8) {Template.instrum("23", "IfStatement", "k == 8", "k", k);
			j=0;
			Template.instrum("23", "Assignment", "j=0", "j", j);}
            int g = 45;
            System.out.println("Local Variable Declarations inside abc() : " + g);
        }
        else if (j==k/4) {
            Template.instrum("27", "IfStatement", "j == k / 4", "j", j, "k", k);
			System.out.println("Local Variable Declarations inside abc() : " +a);
        }
        else {
            System.out.println("Local Variable Declarations inside abc() :" +a);
        }
        if (k==99) {Template.instrum("33", "IfStatement", "k == 99", "k", k);
		j=0;
		Template.instrum("33", "Assignment", "j=0", "j", j);}
        j = j * k;
		Template.instrum("34", "Assignment", "j=j * k", "j", j, "k", k);
        System.out.println("Global Variable Declarations inside abc() :  " + j + " , " + k);
    }
}