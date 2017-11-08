package tianma.learn.ds.AST;

import com.sun.javafx.css.Declaration;
import org.eclipse.jdt.core.dom.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ASTVisitorClass extends ASTVisitor {

    HashMap<ASTNode,String> hm = new HashMap<ASTNode, String>();
    HashMap<String,String> table = new HashMap<String, String>();

    @Override
    public boolean visit(TypeDeclaration node) {

        //ITypeBinding binding = node.resolveBinding();
        //System.out.println("\n"+ node.toString());
        hm.put(node,node.getName().toString());
        table.put(node.getName().toString(),"");
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {

        //System.out.println("Declaring Class: " + node.resolveBinding().getDeclaringClass());
        //System.out.println("Default Class: " + node.resolveBinding().getDefaultValue());
        //System.out.println("getMethodDeclaration : " + node.resolveBinding().getMethodDeclaration());
        //System.out.println("IVariableBinding: " + node.resolveBinding());

        //System.out.println("\n"+node.toString());
        //IMethodBinding b = node.resolveBinding();
        //System.out.println("BODY: " + node.getBody().toString());

        //Block body = node.getBody();
        //System.out.println("Binding: " + b.toString());

        //System.out.println("TypeArguments"+b.getTypeArguments());
        //System.out.println("TypeParameters"+b.getTypeParameters());
        //System.out.println(b.getMethodDeclaration().toString());
        hm.put(node,node.getName().toString());
        table.put(node.getName().toString(),"");
        return super.visit(node);
    }

    public boolean visit(VariableDeclarationFragment node) {

        System.out.println(node.resolveBinding().getDeclaringMethod()+"DECLARING METHOD");
        //System.out.println("Variable: " + node.getName() + ", In Method: " + method.getName() + "' Method line ");// cu.getLineNumber(method.getStartPosition()));
        //System.out.println("Variable: " + node.getName() + "' Method line ");// cu.getLineNumber(method.getStartPosition()));

        //SimpleName name = node.getName();
        //System.out.println("Declaration of '"+name+"' at line");
        //cu.getLineNumber(name.getStartPosition()));

        int modifiers = 0;
        if (node.getParent() instanceof FieldDeclaration){
            modifiers = ((FieldDeclaration)node.getParent()).getModifiers();
            for(Map.Entry m:hm.entrySet()){
                System.out.println(m.getKey()+"LETS CHECK KEY");
                hm.put((ASTNode)m.getKey(),node.getName().toString());
            }
            for(Map.Entry m:table.entrySet()){
                System.out.println(m.getKey()+"LETS CHECK KEY");
                table.put(m.getKey().toString(),node.getName().toString());
            }
        }
        else if (node.getParent() instanceof VariableDeclarationStatement){
            modifiers = ((VariableDeclarationStatement)node.getParent()).getModifiers();
        }
        System.out.println("VARIABLE: "+ node.toString()+ " MODIFIER: "+ modifiers);

        /*
        Multimap<Integer, String> multimap = ArrayListMultimap.create();
        multimap.put(1, var.getName()); // variable name
        multimap.put(1, cu.getLineNumber(method.getStartPosition())); // method line
        multimap.put(1, method.getName()); // method name
        */
        //Then what should i do if found variable name, method line and method name more than one in the Multimap?


        /*public boolean visit(VariableDeclarationFragment node) {
            SimpleName name = node.getName();
            System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));

            int modifiers = 0;
            if (node.getParent() instanceof FieldDeclaration){
                modifiers = ((FieldDeclaration)node.getParent()).getModifiers();
            }
            else if (node.getParent() instanceof VariableDeclarationStatement){
                modifiers = ((VariableDeclarationStatement)node.getParent()).getModifiers();
            }
            return false;
        }*/

        return false;
    }

    public void printTable() {
        for(Map.Entry m:hm.entrySet()){
            System.out.println("SCOPE: "+m.getKey()+"  VARIABLES:  "+m.getValue());
        }
        for(Map.Entry m:table.entrySet()){
            System.out.println("SCOPE NAME: "+m.getKey()+"  VARIABLES:  "+m.getValue());
        }
    }
    /*
    @Override
    public boolean visit(VariableDeclarationFragment node) {

        SimpleName name = node.getName();
        System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));

        int modifiers = 0;
        if (node.getParent() instanceof FieldDeclaration){
            modifiers = ((FieldDeclaration)node.getParent()).getModifiers();
        }
        else if (node.getParent() instanceof VariableDeclarationStatement){
            modifiers = ((VariableDeclarationStatement)node.getParent()).getModifiers();
        }
        return false;

        //System.out.println("Declaring Class: " + node.resolveBinding().getDeclaringClass());
        //System.out.println("Default Class: " + node.resolveBinding().getDefaultValue());
        //System.out.println("getMethodDeclaration : " + node.resolveBinding().getMethodDeclaration());
        //System.out.println("IVariableBinding: " + node.resolveBinding());
        //IMethodBinding b = node.resolveBinding();
        //System.out.println("Binding: "+b.toString());
        //b.get

        //System.out.println(node+"");
        System.out.println(node.toString());

        //System.out.println(b.getMethodDeclaration().toString());




        for (Iterator iter = node.fragments().iterator(); iter.hasNext();) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) iter.next();

            // VariableDeclarationFragment: is the plain variable declaration
            // part. Example:
            // "int x=0, y=0;" contains two VariableDeclarationFragments, "x=0"
            // and "y=0"

            IVariableBinding b = fragment.resolveBinding();
            String s = b.getName();
            System.out.println(s);
            /*VariableBindingManager manager = new VariableBindingManager(
                    fragment); // create the manager fro the fragment
            localVariableManagers.put(binding, manager);
            manager.variableInitialized(fragment.getInitializer());*//*
            // first assignment is the initalizer
        }

        return super.visit(node);
    }*/
}
