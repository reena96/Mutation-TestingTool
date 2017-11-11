package tianma.learn.ds.Launcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.*;
import org.apache.commons.io.FileUtils;
import tianma.learn.ds.Launcher.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class TestInvoke {

    public static class TraceGeneratorThread extends Thread {
        String sequence, key;
        int i;
        public TraceGeneratorThread (String sequence, String key, int i) {
            this.sequence = sequence;
            this.key = key;
            this.i = i;
        }

        @Override
        public void run() {

            tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass = new tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher();
            Method methodCall = null;
            try {
                methodCall = currentClass.getClass().getDeclaredMethod("indexOf", String.class, String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {

                File file = new File("./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_"+i);

                //PrintStream out = new PrintStream(new FileOutputStream("ExecutionTrace.txt"));
                //System.setOut(out);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                // IMPORTANT: Save the old System.out!
                PrintStream old = System.out;
                // Tell Java to use your special stream
                System.setOut(ps);
                // Print some output: goes to your special stream
                methodCall.invoke(currentClass, sequence, key);

                // Put things back
                System.out.flush();
                System.setOut(old);
                // Show what happened
                System.out.println(baos.toString());
                String b = baos.toString();
                FileUtils.writeStringToFile(file,b);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // test Javassist
    public static void main(String[] args) throws NotFoundException, CannotCompileException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        /*
        tianma.learn.ds.sort.BubbleSorter2 currentClass = new tianma.learn.ds.sort.BubbleSorter2();
        Method methodCall =  currentClass.getClass().getDeclaredMethod("sort",int[].class);
        // int[] a = {9, 3, 2, 6, 8, 7, 1, 12, 4, 11};
        methodCall.invoke(currentClass,Config.BubbleSorter.input_array);
         */
        for (int i=0; i<Config.StringMatchSample.source.length; i++) {
            TraceGeneratorThread t = new TraceGeneratorThread(Config.StringMatchSample.source[i], Config.StringMatchSample.pattern[i], i);
            t.start();
            System.out.println("Started Thread:" + i);
        }
    }
}