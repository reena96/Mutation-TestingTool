package tianma.learn.ds;

import java.util.Map;

//Map<String,String> myMap = new HashMap<String,String>();
public class Template {
    public static void instrum(String lineNumber, String methodDeclaration_name, String statement_type, String expression, String operatorList, Object ... objects)
    {
        String complete_objects_list="";
        for (int i = 0; i < objects.length; ++i) {
            complete_objects_list+=" , "+objects[i].toString();
        }
        System.out.println(""+lineNumber+","+methodDeclaration_name+","+statement_type+","+expression+","+operatorList+","+complete_objects_list);
    }
}
