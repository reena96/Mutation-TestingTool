package tianma.learn.ds.javassist;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.*;

import javassist.*;
import org.apache.commons.io.FileUtils;
import sun.misc.Launcher;
import tianma.learn.ds.Launcher.Config;
import tianma.learn.ds.Launcher.MutationThread;
import tianma.learn.ds.Launcher.TracePrint;
import tianma.learn.ds.string.main.StringMatchSample;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import static sun.tracing.dtrace.JVM.defineClass;


public class ReplaceMethodBody{

    public static void main(String args[]) throws Throwable {

        String str1 = "public int indexOf(String sequence, String pattern) {\n" +
                "\n" +
                "            tianma.learn.ds.Launcher.Template template = new tianma.learn.ds.Launcher.Template();\n" +
                "\n" +
                "            int i = 0, j = 0;\n" +
                "            int sLen = sequence.length(), pLen = pattern.length();\n" +
                "            char[] src = sequence.toCharArray();\n" +
                "            char[] ptn = pattern.toCharArray();\n" +
                "            while (i < sLen && j < pLen) {\n" +
                "                template.instrum(\"46\", \"WhileStatement\",\n" +
                "                        \"i < sLen && j < pLen\");\n" +
                "                if (src[i] == ptn[j]) {\n" +
                "                    template.instrum(\"47\", \"IfStatement\", \"src[i] == ptn[j]\");\n" +
                "                    // 如果当前字符匹配成功,则将两者各自增1,继续比较后面的字符\n" +
                "                    i++;\n" +
                "                    template.instrum(\"49\", \"PostfixExpression\", \"i++\");\n" +
                "                    j++;\n" +
                "                    template.instrum(\"50\", \"PostfixExpression\", \"j++\");\n" +
                "                } else {\n" +
                "                    // 如果当前字符匹配不成功,则i回溯到此次匹配最开始的位置+1处,也就是i = i - j + 1\n" +
                "                    // (因为i,j是同步增长的), j = 0;\n" +
                "                    i = i - j + 1;\n" +
                "                    template.instrum(\"54\", \"Assignment\", \"i=i - j + 1\");\n" +
                "                    j = 0;\n" +
                "                    template.instrum(\"55\", \"Assignment\", \"j=0\");\n" +
                "                }\n" +
                "            }\n" +
                "            // 匹配成功,则返回模式字符串在原字符串中首次出现的位置;否则返回-1\n" +
                "            if (j == pLen){\n" + "System.out.println(i-j);"+
                "                return i - j;\n}" +
                "            else {\n" + "System.out.println(i-j);"+
                "                return -1;}\n" +
                "        }";

        String str2 = "public int indexOf(String sequence, String pattern) {\n" +
                "\n" +
                "            tianma.learn.ds.Launcher.Template template = new tianma.learn.ds.Launcher.Template();\n" +
                "\n" +
                "            int i = 0, j = 0;\n" +
                "            int sLen = sequence.length(), pLen = pattern.length();\n" +
                "            char[] src = sequence.toCharArray();\n" +
                "            char[] ptn = pattern.toCharArray();\n" +
                "            while (i < sLen || j < pLen) {\n" +
                "                template.instrum(\"46\", \"WhileStatement\",\n" +
                "                        \"i < sLen && j < pLen\");\n" +
                "                if (src[i] != ptn[j]) {\n" +  ////old value  if (src[i] == ptn[j])  --------------------
                "                    template.instrum(\"47\", \"IfStatement\", \"src[i] == ptn[j]\");\n" +
                "                    // 如果当前字符匹配成功,则将两者各自增1,继续比较后面的字符\n" +
                "                    i++;\n" +
                "                    template.instrum(\"49\", \"PostfixExpression\", \"i++\");\n" +
                "                    j++;\n" +
                "                    template.instrum(\"50\", \"PostfixExpression\", \"j++\");\n" +
                "                } else {\n" +
                "                    // 如果当前字符匹配不成功,则i回溯到此次匹配最开始的位置+1处,也就是i = i - j + 1\n" +
                "                    // (因为i,j是同步增长的), j = 0;\n" +
                "                    i = i - j + 1;\n" +
                "                    template.instrum(\"54\", \"Assignment\", \"i=i - j + 1\");\n" +
                "                    j = 0;\n" +
                "                    template.instrum(\"55\", \"Assignment\", \"j=0\");\n" +
                "                }\n" +
                "            }\n" +
                "            // 匹配成功,则返回模式字符串在原字符串中首次出现的位置;否则返回-1\n" +
                "            if (j == pLen){\n" + "System.out.println(i-j);"+
                "                return i - j;\n}" +
                "            else{\n" + "System.out.println(i-j);"+
                "                return -1;}\n" +
                "        }";


        String str3 = "public int indexOf(String sequence, String pattern) {\n" +
                "\n" +
                "            tianma.learn.ds.Launcher.Template template = new tianma.learn.ds.Launcher.Template();\n" +
                "\n" +
                "            int i = 0, j = 0;\n" +
                "            int sLen = sequence.length(), pLen = pattern.length();\n" +
                "            char[] src = sequence.toCharArray();\n" +
                "            char[] ptn = pattern.toCharArray();\n" +
                "            while (i < sLen && j < pLen) {\n" +
                "                template.instrum(\"46\", \"WhileStatement\",\n" +
                "                        \"i < sLen && j < pLen\");\n" +
                "                if (src[i] == ptn[j]) {\n" +
                "                    template.instrum(\"47\", \"IfStatement\", \"src[i] == ptn[j]\");\n" +
                "                    // 如果当前字符匹配成功,则将两者各自增1,继续比较后面的字符\n" +
                "                    i++;\n" +
                "                    template.instrum(\"49\", \"PostfixExpression\", \"i++\");\n" +
                "                    j++;\n" +
                "                    template.instrum(\"50\", \"PostfixExpression\", \"j++\");\n" +
                "                } else {\n" +
                "                    // 如果当前字符匹配不成功,则i回溯到此次匹配最开始的位置+1处,也就是i = i - j + 1\n" +
                "                    // (因为i,j是同步增长的), j = 0;\n" +
                "                    i = i - j + 1;\n" +
                "                    template.instrum(\"54\", \"Assignment\", \"i=i - j + 1\");\n" +
                "                    j = 0;\n" +
                "                    template.instrum(\"55\", \"Assignment\", \"j=0\");\n" +
                "                }\n" +
                "            }\n" +
                "            // 匹配成功,则返回模式字符串在原字符串中首次出现的位置;否则返回-1\n" +
                "            if (j != pLen){\n" + "System.out.println(i-j);"+ //old value-  if (j == pLen)
                "                return i - j;\n}" +
                "            else{\n" + "System.out.println(i-j);"+
                "                return -1;}\n" +
                "        }";
        String[] strArr = {str1,str2,str3};



        String MUTATED_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/MutatedTrace";
        ExecutorService execService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < Config.StringMatchSample.sequence.length; i++) {

            for (int j=0; j<strArr.length; j++ ) {
                execService.execute(new MutationThread(MUTATED_FILE_PATH,strArr[j],Config.StringMatchSample.sequence[i], Config.StringMatchSample.pattern[i], i+""+j));
            }
        }
        execService.shutdown();




    }

