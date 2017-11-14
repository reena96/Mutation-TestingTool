package tianma.learn.ds.javassist;


import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;

import static javassist.ClassPool.getDefault;

public class ByteCodeInjector {

    public static final String CLASS_NAME = "HelloWorld";
    public static final String METHOD = "sayHello";

    public static void main(String[] argos) throws Exception{
        modifyByteCode(CLASS_NAME,METHOD);
    }

    public static void modifyByteCode(String className ,String methodName)
            throws NotFoundException, CannotCompileException, IOException {
        //Get the Class implementation byte code
        CtClass ctClass = getDefault().get(className);

        //Get the method from the Class byte code
        CtMethod method= ctClass.getDeclaredMethod(methodName);

        /**
         * Creating the new Method implementation
         */
        StringBuffer content = new StringBuffer();
        content.append("{\n System.out.println(\"Hello!!! This is the Modified version!!!\");\n } ");

        /**
         * Inserting the content
         */
        method.setBody(content.toString());
        System.out.println("Replacing Method \' sayHello\' s body with new implementation : " + content);
        //modify the actual class File
        ctClass.writeFile();
    }
}