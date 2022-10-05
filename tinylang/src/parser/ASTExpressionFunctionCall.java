package parser;

import java.util.ArrayList;

public class ASTExpressionFunctionCall extends ASTExpressionNode{
    public String identifier;
    public ArrayList<ASTExpressionNode> actualParams;

    public ASTExpressionFunctionCall(String identifier, ArrayList<ASTExpressionNode> actualParams){
        this.identifier = identifier;
        this.actualParams = actualParams;
    }

    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
