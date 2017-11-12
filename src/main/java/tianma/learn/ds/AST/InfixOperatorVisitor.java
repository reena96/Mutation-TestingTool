package tianma.learn.ds.AST;


import org.eclipse.jdt.core.dom.*;

import java.util.*;

public class InfixOperatorVisitor extends ASTVisitor{

    public String infixOperatorList = "";

    @Override
    public boolean visit(InfixExpression infixExpression) {
        //System.out.println("inside OPV: "+infixExpression.getOperator());
        if(!infixOperatorList.isEmpty())
            infixOperatorList = infixOperatorList+"@"+infixExpression.getOperator().toString();
        else
            infixOperatorList = infixExpression.getOperator().toString();

        return super.visit(infixExpression);
    }
}