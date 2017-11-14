package tianma.learn.ds.Launcher;

import java.util.Map;

//Map<String,String> myMap = new HashMap<String,String>();
public class Template {
    public static void instrum(String lineNumber, String methodDeclaration_name, String statement_type, String expression, String operatorList)
    {
        System.out.println(""+lineNumber+","+methodDeclaration_name+","+statement_type+","+expression+","+operatorList);
    }
}
