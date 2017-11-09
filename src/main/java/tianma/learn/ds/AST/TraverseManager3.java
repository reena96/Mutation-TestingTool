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

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.eclipse.jdt.core.dom.ASTNode.INITIALIZER;
import static org.eclipse.jdt.core.dom.ASTNode.METHOD_INVOCATION;

public class TraverseManager3 {

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

    HashSet<Scopetable> scope_table = new HashSet<>();
    CompilationUnit unit;
    HashMap<String, String> table = new HashMap<>();
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

    public MethodInvocation addArguments(AST ast, MethodInvocation methodInvocation, HashMap<String, SimpleName> arguments_list) {

        for (Map.Entry<String, SimpleName> argument : arguments_list.entrySet()) {
            methodInvocation = addEachArgument(ast, methodInvocation, argument.getKey());
        }
        return methodInvocation;
    }

    public MethodInvocation addEachArgument(AST ast, MethodInvocation methodInvocation, String stringLiteral) {

        StringLiteral stringLiteralNode = ast.newStringLiteral();
        stringLiteralNode.setLiteralValue(stringLiteral);
        methodInvocation.arguments().add(stringLiteralNode);
        SimpleName variableNode = ast.newSimpleName(stringLiteral);
        methodInvocation.arguments().add(variableNode);

        return methodInvocation;
    }

    public HashMap<String, SimpleName> getSimpleNames(ExpressionStatementVisitor esv) {

        HashMap<String, SimpleName> arguments_list = new HashMap<>();
        for (SimpleName variableName : esv.list) {
            arguments_list.put(variableName.toString(), variableName);
        }

        return arguments_list;
    }

