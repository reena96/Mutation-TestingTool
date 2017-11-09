package tianma.learn.ds;

import java.lang.reflect.Method;

        import javassist.ClassPool;
        import javassist.CtClass;
        import javassist.*;
        import java.lang.reflect.InvocationTargetException;
        import java.lang.reflect.Method;
        import java.util.Scanner;

public class TestInvoke {

    // test Javassist
    public static void main (String[] args) throws NotFoundException, CannotCompileException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        /*
           tianma.learn.ds.sort.BubbleSorter2 currentClass = new tianma.learn.ds.sort.BubbleSorter2();
        Method methodCall =  currentClass.getClass().getDeclaredMethod("sort",int[].class);
        // int[] a = {9, 3, 2, 6, 8, 7, 1, 12, 4, 11};
        methodCall.invoke(currentClass,Config.BubbleSorter.input_array);
         */
        tianma.learn.ds.string.main.StringMatchSample2.ViolentStringMatcher currentClass = new tianma.learn.ds.string.main.StringMatchSample2.ViolentStringMatcher();
        Method methodCall =  currentClass.getClass().getDeclaredMethod("indexOf",String.class,String.class);
        // int[] a = {9, 3, 2, 6, 8, 7, 1, 12, 4, 11};
        methodCall.invoke(currentClass,Config.StringMatchSample.source,Config.StringMatchSample.pattern);



    }
}