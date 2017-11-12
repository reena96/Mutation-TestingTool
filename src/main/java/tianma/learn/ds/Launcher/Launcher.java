package tianma.learn.ds.Launcher;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import tianma.learn.ds.AST.ASTTreeParse;

import java.io.File;
import java.io.IOException;

public class Launcher {

    public static void main(String[] args) throws IOException, BadLocationException {

        File file = new File("./src/main/java/tianma/learn/ds/string/main/StringMatchSample_1.java");
        Launcher launcher = new Launcher();
        launcher.mutate(file);

    }

    public void mutate(File file) throws IOException, BadLocationException {

        ASTTreeParse astTreeParse = new ASTTreeParse();
        CompilationUnit unit = astTreeParse.getCompilationUnit(file);

        String operatorToReplace = "<";
        String expression = "i < sLen && j < pLen";
        String statement_type = "WhileStatement";
        String method_name = "tianma.learn.ds.string.main.StringMatchSample_1.StringMatcher";


        AST ast = unit.getAST();

        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {

                String methodDeclaration_name =  methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName();
                if(methodDeclaration_name.equals(method_name)) {

                    if(statement_type.equals("WhileStatement")) {
                        methodDeclaration.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(WhileStatement whileStatement) {
                                if (whileStatement.getExpression().toString().equals(expression)) {
                                    String new_expression = expression.replaceFirst("<", ">");
                                    String new_while_statement = whileStatement.toString().replace(expression, new_expression);
                                    System.out.println(new_while_statement);
                                }
                                return super.visit(whileStatement);
                            }

                        });
                    }
                }
                
                return super.visit(methodDeclaration);
            }
        });
    }
}
