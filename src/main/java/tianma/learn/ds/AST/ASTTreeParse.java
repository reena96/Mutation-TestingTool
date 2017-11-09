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
        test.processJavaFile(new File("./src/main/java/tianma/learn/ds/string/main/StringMatchSample.java"));
    }


    public void processJavaFile(File file) throws IOException, MalformedTreeException, BadLocationException {

        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        //these four lines are here for resolving the binding
        String unitName = "DataStruc/src/main/java/tianma/learn/ds/sort/SorterMain.java";
        parser.setUnitName(unitName);
        String[] sources = {"/Users/sharandec7/Desktop/CS474_HW2/DataStruc/src/main/java/"};
        String[] classpath = {""};

        parser.setEnvironment(null, null, null, true);
        parser.setSource(FileUtils.readFileToString(file).toCharArray());
        parser.setBindingsRecovery(true);
        final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
        if (unit.getAST().hasResolvedBindings()) {
            System.out.println("Binding activated.\n");
        } else {
            System.out.println("Binding is not activated.\n");
        }
        IProblem[] problems = unit.getProblems();
        if (problems != null && problems.length < 0) {
            System.out.println("Got No {} problems compiling the source file: " + problems.length);
            //for (IProblem problem : problems)
            //System.out.println("{}"+problem);
        }

        unit.recordModifications();

        TraverseManager3 manager3 = new TraverseManager3();
        manager3.Instrument2(unit, file);
        //manager3.calculateVariableScopes(unit);
        //manager3.printTable();

        System.out.println();
    }
}
