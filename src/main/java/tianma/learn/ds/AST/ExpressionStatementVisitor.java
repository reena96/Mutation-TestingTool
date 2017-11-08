package tianma.learn.ds.AST;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;

import java.util.*;

public class ExpressionStatementVisitor extends ASTVisitor{

    public List<SimpleName> list = new ArrayList<>();

    @Override
    public boolean visit(SimpleName simpleName) {
        //System.out.println("inside ESV: "+simpleName);
        this.list.add(simpleName);
        return super.visit(simpleName);
    }

    /*@Override

    public void endVisit(SimpleName simpleName) {

        System.out.println("---------"+list.get(0));
        super.endVisit(simpleName);

    }
    */

    public List<SimpleName> getSimpleNameList() {
        return list;
    }
}