    public void rewriteASTToFile(File file_new, ASTRewrite rewrite) throws IOException {

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
            //System.out.println(document.get());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        try {
            String new_file_name = "."+file_new.toString().split("\\.")[1]+"_1.java";
            File file = new File(new_file_name);
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
            FileUtils.writeStringToFile(file, document.get());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void injectInstrumentation(AST ast, ASTRewrite rewrite, Block block, Statement statement, Expression expression, ExpressionStatementVisitor esv) {

        ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
        Statement addBeforeThisStatement = (Statement) block.statements().get(0);

        String statement_type = statement.getClass().toString().substring(statement.getClass().toString().lastIndexOf('.') + 1);

        int lineNumber = unit.getLineNumber(statement.getStartPosition());

        MethodInvocation methodInvocation = createInstrumMethodNode(ast, lineNumber, statement_type, expression.toString());

        expression.accept(esv);
        HashMap<String, SimpleName> arguments_list = getSimpleNames(esv);

        methodInvocation = addArguments(ast, methodInvocation, arguments_list);

        ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
        listRewrite.insertBefore(statement1, addBeforeThisStatement, null);
    }

    public void Instrument2(CompilationUnit unit, File file_new) throws IOException, BadLocationException {

        //System.out.println("This is TraverseManager3 section : ");

        this.unit = unit;
        AST ast = unit.getAST();
        ASTRewrite rewrite = ASTRewrite.create(ast);

        //adding Template class import statement
        ImportDeclaration id = ast.newImportDeclaration();
        String classToImport = "tianma.learn.ds.Template";
        id.setName(ast.newName(classToImport.split("\\.")));
        ListRewrite unitRewrite = rewrite.getListRewrite(unit, CompilationUnit.IMPORTS_PROPERTY);
        unitRewrite.insertLast(id,null);

        // change class name to new class
        SimpleName oldName = ((TypeDeclaration) unit.types().get(0)).getName();
        SimpleName newName = unit.getAST().newSimpleName(oldName.toString()+"_1");
        rewrite.replace(oldName, newName, null);

        unit.accept(new ASTVisitor() {

            @Override
            public boolean visit(Block block) {

                ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);

                for (Statement currentStatement : (List<Statement>) block.statements()) {

                    if (currentStatement instanceof ExpressionStatement) {

                        ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                        Expression expression = ((ExpressionStatement) currentStatement).getExpression();
                        if (expression.toString().contains("System.out.println")) {
                            continue;
                        }
                        if (!(expression instanceof MethodInvocation)) {

                            String statement_type = expression.getClass().toString().substring(expression.getClass().toString().lastIndexOf('.') + 1);

                            if (!(expression instanceof Assignment && ((Assignment) expression).getRightHandSide() instanceof ClassInstanceCreation)) {

                                int lineNumber = unit.getLineNumber(expression.getStartPosition());

                                MethodInvocation methodInvocation = createInstrumMethodNode(ast, lineNumber, statement_type, expression.toString());

                                expression.accept(esv);
                                HashMap<String, SimpleName> arguments_list = getSimpleNames(esv);

                                methodInvocation = addArguments(ast, methodInvocation, arguments_list);

                                ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
                                listRewrite.insertAfter(statement1, currentStatement, null);
                            }
                        }
                        else if (expression instanceof MethodInvocation && ((MethodInvocation) expression).arguments() != null) {

                            String statement_type = expression.getClass().toString().substring(expression.getClass().toString().lastIndexOf('.') + 1);

                            int lineNumber = unit.getLineNumber(expression.getStartPosition());

                            MethodInvocation methodInvocation = createInstrumMethodNode(ast, lineNumber, statement_type, expression.toString());

                            List<Expression> arguments = ((MethodInvocation) expression).arguments();
                            for (Expression e : arguments) {
                                if ( e instanceof  ClassInstanceCreation || e instanceof  MethodInvocation)
                                    arguments.remove(e);
                            }

                            HashMap<String, SimpleName> arguments_list = getSimpleNames(esv);
                            arguments_list = addInitializersUpdatersToArguments(ast, esv, arguments_list, arguments);

                            methodInvocation = addArguments(ast, methodInvocation, arguments_list);

                            ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
                            listRewrite.insertAfter(statement1, currentStatement, null);
                        }
                    }
                }
                return super.visit(block);
            }

            @Override
            public boolean visit(WhileStatement whileStatement) {

                if (whileStatement.getBody() != null) {

                    ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                    whileStatement.accept(new ASTVisitor() {
                        @Override
                        public boolean visit(Block block) {

                            Expression expression = whileStatement.getExpression();

                            if (block.getParent() == whileStatement && !(expression instanceof MethodInvocation)) {
                                injectInstrumentation(ast, rewrite, block, whileStatement, expression, esv);
                            }
                            return super.visit(block);
                        }
                    });
                }
                return super.visit(whileStatement);
            }

            @Override
            public boolean visit(ForStatement forStatement) {

                if (forStatement.getBody() != null) {

                    ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                    List<Expression> initializers = forStatement.initializers();
                    List<Expression> updaters = forStatement.updaters();
                    Expression expression = forStatement.getExpression();

                    forStatement.accept(new ASTVisitor() {

                        @Override
                        public boolean visit(Block block) {

                            if (block.getParent() == forStatement) {

                                ListRewrite listRewrite = rewrite.getListRewrite(block, Block.STATEMENTS_PROPERTY);
                                Statement addBeforeThisStatement = (Statement) block.statements().get(0);
                                String statement_type = forStatement.getClass().toString().substring(forStatement.getClass().toString().lastIndexOf('.') + 1);
                                int lineNumber = unit.getLineNumber(forStatement.getStartPosition());
                                MethodInvocation methodInvocation = createInstrumMethodNode(ast, lineNumber, statement_type, expression.toString());
                                HashMap<String, SimpleName> arguments_list = getSimpleNames(esv);

                                if (!(expression instanceof MethodInvocation)) {

                                    expression.accept(esv);
                                    arguments_list = getSimpleNames(esv);
                                }
                                arguments_list = addInitializersUpdatersToArguments(ast, esv, arguments_list, initializers);
                                arguments_list = addInitializersUpdatersToArguments(ast, esv, arguments_list, updaters);

                                methodInvocation = addArguments(ast, methodInvocation, arguments_list);

                                ExpressionStatement statement1 = ast.newExpressionStatement(methodInvocation);
                                listRewrite.insertBefore(statement1, addBeforeThisStatement, null);
                            }
                            return super.visit(block);
                        }
                    });
                }
                return super.visit(forStatement);
            }

            @Override
            public boolean visit(IfStatement ifStatement) {

                if (ifStatement.getThenStatement() instanceof Block)
                //&& ifStatement.getElseStatement() instanceof Block
                {
                    ExpressionStatementVisitor esv = new ExpressionStatementVisitor();

                    ifStatement.accept(new ASTVisitor() {
                        int end_counter = 0;

                        @Override
                        public boolean visit(Block block) {

                            end_counter++;
                            Expression expression = ifStatement.getExpression();

                            if ((end_counter < 2) && block.getParent() == ifStatement && !(expression instanceof MethodInvocation)) {
                                injectInstrumentation(ast, rewrite, block, ifStatement, expression, esv);
                            }
                            return super.visit(block);
                        }
                    });
                }
                return super.visit(ifStatement);
            }
        });
        rewriteASTToFile(file_new, rewrite);
    }

    private HashMap<String, SimpleName> addInitializersUpdatersToArguments(AST ast, ExpressionStatementVisitor esv, HashMap<String, SimpleName> arguments_list, List<Expression> list) {

        for (Expression e : list) {
            if (!(e instanceof MethodInvocation)) {
                e.accept(esv);
                arguments_list = getSimpleNames(esv);
            }
        }
        return arguments_list;
    }
}