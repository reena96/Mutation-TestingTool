package tianma.learn.ds.Launcher;

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

public class TracePrint implements Runnable{
    String INPUT_FILE_PATH;
    String pattern;
    String sequence;
    String i;

    public TracePrint(String INPUT_FILE_PATH,String sequence, String pattern, String i) {
        this.INPUT_FILE_PATH = INPUT_FILE_PATH;
        this.sequence = sequence;
        this.pattern = pattern;
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
            File file = new File(INPUT_FILE_PATH + i);
            //synchronized (this) {
            printToFile(file, methodCall, currentClass, sequence, pattern);
            //}

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printToFile(File file, Method methodCall, StringMatchSample_1.ViolentStringMatcher currentClass, String sequence, String pattern) throws IOException, InvocationTargetException, IllegalAccessException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!

            PrintStream old = System.out;
            // Tell Java to use your special stream
            System.setOut(ps);
            // Print some output: goes to your special stream
            methodCall.invoke(currentClass, sequence, pattern);
            // Put things back
            System.out.flush();
            System.setOut(old);
            // Show what happened
            System.out.println(baos.toString());
            String b = baos.toString();

            FileUtils.writeStringToFile(file, b);
            //addToList();
    }

    private void addToList() throws IOException {
        BufferedReader lineReader = new BufferedReader(new FileReader(INPUT_FILE_PATH+i));
        String[] values = new String[2];
        //LineNumberReader lineReader =
        //        new LineNumberReader(new FileReader(INPUT_FILE_PATH));
        String text = "";
        String expression = "";
        int key;
        int c = 0;
        HashMap<Integer,String[]> map=new HashMap<Integer, String[]>();
        while((text = lineReader.readLine()) != null)
        {
            c = c + 1;
            String[] tokens = text.split(" , ");
            key = Integer.parseInt(tokens[0]);
            values[0] = tokens[1];
            values[1] = tokens[2];
            //System.out.println("------------"+values[1]);
            map.put(key , values);

        }
        for ( Integer keyVal  :map.keySet()){
            System.out.println(map.get(keyVal));
        }

        System.out.println("Size of Map------------------------------------------------"+map.size());




    }
}
