package tianma.learn.ds.Launcher;

import java.io.*;
import java.util.*;

public class Mutator {

    static List<String> totLines= new ArrayList<String>();
     HashMap<Integer, List<String>> map_final = new HashMap<Integer, List<String>>();
    private static String s;
    int i=0;

    public  Mutator(File in) {


        try {
            BufferedReader br = new BufferedReader(new FileReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                totLines.add(line);
            }
            br.close();
        }
        catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        catch (IOException e) {

            e.printStackTrace();
        }

    }

    public HashMap<Integer, List<String>> getMap_final() {



        String[] str;
        HashMap<String, String> map = new HashMap<String, String>();

        System.out.println("-------------------------");
        for (String s : totLines) {
            str=s.split(",");
            String k=str[0]+"@@"+str[1]+"@@"+str[2]+"@@"+str[3];
            System.out.println(k);
            map.put(k,str[4]);




        }



        for (Map.Entry<String,String> entry : map.entrySet()) {


            String key = entry.getKey();
            String value = entry.getValue();
            List<String> list=new ArrayList<String>();
            list.addAll(Arrays.asList(key.split("@@")));


            // do stuff

            for (String op :(value.split("@"))
                    ) {


                switch (op)
                {
                    case "<":
                        s="<=";
                        System.out.println("< <=");
                        break;
                    case ">":
                        s=">=";

                        System.out.println("< <=");
                        break;
                    case "==":
                        s="!=";
                        System.out.println("== !=");
                        break;

                    case "!=":
                        s="==";
                        System.out.println("!= ==");
                        break;
                    case "=":
                        s="+=";
                        break;
                    default:
                        s="default";
                        System.out.println("default");

                }
                list.add(op);
                list.add(s);
                map_final.put(i,list);
                i++;


            }

        }
    return map_final;
    }

}
