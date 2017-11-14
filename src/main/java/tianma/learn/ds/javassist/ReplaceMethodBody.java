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
    String FilePath = "tianma.learn.ds.string.main.StringMatchSample_1$ViolentStringMatcher";
    String MUTATED_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/MutatedTrace";


    public static void main(String args[]) throws Throwable {

        String mm = "public int indexOf(String source, String pattern) {\n" +
                "            int i = 0, j = 0;\n" +
                "            int sLen = source.length(), pLen = pattern.length();\n" +
                "            char[] src = source.toCharArray();\n" +
                "            char[] ptn = pattern.toCharArray();\n" +
                "            while (i < sLen && j < pLen) {\n" +
                "                template.instrum(\n" +
                "                        \"46\",\n" +
                "                        \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                        \"WhileStatement\", \"i < sLen && j < pLen\", \"&&@<@<\");\n" +
                "                if (src[i] != ptn[j]) {\n" + //changed to not equal
                "                    template.instrum(\n" +
                "                            \"47\",\n" +
                "                            \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                            \"IfStatement\", \"src[i] == ptn[j]\", \"==\");\n" +
                "                    // 如果当前字符匹配成功,则将两者各自增1,继续比较后面的字符\n" +
                "                    i++;\n" +
                "                    template.instrum(\n" +
                "                            \"49\",\n" +
                "                            \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                            \"PostfixExpression\", \"i++\", \"++\");\n" +
                "                    j++;\n" +
                "                    template.instrum(\n" +
                "                            \"50\",\n" +
                "                            \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                            \"PostfixExpression\", \"j++\", \"++\");\n" +
                "                } else {\n" +
                "                    // 如果当前字符匹配不成功,则i回溯到此次匹配最开始的位置+1处,也就是i = i - j + 1\n" +
                "                    // (因为i,j是同步增长的), j = 0;\n" +
                "                    i = i - j + 1;\n" +
                "                    template.instrum(\n" +
                "                            \"54\",\n" +
                "                            \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                            \"Assignment\", \"i=i - j + 1\", \"+@-@+@-=\");\n" +
                "                    j = 0;\n" +
                "                    template.instrum(\n" +
                "                            \"55\",\n" +
                "                            \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                            \"Assignment\", \"j=0\", \"@=\");\n" +
                "                }\n" +
                "            }\n" +
                "            // 匹配成功,则返回模式字符串在原字符串中首次出现的位置;否则返回-1\n" +
                "            if (j == pLen) {\n" +
                "                template.instrum(\n" +
                "                        \"59\",\n" +
                "                        \"tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf\",\n" +
                "                        \"IfStatement\", \"j == pLen\", \"==\");\n" +
                "                return i - j;\n" +
                "            } else {\n" +
                "                return -1;\n" +
                "            }\n" +
                "        }";
        ReplaceMethodBody rmb = new ReplaceMethodBody();
        rmb.createMutation1(mm);


    }

    public void createMutation1(String new_method) throws NotFoundException, CannotCompileException, IOException, NoSuchMethodException {
        String mutated_code_str = new_method;
        /*"public int indexOf(String sequence, String pattern) {\n" +
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
                "                if (src[i] != ptn[j]) {\n" +
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
                "        }";*/

        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(FilePath);
        CtMethod cm = cc.getDeclaredMethod("indexOf");
        cc.removeMethod(cm);
        CtMethod newcm = CtMethod.make(mutated_code_str,cc);
        cc.addMethod(newcm);
        cc.writeFile();
        cc.toClass();

        tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher currentClass = new tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher();
        Method currentMethod =  currentClass.getClass().getDeclaredMethod("indexOf", String.class, String.class);

        if(cc.isFrozen()){
            cc.defrost();
            cc.detach();
        }

        ExecutorService execService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < Config.StringMatchSample.sequence.length; i++) {
            execService.execute(new MutationThread(MUTATED_FILE_PATH,currentClass,currentMethod,Config.StringMatchSample.sequence[i], Config.StringMatchSample.pattern[i], i+""));
        }
        execService.shutdown();
    }


}