/*
    private static void replaceMethodBody(String str) throws NotFoundException, CannotCompileException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        int[] a = {9, 3, 2, 6, 8, 7, 1, 12, 4, 11, 10, 5};
        String FilePath = "tianma.learn.ds.string.main.StringMatchSample_1$ViolentStringMatcher";
        String MUTATED_FILE_PATH1 = "./src/main/java/tianma/learn/ds/Launcher/MutatedTrace1";
        String MUTATED_FILE_PATH2 = "./src/main/java/tianma/learn/ds/Launcher/MutatedTrace2";


        tianma.learn.ds.Launcher.Config.StringMatchSample sms = new tianma.learn.ds.Launcher.Config.StringMatchSample();

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(FilePath);
        CtMethod cm = cc.getDeclaredMethod("indexOf");
        cc.removeMethod(cm);
        CtMethod newcm = CtMethod.make(str,cc);
        cc.addMethod(newcm);
        cc.writeFile();
        cc.toClass();


        tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass = new tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher();
        Method currentMethod =  currentClass.getClass().getDeclaredMethod("indexOf", String.class, String.class);
        TracePrint tracePrint = new TracePrint(MUTATED_FILE_PATH1,sms.sequence[0],sms.pattern[0],"0");
        File file = new File(MUTATED_FILE_PATH1);
        tracePrint.printToFile(file, currentMethod, currentClass, sms.sequence[0],sms.pattern[0]);
        currentMethod.invoke(currentClass,sms.sequence[0],sms.pattern[0]);
        compareTraces(file);
        if(cc.isFrozen()){
            cc.defrost();}
        byte[] bytecode = cc.toBytecode();
        //defineClass(cc, bytecode, 0, bytecode.length);

        cc = cp.getAndRename(FilePath,"tianma.learn.ds.string.main.StringMatchSample_1$ViolentStringMatcher2");
        cm = cc.getDeclaredMethod("indexOf");
        cc.removeMethod(cm);
        //newcm = CtMethod.make(str2,cc);
        cc.addMethod(newcm);
        cc.writeFile();
        cc.toClass();


        currentClass = new tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher();
        currentMethod =  currentClass.getClass().getDeclaredMethod("indexOf", String.class, String.class);
        File file2 = new File(MUTATED_FILE_PATH2);

        tracePrint = new TracePrint(MUTATED_FILE_PATH2,sms.sequence[0],sms.pattern[0],"0");
        tracePrint.printToFile(file2, currentMethod, currentClass, sms.sequence[0],sms.pattern[0]);
        compareTraces(file2);







    }

    private static void compareTraces(File mutatedTrace) throws IOException {
        //String CHECK_FILE = "./src/main/java/tianma/learn/ds/Launcher/CheckFile";
        String INPUT_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_1";
        File originalTrace = new File(INPUT_FILE_PATH);
        //File checkTrace = new File(CHECK_FILE);
        boolean compareResult = FileUtils.contentEquals(mutatedTrace, originalTrace);
        if(compareResult == true)
            System.out.println("Bad news: SAME TRACES- Mutant has survived!");
        else
            System.out.println("Good news: DIFFERENT TRACES -Mutant is killed!");

    }
*/

}


