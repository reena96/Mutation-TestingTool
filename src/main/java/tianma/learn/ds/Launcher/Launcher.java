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


        launcher.astMutate(file);

    }

    public void astMutate(File file) throws IOException, BadLocationException {

        ASTTreeParse astTreeParse = new ASTTreeParse();
        CompilationUnit unit = astTreeParse.getCompilationUnit(file);

        String operatorToFind = "\\+\\+";
        String operatorToReplace = "--";
        String expression = "i++";
        String statement_type = "PostfixExpression";
        String method_name = "tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf";


        AST ast = unit.getAST();

        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {

                String methodDeclaration_name = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName() + "." + methodDeclaration.getName();
                if (methodDeclaration_name.equals(method_name)) {

                    if (statement_type.equals("WhileStatement")) {
                        methodDeclaration.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(WhileStatement whileStatement) {
                                if (whileStatement.getExpression().toString().equals(expression)) {
                                    String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                    String new_while_statement = whileStatement.toString().replace(expression, new_expression);
                                    String old = methodDeclaration.toString();
                                    String new_method = old.replace(whileStatement.toString(), new_while_statement);
                                    saveNewMethod(new_method);
                                }
                                return super.visit(whileStatement);
                            }
                        });
                    }
                    if (statement_type.equals("IfStatement")) {
                        methodDeclaration.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(IfStatement ifStatement) {
                                if (ifStatement.getExpression().toString().equals(expression)) {
                                    String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                    String new_if_statement = ifStatement.toString().replace(expression, new_expression);
                                    String old = methodDeclaration.toString();
                                    String new_method = old.replace(ifStatement.toString(), new_if_statement);
                                    saveNewMethod(new_method);
                                }
                                return super.visit(ifStatement);
                            }
                        });
                    }
                    if (statement_type.equals("Assignment")) {
                        methodDeclaration.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(ExpressionStatement exp) {
                                if (exp.getExpression().toString().equals(expression)) {
                                    String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                    String new_ass_statement = exp.toString().replace(expression, new_expression);
                                    String old = methodDeclaration.toString();
                                    String new_method = old.replace(exp.toString(), new_ass_statement);
                                    saveNewMethod(new_method);
                                }
                                return super.visit(exp);
                            }
                        });
                    }
                    if (statement_type.equals("PostfixExpression")) {
                        methodDeclaration.accept(new ASTVisitor() {
                            @Override
                            public boolean visit(ExpressionStatement exp) {
                                String exp_class = exp.getExpression().getClass().toString().substring(exp.getClass().toString().lastIndexOf('.') + 1);
                                if (exp_class.equals("PostfixExpression")) {
                                    if (exp.getExpression().toString().equals(expression)) {
                                        String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                        String new_post_statement = exp.toString().replace(expression, new_expression);
                                        String old = methodDeclaration.toString();
                                        String new_method = old.replace(exp.toString(), new_post_statement);
                                        saveNewMethod(new_method);
                                    }
                                }
                                return super.visit(exp);
                            }
                        });
                    }
                }
                return super.visit(methodDeclaration);
            }

        });
    }

    private void saveNewMethod(String new_method) {
        String mutated_method = "";
        mutated_method = new_method;
        System.out.println(mutated_method);
    }
}
