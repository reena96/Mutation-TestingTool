package tianma.learn.ds.AST;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class MyJavaParser {
    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("src/main/java/tianma/learn/ds/AST/JavaParserTest.java");

        // parse the file
        CompilationUnit cu = JavaParser.parse(in);

        // visit and print the methods names
        cu.accept(new MethodVisitor(), null);

        cu.getTypes().forEach(type ->
                type.getMethods().forEach(method -> {
                    System.out.println(method.getName());
                    // Make the visitor go through everything inside the method.
                    method.accept(new MethodVisitor(), null);
                })
        );
        for (String s: MethodVisitor.names
             ) {

            System.out.println(s);

        }
        // cu.accept(new MethodVisitor(), null);

        // prints the resulting compilation unit to default system output
        //System.out.println(cu.toString());
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes.
     */
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {

        public static List<String> names = new ArrayList<>();

        @Override
        public void visit(SimpleName n, Void arg) {
            names.add(n.toString());
            //System.out.println(n);
            super.visit(n, arg);
        }

        /*@Override
        public void visit(MethodCallExpr n, Void arg) {
            System.out.println(n.getScope());
            super.visit(n, arg);
        }*/

        /*@Override
        public void visit(MethodDeclaration n, Void arg) {
            //here you can access the attributes of the method.
            //this method will be called for all methods in this
            //CompilationUnit, including inner class methods
            System.out.println(n.getName());
            super.visit(n, arg);
        }

        @Override
        public void visit(SimpleName n, Void arg) {
            System.out.println(n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ExpressionStmt n, Void arg) {

            System.out.println(n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ForStmt n, Void arg) {

            System.out.println(n);
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Void arg) {

            System.out.println(n);
            super.visit(n, arg);
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            //here you can access the attributes of the method.
            //this method will be called for all methods in this
            //CompilationUnit, including inner class methods

            //System.out.println(n.getName());
            super.visit(n, arg);
        }

        @Override
        public void visit(ExpressionStmt n, Void arg) {

            System.out.println(n+"-------");
            List<Node> child_nodes = n.getChildNodes();
            for (Node node : child_nodes) {
                visit(, null);
            }
            //n.accept(new SimpleNameVisitor(), null);


            super.visit(n, arg);
        }*/
/*n.accept(new GenericVisitorAdapter<Node, Void>() {
                @Override
                public Node visit(SimpleName n, Void arg) {
                    System.out.println(n);
                    super.visit(n, arg);
                    return n;
                }
            }, null);*/
        /*private class SimpleNameVisitor extends VoidVisitorAdapter {

            @Override
            public void visit(SimpleName n, Object arg) {
                System.out.println(n);
                super.visit(n, arg);
            }
        }*/
    }
}
