package tianma.learn.ds.Launcher;

import javassist.ClassPool;
import javassist.CtClass;

        import javassist.ClassPool;
        import javassist.CtClass;
        import javassist.ClassPool;
        import javassist.CtClass;
        import javassist.*;
import javassist.bytecode.*;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;
        import javassist.expr.MethodCall;
        import javassist.CtMethod;
        import javassist.util.HotSwapper;

        import java.lang.reflect.Method;
        import java.util.Scanner;


/*
class MyTranslator implements Translator {
    public void start(ClassPool pool)
            throws NotFoundException, CannotCompileException {}
    public void onLoad(ClassPool pool, String classname)
            throws NotFoundException, CannotCompileException
    {
        CtClass cc = pool.get(classname);
        cc.setModifiers(Modifier.PUBLIC);
    }
}
*/
public class SubstituteOperator {


    public static void main(String args[]) throws Throwable {

        //  int[] a = {9, 3, 2, 6, 8, 7, 1, 12, 4, 11, 10, 5};
        Config.Point p = new Config.Point();
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("tianma.learn.ds.Launcher.Point");
        CtMethod cm = cc.getDeclaredMethod("move");
        cc.removeMethod(cm);

        String[] arg = {"x1", "x2", "x3"};


        CtMethod newcm = CtMethod.make("public void move(int dx, int dy, int dz) {\n" +
                "        x += dx; y += dy; z += dz;\n" +
                "        if(x==y){\n" +
                "            x = x+1;\n" +
                "            y = y+100;\n"+
                "            z = z+100;\n"+
                "        }\n" +
                "    }",cc);


        newcm.insertAfter("{ System.out.println(x); System.out.println(y);  System.out.println(z); System.out.println(count); }");
        cc.addMethod(newcm);
        cc.writeFile();
        cc.toClass();
        System.out.println(cc.toString());
        /*for(Method me: cc.toClass().getDeclaredMethods()){ //test print, ok
            System.out.println(me.getName()+me.getParameterCount());
            System.out.println("===================");
        }*/

        tianma.learn.ds.Launcher.Point point = new tianma.learn.ds.Launcher.Point();
        //point.move(p.x, p.y, p.z);
        Method move =  point.getClass().getDeclaredMethod("move",int.class,int.class,int.class);
        move.invoke(point,1,1,2);


    }

}