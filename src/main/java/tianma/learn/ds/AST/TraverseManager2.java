package tianma.learn.ds.AST;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.projection.Fragment;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TraverseManager2 {

    CompilationUnit unit;
    HashMap<String, String> table = new HashMap<String, String>();
    MethodDeclaration[] methodDeclarationList;
    FieldDeclaration[] fieldDeclarationList;
    List<VariableDeclarationFragment> globalVariableFragmentList;
    String globalVariables = "";
    String package_name = "";
    String class_name = "";
    //ExpressionStatementVisitor visitor = new ExpressionStatementVisitor();


    public void Instrument2(CompilationUnit unit, File file_new) {
        this.unit = unit;
        unit.accept(new ASTVisitor() {
            @Override
            public boolean visit(PackageDeclaration node) {
                package_name = node.getName().toString();
                return super.visit(node);
            }
        });
        AST ast = unit.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);

        unit.accept(new ASTVisitor() {
            @Override
            public boolean visit(Block block) {

                ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);

                for (Statement statement : (List<Statement>) block.statements()) {


                    if (statement instanceof ExpressionStatement) {
                        statement.accept(new ASTVisitor() {

                            ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                            @Override
                            public boolean visit(ExpressionStatement assignment) {

                                String value[] = assignment.getClass().toString().split("\\.");
                                String statement_type = value[value.length - 1];

                                int l = unit.getLineNumber(assignment.getStartPosition());

                                MethodInvocation methodInvocation = ast.newMethodInvocation();

                                SimpleName qName = ast.newSimpleName("Template");
                                methodInvocation.setExpression(qName);
                                methodInvocation.setName(ast.newSimpleName("instrum"));

                                StringLiteral literal1 = ast.newStringLiteral();
                                StringLiteral literal2 = ast.newStringLiteral();
                                StringLiteral literal3 = ast.newStringLiteral();
                                literal1.setLiteralValue(String.valueOf(l));
                                literal2.setLiteralValue(String.valueOf(statement_type));
                                literal3.setLiteralValue(assignment.toString());
                                methodInvocation.arguments().add(literal1);
                                methodInvocation.arguments().add(literal2);
                                methodInvocation.arguments().add(literal3);

                                System.out.println("------------"+assignment.toString()+"------------");

                                HashMap<String, SimpleName> arguments_list = new HashMap<>();

                                assignment.accept(esv);

                                for (SimpleName s :esv.list) {
                                    arguments_list.put(s.toString(), s);
                                    //-System.out.println(arguments_list.get(s.toString())+"show");
                                }

                                for (Map.Entry<String, SimpleName> argument : arguments_list.entrySet()) {

                                    //System.out.printf("Key : %s and Value: %s %n", argument.getKey(), argument.getValue());
                                    StringLiteral a1 = ast.newStringLiteral();
                                    a1.setLiteralValue(argument.getKey());
                                    methodInvocation.arguments().add(a1);
                                    methodInvocation.arguments().add(argument.getValue());
                                    System.out.println(methodInvocation.toString());
                                }

                                //System.out.println("Statement : "+s);
                                ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
                                //listRewrite.insertAfter(statement1, assignment, null);
                                return super.visit(assignment);
                            }


                            /*@Override
                            public boolean visit(Assignment assignment) {



                                return super.visit(assignment);
                            }*/
                        });
                    }
                }
                return super.visit(block);
            }
        });

        String str = null;
        try {
            str = FileUtils.readFileToString(file_new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = new Document(str);
        TextEdit edits = rewrite.rewriteAST(document, null);
        try {
            edits.apply(document);
            //System.out.println(document.get() );
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.writeStringToFile(file_new, document.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}