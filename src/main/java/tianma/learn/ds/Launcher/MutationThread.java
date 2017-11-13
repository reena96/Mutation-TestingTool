package tianma.learn.ds.Launcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.*;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.io.FileUtils;
import tianma.learn.ds.string.main.StringMatchSample_1;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MutationThread implements Runnable{
    String MUTATED_FILE_PATH;
    String method_code_str;
    String pattern;
    String sequence;
    String i;

    public MutationThread(String INPUT_FILE_PATH,String method_code_str, String sequence, String pattern, String i) {
        this.MUTATED_FILE_PATH = INPUT_FILE_PATH;
        this.method_code_str = method_code_str;
        this.sequence = sequence;
        this.pattern = pattern;
        this.i = i;
    }

    @Override
    public void run() {

System.out.println("------------------");
        try {
            File file = new File(MUTATED_FILE_PATH + i);
            String FilePath = "tianma.learn.ds.string.main.StringMatchSample_1$ViolentStringMatcher";

            ClassPool cp = ClassPool.getDefault();

            CtClass cc = cp.get(FilePath);
            CtMethod cm = cc.getDeclaredMethod("indexOf");
            cc.removeMethod(cm);
            CtMethod newcm = CtMethod.make(method_code_str,cc);
            cc.addMethod(newcm);
            cc.writeFile();
            cc.toClass();


            tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass = new tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher();
            Method currentMethod =  currentClass.getClass().getDeclaredMethod("indexOf", String.class, String.class);
            TracePrint tracePrint = new TracePrint(MUTATED_FILE_PATH,sequence,pattern,i);
            currentMethod.invoke(currentClass,sequence,pattern);
            tracePrint.printToFile(file, currentMethod, currentClass, sequence, pattern);
            compareTraces(file);
            if(cc.isFrozen()){
                cc.defrost();
                cc.detach();
            }


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


    private static void compareTraces(File mutatedTrace) throws IOException {
        String INPUT_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_1";
        File originalTrace = new File(INPUT_FILE_PATH);
        boolean compareResult = FileUtils.contentEquals(mutatedTrace, originalTrace);
        if(compareResult == true)
            System.out.println("Bad news: SAME TRACES- Mutant has survived!");
        else
            System.out.println("Good news: DIFFERENT TRACES -Mutant is killed!");

    }


}

