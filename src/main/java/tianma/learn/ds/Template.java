package tianma.learn.ds;

import java.util.Map;

//Map<String,String> myMap = new HashMap<String,String>();
public class Template {
    public static void instrum(String lineNumber, String statement_type, Object ... objects)
    {
        String complete_objects_list="";
        for (int i = 0; i < objects.length; ++i) {
            complete_objects_list+=" , "+objects[i].toString();
        }
        System.out.println("Line Number: "+lineNumber+" Instrumented Statement Type : "+statement_type+"  "+complete_objects_list);
    }
}
