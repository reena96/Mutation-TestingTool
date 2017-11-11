/*package tianma.learn.ds.AST;

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

public class TraversalManager {


    public class Scopetable {
        int linenumber;
        String path, scope, variables;
        int quantity;

        public Scopetable(int linenumber, String path, String scope, String variables) {
            this.linenumber = linenumber;
            this.path = path;
            this.scope = scope;
            this.variables = variables;
        }
    }

    HashSet<Scopetable> scope_table = new HashSet<Scopetable>();
    CompilationUnit unit;
    HashMap<String, String> table = new HashMap<String, String>();
    MethodDeclaration[] methodDeclarationList;
    FieldDeclaration[] fieldDeclarationList;
    List<VariableDeclarationFragment> globalVariableFragmentList;
    String globalVariables = "";
    String package_name = "";
    String class_name = "";

    public void calculateVariableScopes(CompilationUnit unit) {

        unit.accept(new ASTVisitor() {
            Set names = new HashSet();

            public boolean visit(VariableDeclarationFragment node) {
                SimpleName name = node.getName();
                this.names.add(name.getIdentifier());
                System.out.println("Declaration of '" + name + "' at line" + unit.getLineNumber(name.getStartPosition()));
                return false; // do not continue
            }

            public boolean visit(SimpleName node) {
                if (this.names.contains(node.getIdentifier())) {
                    System.out.println("Usage of '" + node + "' at line " + unit.getLineNumber(node.getStartPosition()));
                }
                return true;
            }
        });

        this.unit = unit;
        unit.accept(new ASTVisitor() {
            @Override
            public boolean visit(PackageDeclaration node) {
                package_name = node.getName().toString();
                return super.visit(node);
            }
        });
        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(TypeDeclaration node) {

                table.put(package_name + "." + node.getName() + "{}", "");

                methodDeclarationList = node.getMethods();
                for (MethodDeclaration method : methodDeclarationList) {
                    table.put(package_name + "." + method.getName() + "()", "");
                }

                fieldDeclarationList = node.getFields();
                for (FieldDeclaration field : fieldDeclarationList) {
                    globalVariableFragmentList = field.fragments();

                    for (VariableDeclarationFragment fragment : globalVariableFragmentList) {
                        if (globalVariables == "" || globalVariables == null)
                            globalVariables = fragment.getName().getIdentifier();
                        else
                            globalVariables += " , " + fragment.getName().getIdentifier();
                    }
                }

                for (Map.Entry m : table.entrySet()) {
                    if (m.getValue() == "" || m.getValue() == null)
                        table.put(m.getKey().toString(), globalVariables);
                    else
                        table.put(m.getKey().toString(), m.getValue() + " , " + globalVariables);
                }

                for (int i = 0; i < methodDeclarationList.length; i++) {
                    MethodDeclaration method = methodDeclarationList[i];
                    Block block = method.getBody();
                    method.getBody().statements();

                    block.accept(new ASTVisitor() {

                        public boolean visit(VariableDeclarationFragment var) {
                            //System.out.println(var.resolveBinding().getDeclaringMethod());
                            //System.out.println("Variable " + var.getName() + ", in Method " + method.getName() + "' Method line " + unit.getLineNumber(method.getStartPosition()));
                            String key = package_name + "." + method.getName() + "()";
                            //System.out.println(key + " : " + table.get(key));
                            if (table.get(key) == "")
                                table.put(key, var.getName().getIdentifier()); // variable name
                            else
                                table.put(key, table.get(key) + " , " + var.getName().getIdentifier()); // variable name
                            return false;
                        }

                        @Override
                        public boolean visit(IfStatement ifStatement) {
                            ifStatement.accept(new ASTVisitor() {

                                public boolean visit(VariableDeclarationFragment var) {
                                    //System.out.println(var.resolveBinding().getDeclaringMethod());
                                    //System.out.println("Variable " + var.getName() + ", in Method " + method.getName() + "' Method line " + unit.getLineNumber(method.getStartPosition()));
                                    String key = package_name + "." + method.getName() + "() : if(" + ifStatement.getExpression().toString() + ") {}";
                                    //System.out.println(key + " : " + table.get(key));
                                    String value = table.get(key);
                                    if (value == "" || value == null)
                                        table.put(key, var.getName().getIdentifier()); // variable name
                                    else
                                        table.put(key, table.get(key) + " , " + var.getName().getIdentifier()); // variable name
                                    return false;
                                }
                            });
                            return super.visit(ifStatement);
                        }

                        @Override
                        public boolean visit(ForStatement forStatement) {
                            forStatement.accept(new ASTVisitor() {

                                public boolean visit(VariableDeclarationFragment var) {
                                    //System.out.println(var.resolveBinding().getDeclaringMethod());
                                    //System.out.println("Variable " + var.getName() + ", in Method " + method.getName() + "' Method line " + unit.getLineNumber(method.getStartPosition()));
                                    String key = package_name + "." + method.getName() + "() : for(" + forStatement.getExpression().toString() + ") {}";
                                    //System.out.println(key + " : " + table.get(key));
                                    String value = table.get(key);
                                    if (value == "" || value == null)
                                        table.put(key, var.getName().getIdentifier()); // variable name
                                    else
                                        table.put(key, table.get(key) + " , " + var.getName().getIdentifier()); // variable name
                                    return false;
                                }
                            });
                            return super.visit(forStatement);
                        }

                        @Override
                        public boolean visit(WhileStatement whileStatement) {
                            whileStatement.accept(new ASTVisitor() {

                                public boolean visit(VariableDeclarationFragment var) {
                                    //System.out.println(var.resolveBinding().getDeclaringMethod());
                                    //System.out.println("Variable " + var.getName() + ", in Method " + method.getName() + "' Method line " + unit.getLineNumber(method.getStartPosition()));
                                    String key = package_name + "." + method.getName() + " : while(" + whileStatement.getExpression().toString() + ") {}";
                                    //System.out.println(key + " : " + table.get(key));
                                    String value = table.get(key);
                                    if (value == "" || value == null)
                                        table.put(key, var.getName().getIdentifier()); // variable name
                                    else
                                        table.put(key, table.get(key) + " , " + var.getName().getIdentifier()); // variable name
                                    return false;
                                }
                            });
                            return super.visit(whileStatement);
                        }
                    });
                }

                return super.visit(node);
            }
        });
    }

    public void printTable() {
        System.out.println("SCOPE TABLE : \n");
        for (Map.Entry m : table.entrySet()) {
            System.out.println("INSIDE SCOPE : " + m.getKey());
            System.out.println("VARIABLES ARE : " + m.getValue() + "\n");
        }
    }


    public MethodInvocation createInstrumMethodNode(AST ast, int linenumber, String statement_type, String statement) {
        MethodInvocation methodInvocation = ast.newMethodInvocation();

        SimpleName qName = ast.newSimpleName("Template");
        methodInvocation.setExpression(qName);
        methodInvocation.setName(ast.newSimpleName("instrum"));

        StringLiteral literal1 = ast.newStringLiteral();
        StringLiteral literal2 = ast.newStringLiteral();
        StringLiteral literal3 = ast.newStringLiteral();
        literal1.setLiteralValue(linenumber + "");
        literal2.setLiteralValue(String.valueOf(statement_type));
        literal3.setLiteralValue(statement);
        methodInvocation.arguments().add(literal1);
        methodInvocation.arguments().add(literal2);
        methodInvocation.arguments().add(literal3);
        return methodInvocation;
    }

    public MethodInvocation addArguments(AST ast, MethodInvocation methodInvocation, String stringLiteral) {
        StringLiteral stringLiteralNode = ast.newStringLiteral();
        stringLiteralNode.setLiteralValue(stringLiteral);
        methodInvocation.arguments().add(stringLiteralNode);
        SimpleName variableNode = ast.newSimpleName(stringLiteral);
        methodInvocation.arguments().add(variableNode);
        return methodInvocation;
    }

    public void Instrument2(CompilationUnit unit, File file_new) {

        this.unit = unit;
        AST ast = unit.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);

        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(TypeDeclaration node) {
                class_name = node.getName().toString();
                methodDeclarationList = node.getMethods();

                for (int i = 0; i < methodDeclarationList.length; i++) {
                    MethodDeclaration method = methodDeclarationList[i];
                    method.accept(new ASTVisitor() {
                        @Override
                        public boolean visit(Block block) {
                            ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);

                            for (Statement s : (List<Statement>) block.statements()) {

                                if (s instanceof ExpressionStatement) {

                                    s.accept(new ASTVisitor() {
                                        ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                                        @Override
                                        public boolean visit(Assignment assignment) {

                                            String value[] = assignment.getClass().toString().split("\\.");
                                            String statement_type = value[value.length - 1];

                                            int lineNumber = unit.getLineNumber(assignment.getStartPosition());

                                            MethodInvocation methodInvocation = createInstrumMethodNode(ast, lineNumber, statement_type, assignment.toString());

                                            System.out.println("------------" + methodInvocation.toString() + "------------");

                                            HashMap<String, SimpleName> arguments_list = new HashMap<>();

                                            assignment.accept(esv);

                                            for (SimpleName variableName : esv.list) {
                                                arguments_list.put(variableName.toString(), variableName);
                                            }

                                            for (Map.Entry<String, SimpleName> argument : arguments_list.entrySet()) {

                                                methodInvocation = addArguments(ast, methodInvocation, argument.getKey());

                                            }
                                            System.out.println(methodInvocation.toString());
                                            ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
                                            listRewrite.insertAfter(statement1, s, null);

                                            return super.visit(assignment);
                                        }
                                    });
                                    if (((ExpressionStatement) s).getExpression().toString().contains("System.out.println")) {
                                        continue;
                                    }
                                }
                            }
                            return super.visit(block);
                        }
                    });
                }
                return super.visit(node);
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
}*/