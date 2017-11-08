package tianma.learn.ds.AST;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;

import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class ASTTreeParse {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        ASTTreeParse test = new ASTTreeParse();
        test.processJavaFile(new File("./src/main/java/tianma/learn/ds/Test_1.java"));
    }


    public void processJavaFile(File file) throws IOException, MalformedTreeException, BadLocationException {

        System.out.println(System.getProperty("user.dir"));

        /* String str = FileUtils.readFileToString(file);
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        //
        //
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        //
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        System.out.println(options+"");
        parser.setSource(str.toCharArray());
        String unitName = "SimpleCalcutor.java";

        parser.setUnitName(unitName);
        //parser.setProject(source.getJavaProject())
        String[] sources = {"./src/main/java/tianma/learn/ds/lineartable/main/"};
        String[] classpath = {};
        parser.setEnvironment(null,null,null,true);

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        //
        unit.recordModifications();

        ASTVisitorClass visitor = new ASTVisitorClass();
        unit.accept(visitor);*/
        /*IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject project = root.getProject("DataStruc");
        try {
            project.open(null);
        }
        catch (Exception e) {
            System.out.println("Failed to open project");
        }

        IJavaProject javaProject = JavaCore.create(project);*/

        /*ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setResolveBindings(true);
        Map options = JavaCore.getOptions();*/
        String str = FileUtils.readFileToString(file);
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);


        String unitName = "DataStruc/src/main/java/tianma/learn/ds/Test_1.java";
        parser.setUnitName(unitName);
        String[] sources = {"/Users/sharandec7/Desktop/CS474_HW2/DataStruc/src/main/java/"};
        String[] classpath = {""};
        parser.setEnvironment(null,null,null,true);
        // parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setSource(FileUtils.readFileToString(file).toCharArray());
        //parser.setProject(javaProject);
        parser.setBindingsRecovery(true);
        final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        if (unit.getAST().hasResolvedBindings()) {
            System.out.println("Binding activated.\n");
        }
        else {
            System.out.println("Binding is not activated.\n");
        }
        IProblem[] problems = unit.getProblems();
        if (problems != null && problems.length < 0) {
            System.out.println("Got No {} problems compiling the source file: "+problems.length);
            //for (IProblem problem : problems)
                //System.out.println("{}"+problem);
        }

        unit.recordModifications();
        File file_new = new File("./src/main/java/tianma/learn/ds/Test_1.java");
        TraversalManager manager = new TraversalManager();
        //manager.calculateVariableScopes(unit);
        //manager.Instrument2(unit,file_new);
        //manager.calculateExpressionStatements(unit,file_new);
        //manager.printTable();

        TraverseManager3 manager3 = new TraverseManager3();
        manager3.Instrument2(unit,file_new);

        //manager.newVariableScopeImplementation(unit);
        //ASTVisitorClass visitor = new ASTVisitorClass();
        //unit.accept(visitor);

        System.out.println();
        //visitor.printTable();

       /* String source = FileUtils.readFileToString(file);
        Document document = new Document(source);
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setSource(document.get().toCharArray());
        CompilationUnit unit = (CompilationUnit)parser.createAST(null);
        unit.recordModifications();
        */


        // to create a new import
        //------------------------------------------------------------------------
        /*

        AST ast = unit.getAST();
        ImportDeclaration id = ast.newImportDeclaration();
        String classToImport = "ast.Readable";
        id.setName(ast.newName(classToImport.split("\\.")));
        unit.imports().add(id); // add import declaration at end

        String str = FileUtils.readFileToString(file_new);
        Document document = new Document(str);
        TextEdit edits = unit.rewrite(document, null);
        edits.apply(document);
        FileUtils.writeStringToFile(file_new, document.get());*/
        // to save the changed file
        //------------------------------------------------------------------------

        /*
        List<ImportDeclaration> imports = unit.imports();
        for (ImportDeclaration i : imports) {
            System.out.println(i.getName().getFullyQualifiedName());
        }

        // to iterate through methods
        List<AbstractTypeDeclaration> types = unit.types();
        for (AbstractTypeDeclaration type : types) {
            if (type.getNodeType() == ASTNode.TYPE_DECLARATION) {
                // Class def found
                List<BodyDeclaration> bodies = type.bodyDeclarations();
                for (BodyDeclaration body : bodies) {
                    if (body.getNodeType() == ASTNode.METHOD_DECLARATION) {
                        MethodDeclaration method = (MethodDeclaration) body;
                        System.out.println("name: " + method.getName().getFullyQualifiedName());
                    }
                }
            }
        }
        */
    }
}
