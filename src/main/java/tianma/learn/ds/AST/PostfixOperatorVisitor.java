package tianma.learn.ds.AST;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PostfixExpression;

public class PostfixOperatorVisitor extends ASTVisitor {

    public String postfixOperatorList = "";

    @Override
    public boolean visit(PostfixExpression postfixExpression) {
        //System.out.println("inside OPV: "+postfixExpression.getOperator());
        if(!postfixOperatorList.isEmpty())
            postfixOperatorList = postfixOperatorList+"@"+postfixExpression.getOperator().toString();
        else
            postfixOperatorList = postfixExpression.getOperator().toString();

        return super.visit(postfixExpression);
    }
}