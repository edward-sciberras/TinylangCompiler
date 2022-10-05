package parser;

public class ASTExpressionNode implements ASTNode{
    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
