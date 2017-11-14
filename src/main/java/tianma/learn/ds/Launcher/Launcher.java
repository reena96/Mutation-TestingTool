package tianma.learn.ds.Launcher;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import tianma.learn.ds.AST.ASTTreeParse;
import tianma.learn.ds.javassist.ReplaceMethodBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Launcher {

    public static String method = "";

    public static int key_lock=0;

    public static void main(String[] args) throws IOException, BadLocationException, NoSuchMethodException, CannotCompileException, NotFoundException {

        File file = new File("./src/main/java/tianma/learn/ds/string/main/StringMatchSample_1.java");

        String INPUT_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_";

        /*ExecutorService execService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < Config.StringMatchSample.sequence.length; i++) {

            execService.execute(new TracePrint(INPUT_FILE_PATH, Config.StringMatchSample.sequence[i], Config.StringMatchSample.pattern[i], i+""));
        }
        execService.shutdown();*/
        File traceFile = new File("./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_0");
        Mutator mutator = new Mutator(traceFile);
        HashMap<Integer, List<String>> mut = mutator.getMap_final();

        Launcher launcher = new Launcher();
        launcher.astMutate(file, mut);

        ReplaceMethodBody rmp = new ReplaceMethodBody();
        rmp.createMutation1(method.replace("@Override ",""));

        //MutationThread mt = new MutationThread();
    }

    /*public void setOldMethod(String main_method_name, File file) throws IOException, BadLocationException {

        ASTTreeParse astTreeParse = new ASTTreeParse();
        CompilationUnit unit = astTreeParse.getCompilationUnit(file);

        AST ast = unit.getAST();
        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {

                String methodDeclaration_name = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName() + "." + methodDeclaration.getName();
                if (methodDeclaration_name.equals(main_method_name)) {

                }
                return super.visit(methodDeclaration);
            }
        });
    } */

    public void astMutate(File file, HashMap<Integer, List<String>> mut) throws IOException, BadLocationException {


        ASTTreeParse astTreeParse = new ASTTreeParse();
        CompilationUnit unit = astTreeParse.getCompilationUnit(file);

        AST ast = unit.getAST();

        for (Map.Entry<Integer, List<String>> entry : mut.entrySet()) {

            int key = entry.getKey();
            List<String> values_list = entry.getValue();
            //String line_number = values_list.get(0);
            String method_name = values_list.get(1);
            String statement_type = values_list.get(2);
            String expression = values_list.get(3);
            String operatorToFind = values_list.get(4);
            String operatorToReplace = values_list.get(5);

            unit.accept(new ASTVisitor() {

                @Override
                public boolean visit(MethodDeclaration methodDeclaration) {
                    if (key_lock == 0 || key_lock==1) {
                        saveNewMethod(methodDeclaration.toString());
                        key_lock++;
                    }
                    String methodDeclaration_name = methodDeclaration.resolveBinding().getDeclaringClass().getQualifiedName() + "." + methodDeclaration.getName();
                    if (methodDeclaration_name.toString().equals(method_name.toString())) {

                        if (statement_type.equals("WhileStatement")) {
                            methodDeclaration.accept(new ASTVisitor() {
                                @Override
                                public boolean visit(WhileStatement whileStatement) {
                                    if (whileStatement.getExpression().toString().equals(expression)) {
                                        String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                        String new_while_statement = whileStatement.toString().replace(expression, new_expression);
                                        String old = method;
                                        String new_method = old.replace(expression, new_expression);
                                        saveNewMethod(new_method);
                                    }
                                    return super.visit(whileStatement);
                                }
                            });
                        } else if (statement_type.equals("IfStatement")) {
                            methodDeclaration.accept(new ASTVisitor() {
                                @Override
                                public boolean visit(IfStatement ifStatement) {
                                    if (ifStatement.getExpression().toString().equals(expression)) {
                                        String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                        String new_if_statement = ifStatement.toString().replace(expression, new_expression);
                                        String old = method;
                                        String new_method = old.replace(expression, new_expression);
                                        saveNewMethod(new_method);
                                    }
                                    return super.visit(ifStatement);
                                }
                            });
                        } else if (statement_type.equals("Assignment")) {
                            methodDeclaration.accept(new ASTVisitor() {
                                @Override
                                public boolean visit(ExpressionStatement exp) {
                                    if (exp.getExpression().toString().equals(expression)) {
                                        String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                        String new_ass_statement = exp.toString().replace(expression, new_expression);
                                        String old = method;
                                        String new_method = old.replace(expression, new_expression);
                                        saveNewMethod(new_method);
                                    }
                                    return super.visit(exp);
                                }
                            });
                        } else if (statement_type.equals("PostfixExpression")) {
                            methodDeclaration.accept(new ASTVisitor() {
                                @Override
                                public boolean visit(ExpressionStatement exp) {
                                    String exp_class = exp.getExpression().getClass().toString().substring(exp.getClass().toString().lastIndexOf('.') + 1);
                                    if (exp_class.equals("PostfixExpression")) {
                                        if (exp.getExpression().toString().equals(expression)) {
                                            String new_expression = expression.replaceFirst(operatorToFind, operatorToReplace);
                                            String new_post_statement = exp.toString().replace(expression, new_expression);
                                            String old = method;
                                            String new_method = old.replace(expression, new_expression);
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

        /*String operatorToFind = "\\+\\+";
        String operatorToReplace = "--";
        String expression = "i++";
        String statement_type = "PostfixExpression";
        String method_name = "tianma.learn.ds.string.main.StringMatchSample_1.ViolentStringMatcher.indexOf";*/
    }


    private void saveNewMethod(String new_method) {
        method = new_method;
        System.out.println(method);
    }
}
