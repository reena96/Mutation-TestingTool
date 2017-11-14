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
    tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass;
    Method currentMethod;
    String pattern;
    String sequence;
    String i;


    //MUTATED_FILE_PATH,currentClass,currentMethod,Config.StringMatchSample.sequence[i], Config.StringMatchSample.pattern[i], i+""

    public MutationThread( String MUTATED_FILE_PATH,tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass, Method currentMethod, String sequence, String pattern, String i) {
        this.MUTATED_FILE_PATH = MUTATED_FILE_PATH;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.sequence = sequence;
        this.pattern = pattern;
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println("----------------------------------------------------------------------");
        try {
            File file = new File(MUTATED_FILE_PATH + i);
            TracePrint tracePrint = new TracePrint(MUTATED_FILE_PATH,sequence,pattern,i);
            tracePrint.printToFile(file, currentMethod, currentClass, sequence, pattern);
            compareTraces(file,i);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void compareTraces(File mutatedTrace,String i) throws IOException {

        String[] INPUT_FILE_PATH = {"./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_0","./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_1","./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_2"};

        int Int_i = Integer.parseInt(i);
        File originalTrace = new File(INPUT_FILE_PATH[Int_i]);
        boolean compareResult = FileUtils.contentEquals(mutatedTrace, originalTrace);
        if(compareResult == true) {
            System.out.println("Bad news: SAME TRACES- Mutant has survived!");
        }
        else{
            System.out.println("Good news: DIFFERENT TRACES -Mutant is killed!");}
    }
}